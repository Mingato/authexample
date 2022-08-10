package com.redcompany.receita.domain.servico;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.redcompany.receita.domain.Receita;
import com.redcompany.receita.domain.Simples;
import com.redcompany.receita.domain.Sintegra;
import com.redcompany.receita.domain.repositorio.RepositorioDeReceita;
import com.redcompany.receita.domain.repositorio.RepositorioDeSimples;
import com.redcompany.receita.domain.repositorio.RepositorioDeSintegra;
import com.redcompany.receita.infra.exception.ConsultaDeCnpjException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@Component
public class ServerConnection {

	@Autowired
    private RepositorioDeReceita repositorioDeReceita;
	
	@Autowired
    private RepositorioDeSintegra repositorioDeSintegra;
	
	@Autowired
    private RepositorioDeSimples repositorioDeSimples;
	
	
	public static final String SITE_SINTEGRA = "SINTEGRAWS";
	public static final String SITE_RECEITA = "RECEITAWS";
	public static final String SITE_CNPJA = "CNPJA";
	
	private final String TOKEN_RECEITA = "a4b6cc3146b1401e02af664cc5161b2c3140ffbe0c65bb4580fabd81774323d0";//TODO: adicionar token
	
    private final String TOKEN_SINTEGRA = "72678677-D2E9-4A9A-A9CB-27F710A2B422";//"6B6A4ADB-5748-4289-8FBA-2B7868DD5BB0";
	
    private final String URL_RECEITA = "https://www.receitaws.com.br/v1/cnpj/";

    private final String URL_SINTEGRA = "https://www.sintegraws.com.br/api/v1/execute-api.php";
    
    private final String URL_SINTEGRA_CONSULTA_SALDO = "https://www.sintegraws.com.br/api/v1/consulta-saldo.php";
	
    
    public static enum Fornecedores {
    	SINTEGRAWS(SITE_SINTEGRA, "https://www.sintegraws.com.br/api/v1/execute-api.php", "72678677-D2E9-4A9A-A9CB-27F710A2B422"),
    	RECEITAWS(SITE_RECEITA, "https://www.receitaws.com.br/v1/cnpj/", "a4b6cc3146b1401e02af664cc5161b2c3140ffbe0c65bb4580fabd81774323d0"),
    	CNPJA(SITE_CNPJA, "https://api.cnpja.com.br/companies/", "e7062e64-2f60-46ec-b435-c55c5134889b-d97c58ce-f425-4f98-8973-16a027574459");

        private final String fornecedor;
        private final String url;
        private final String token;

        private Fornecedores(String fornecedor, String url, String token) {
            this.fornecedor = fornecedor;
            this.url = url;
            this.token = token;
        }

        public String getFornecedor() {
            return fornecedor;
        }

        public String getUrl() {
            return url;
        }

        public String getToken(){
            return token;
        }
    }
    
    public static enum TipoConsulta {
    	RECEITA("RF"), //RECEITA
		SINTEGRA("ST"), //SINTEGRA
		SIMPLES("SN"), //SIMPLES NACIONAL
		SUFRAMA("SF"),  //SUFRAMA
    	CPF("CPF");  //CPF
    	
    	private String descricao;

    	TipoConsulta(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
	};
    
    public ServerConnection() {
        super();
    }

    
    public JSONObject buscarReceita(String chave, String dataNascimento, String json, Fornecedores fornecedor, TipoConsulta plugin, String cache_max_age) throws JSONException {
    	String urlConculta = "";
    	String authorization = null;
    	int timeout = 1000 * 50;
    	
    	switch (fornecedor.getFornecedor()) {
    	case SITE_CNPJA:
    		if(cache_max_age.equals("")) {
    			urlConculta = fornecedor.getUrl() + chave + "?company_max_age=90&sintegra_max_age=90&simples_max_age=90&";
    		}else {
    			urlConculta = fornecedor.getUrl() + chave + "?company_max_age=" + cache_max_age +
    														"&sintegra_max_age=" + cache_max_age +
    														"&simples_max_age=" + cache_max_age + "&";
    		}
    		
			authorization = fornecedor.getToken();
			timeout = 10000 * 50;
			break;
		case SITE_SINTEGRA:
			
			if(!plugin.getDescricao().equalsIgnoreCase("cpf")) {
				urlConculta = fornecedor.getUrl() + "?token=" + fornecedor.getToken() 
											+ "&cnpj=" + chave + "&plugin=" + plugin.getDescricao();
    		}else {
    			urlConculta = fornecedor.getUrl() + 
    					"?token=" + fornecedor.getToken() + 
    					"&cpf=" + chave +
    					"&data-nascimento=" + dataNascimento +
						"&plugin=" + plugin.getDescricao();
    			//https://www.sintegraws.com.br/api/v1/execute-api.php?token=72678677-D2E9-4A9A-A9CB-27F710A2B422&cpf=
    		}
			
			break;
			
		case SITE_RECEITA:
			urlConculta = URL_RECEITA + chave;    
			break;

		default:
			break;
		}

    	
    	if(plugin != null) {
    		System.out.println("plugin != null");
    		if(plugin == TipoConsulta.SUFRAMA) {
    			System.out.println("EH SUFRAMA");
    			timeout = 1000 * 500;
    		}
    	}

    	JSONObject resposta = sendRequest(urlConculta, json, "GET", authorization, timeout);
    	
    	resposta = verificarSeEhCNPJA(resposta, fornecedor, plugin);
    	
    	if(resposta != null) {
    		
    		try {
    			if(resposta.getString("status").equalsIgnoreCase("OK")) {
    				if(!resposta.has("code")) {
    					resposta.put("code", "0");
    					resposta.put("message", "Consulta Realizada");
    				}
    			}else {
    				resposta.put("code", "5");
    				if(resposta.has("message")) {
    					resposta.put("message", resposta.getString("message"));
    				}else {
    					resposta.put("message", "Erro na consulta com os nossos fornecedores");
    				}
    			}
    		}catch (Exception e) {
    			resposta.put("status", "OK");
    			resposta.put("code", "0");
    			resposta.put("message", "Consulta Realizada");
    			System.out.println("adicionou status");
			}
    		
    	}
    	
    	System.out.println("---------- resposta: " + resposta);
    	return resposta;
    }

