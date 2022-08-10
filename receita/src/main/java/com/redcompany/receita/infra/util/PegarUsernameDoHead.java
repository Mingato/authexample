package com.redcompany.receita.infra.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;


@Service
public class PegarUsernameDoHead {

	
	public String pegarUsername(String token){
		System.out.println("PegarUsernameDoHead.pegarUsuario ");
		
		String accessToken = token.substring(token.indexOf(" "));

		String email = getEmail(decodeJWT(accessToken));
		System.out.println("email33: '" + email + "'");
		
		return email;
	}
	
	
	private String decodeJWT(String jwtToken){
       //System.out.println("------------ Decode JWT ------------");
       String[] split_string = jwtToken.split("\\.");
       //String base64EncodedHeader = split_string[0];
       String base64EncodedBody = split_string[1];
       //String base64EncodedSignature = split_string[2];

       //System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
       Base64 base64Url = new Base64(true);
       //String header = new String(base64Url.decode(base64EncodedHeader));
       //System.out.println("JWT Header : " + header);


       System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
       String body = new String(base64Url.decode(base64EncodedBody));
       System.out.println("JWT Body : "+body);
       
       return body;
   }
	
	public String getEmail(String jsonString) {
		//
		try {
			JSONObject json = new JSONObject(jsonString);
			return (String) json.get("user_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "";
    }
}