    private JSONObject verificarSeEhCNPJA(JSONObject resposta, Fornecedores fornecedor, TipoConsulta plugin) {
    	if(fornecedor == Fornecedores.CNPJA) {
    		System.out.println("EhCNPJA");
    		Gson gson = new Gson();
    	
			Receita receita = JsonParser.CNPJAToReceita(resposta);
			Sintegra sintegra = JsonParser.CNPJAToSintegra(resposta);
			Simples simples = JsonParser.CNPJAToSimples(resposta);
		
		
    		System.out.println("1) receita: " + receita.toString());
    		
    		repositorioDeReceita.save(receita);
    		System.out.println("EhCNPJA " + receita.getCnpj());
    		repositorioDeSintegra.save(sintegra);
    		System.out.println("EhCNPJA " + sintegra.getCnpj());
    		repositorioDeSimples.save(simples);
    		System.out.println("EhCNPJA " + simples.getCnpj());
    		
    		System.out.println("2) receita: " + receita.toString());
    		
    		if(plugin == TipoConsulta.RECEITA) {
				resposta = new JSONObject(gson.toJson(receita));
			}else if(plugin == TipoConsulta.SINTEGRA) {
				resposta = new JSONObject(gson.toJson(sintegra));
			}else if(plugin == TipoConsulta.SIMPLES) {
				resposta = new JSONObject(gson.toJson(simples));
			}
	    		
    		System.out.println("3) receita: " + receita.toString());
			
		}
		
		return resposta;
	}


	public JSONObject sendRequest(String urlConculta, String json, String method, String authorization, int timeout) throws JSONException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        boolean error = false;
        
        try {
            String jsonText = "";
            URL url = new URL(urlConculta);
            System.out.println("conectando . . .\n" + urlConculta);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
        	connection.setConnectTimeout(timeout);
        	connection.setReadTimeout(timeout);
            
            if(urlConculta.contains("URL_RECEITA")) {
            	connection.setRequestProperty("Authorization", "Bearer " + TOKEN_RECEITA);
            }
            connection.setRequestMethod(method);
            
            if(authorization != null) {
            	connection.setRequestProperty("authorization", authorization);
            }
            
            System.out.println("conectando2 . . .");
            connection.connect();
            
            if(!method.equals("GET")) {
            	System.out.println("writer . . .");
            	OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
	            wr.write(json);
	            wr.flush();
	            wr.close();
	            System.out.println("writ done. . .");
            }
            
            if (connection.getResponseCode() < 400) {
            	System.out.println("reader . . .");
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                System.out.println("lendo json . . .");
                while ((line = reader.readLine()) != null) {
                    jsonText += line;
                    buffer.append(line + "\n");
                }

                System.out.println("leu json . . .");
                reader.close();
                stream.close();
                connection.disconnect();
                return new JSONObject(jsonText);
            }else {
            	
                System.out.println("ERRO_CODIGO: " + connection.getResponseCode());
                connection.disconnect();
            	throw new ConsultaDeCnpjException("ERROR", "Erro na busca do CNPJ | ERRO_CODIGO: " + connection.getResponseCode());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            connection.disconnect();
            System.out.println("Erro na conneccao: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            connection.disconnect();
            System.out.println("Erro na conneccao: " + e.getMessage());
        } finally {
        	System.out.println("Finalisando conneccao");
        	try {
				System.out.println("Code: " + connection.getResponseCode());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            error = true;
        }
        
        if(error) {
        	connection.disconnect();
        	throw new ConsultaDeCnpjException("ERROR", "Erro na busca do CNPJ");
        }
        connection.disconnect();
        return null;
    }

	public JSONObject sendRequest2(String urlConculta, String json, String method, String authorization, int timeout) throws JSONException {
        
		OkHttpClient client = new OkHttpClient();

		System.out.println("authorization: " + authorization);
		Request request = new Request.Builder()
		  .url(urlConculta)
		  .get()
		  .addHeader("authorization", authorization)
		  .build();

		try {
			Response response = client.newCall(request).execute();
			
			System.out.println(response.message());
			System.out.println(response.body().string());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return null;
   }

	public JSONObject buscarCredito() {
		HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            String jsonText = "";
            URL url = new URL(URL_SINTEGRA_CONSULTA_SALDO + "?token=" + TOKEN_SINTEGRA);
            System.out.println("conectando . . .");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(1000 * 7);
            connection.setReadTimeout(1000 * 10);
            connection.setRequestMethod("GET");
            
            System.out.println("conectando2 . . .");
            connection.connect();
            System.out.println("conectado . . .");
            
            if (connection.getResponseCode() < 400) {
            	System.out.println("reader . . .");
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                System.out.println("lendo json . . .");
                while ((line = reader.readLine()) != null) {
                    jsonText += line;
                    buffer.append(line + "\n");
                }

                System.out.println("leu json . . .");
                return new JSONObject(jsonText);
            }else {
                System.out.println("ERRO_CODIGO: " + connection.getResponseCode());
                return null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
	}

}
