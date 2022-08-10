//var appReceita = angular.module('appReceita', []);
//var urlRoot = '';

appReceita.controller('bodyController', function($scope, $http, $window) {
    $scope.usuarioLogado = {nome : 'Gunter'};
    $scope.consultas = '';
    $scope.usuarios = [];
    $scope.totalTipoConsultas = {
        sintegra : 0,
        receita : 0,
        suframa : 0,
        simples : 0,
        cnpj : 0,
        cpf : 0,
        cnpjConsultados : 0,
        listaCnpjConsultados : [],
        cpfConsultados : 0,
        listaCpfConsultados : []
    };
    

    getNewAccessToken = function(){
        credenciais = 'grant_type=refresh_token';

        $http({
            method: 'POST',
            url: urlRoot + '/oauth/token',
            data: credenciais,
            headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Authorization' : 'Basic YW5ndWxhcjpAYW5ndWxhcg=='}
        }).then(function successCallback(response) {
            //console.log("pegou novo access token.");
             
        }, function errorCallback(response) {
            //console.log('Não conseguiu pegar o access token');
            location.href = '/login.html';
        });
    };
    
    pegarInformacoesDoUsuario = function(){
        $http({
            method: 'POST',
            url: urlRoot + '/api/usuario/infoPessoais',
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            //console.log(response.data);
            $scope.usuarioLogado = response.data;
            
            if($scope.usuarioLogado.perfil == 'ADMINISTRADOR'){
                $('#clientHeader').css('display', 'flex')
            }
            
        }, function errorCallback(response) {
            console.log(response.status);
            if(response.data.error == "invalid_token"){
                getNewAccessToken();
                pegarInformacoesDoUsuario();
            }
        });
    };

    var buscarConsultas = function(tempoInicial){
        console.log("BuscarConsultas");
        $http({
            method: 'GET',
            url: urlRoot + '/api/receita/consultas/nome/'+$scope.usuarioLogado.nome+'/' + ((new Date()).getTime() - tempoInicial),
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            console.log("buscou consultas.");
            $scope.consultas = response.data
            //console.log(JSON.stringify(response.data, null, '\t'));
            KTConsultasGeneral.desenharGrafico(createData(response.data));
            KTConsultasGeneral.refreshTable(response.data);
            
            if(response.data.length <= 0){
                $scope.buscarConsultas('')
            }
        }, function errorCallback(response) {
            console.log(response.status + '\n ' + console.log(JSON.stringify(response.data, null, '\t')));
            if(response.data.error == "invalid_token"){
                getNewAccessToken();
                $scope.buscarConsultar();
            }
        });
    };

    $scope.buscarConsultas = function(nomeUsuario){
        console.log('GET consultas')
        //console.log($('#txtDate')[0].value);
        var uri = '';

        if(nomeUsuario == undefined || nomeUsuario == ''){
            nomeUsuario = $scope.usuarioLogado.nome;
        }

        if($scope.usuarioLogado.perfil == 'ADMINISTRADOR'){ 
            console.log('usuario ' + nomeUsuario)
            uri += 'nome/' + nomeUsuario + '/'
        }

        
        if($('#txtDate')[0].value == ''){
            uri += (1000 * 60 * 60 * 24 * 07)
        }else{
            uri += $('#txtDate')[0].value.substring(0,10) +  '/' + $('#txtDate')[0].value.substring(13);
        }
        
        //console.log('GET consultas')
        //console.log(uri)
        $("#btnBuscarConsultas").addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
        $http({
            method: 'GET',
            url: urlRoot + '/api/receita/consultas/' + uri,
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            $("#btnBuscarConsultas").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
            console.log("buscou consultas.");
            $scope.consultas = response.data
            //console.log(JSON.stringify(response.data, null, '\t'));
            KTConsultasGeneral.desenharGrafico(createData(response.data));
            KTConsultasGeneral.refreshTable(response.data);

            if(response.data.length <= 0){
                swal.fire("Usuário não possui consultas ", "", "warning");
            }

        }, function errorCallback(response) {
            $("#btnBuscarConsultas").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
            console.log('erro ao buscar consultas ' + response.status + '\n' + console.log(JSON.stringify(response.data, null, '\t')));
        });
    };

    
    var getUsuarios = function(){
        //if($scope.usuarioLogado.perfil != 'ADMINISTRADOR'){ return;}
        //pega as categorias
        $http({
            method: 'GET',
            url: urlRoot + 'api/usuario/todos',
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            //console.log(response.data)
            $scope.usuarios = response.data;

            $("#categoria").change(function() {
                $scope.buscarConsultas(this.value)
            });

        }, function errorCallback(response) {
            tratarRespostasDosResquests(response);
            console.log(response.status);
            if(response.data.error == "invalid_token"){
                getNewAccessToken();
                getUsuarios();
            }
        });
    };

    var createData = function(consultas){
        $scope.totalTipoConsultas.cnpj = 0;
        $scope.totalTipoConsultas.cpf = 0;
        $scope.totalTipoConsultas.sintegra = 0;
        $scope.totalTipoConsultas.receita = 0;
        $scope.totalTipoConsultas.simples = 0;
        $scope.totalTipoConsultas.suframa = 0;

        var data = [];
        if(consultas.length > 0){
            var consultasGrafico = {};
            var date = new Date(consultas[0].dataConsulta);
            var diaAtual = date.getDate();

            consultasGrafico.a = 0;
            consultasGrafico.y = date.getDate() + '/' + addZero(date.getMonth() + 2) + '/' + date.getFullYear();

            for (i = 0; i < consultas.length; i++) {
                verificarTipo(consultas[i]);
                date = new Date(consultas[i].dataConsulta);
                            
                if(diaAtual == date.getDate()){
                    consultasGrafico.a = consultasGrafico.a + 1;
                }else{
                    data.push(consultasGrafico);

                    consultasGrafico = new Object();
                    consultasGrafico.a = 0;
                    consultasGrafico.y = date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear();

                    diaAtual = date.getDate();
                }
                
            }

            $scope.totalTipoConsultas.cnpj +=
                $scope.totalTipoConsultas.receita +
                $scope.totalTipoConsultas.sintegra +
                $scope.totalTipoConsultas.simples +
                $scope.totalTipoConsultas.suframa;
            
            data.push(consultasGrafico);
        }else{
            console.log('Não possui registros');
        }
        return data;
    }
   
    verificarTipo = function(consulta){
    	
        if(consulta.tipoConsulta == 'RECEITA'){
            $scope.totalTipoConsultas.receita += 1;
            verificarSeCnpjJaFoiConsultado(consulta);
        }else if(consulta.tipoConsulta == 'SINTEGRA'){
            $scope.totalTipoConsultas.sintegra += 1;
            verificarSeCnpjJaFoiConsultado(consulta);
        }else if(consulta.tipoConsulta == 'SIMPLES'){
            $scope.totalTipoConsultas.simples += 1;
            verificarSeCnpjJaFoiConsultado(consulta);
        }else if(consulta.tipoConsulta == 'SUFRAMA'){
            $scope.totalTipoConsultas.suframa += 1;
            verificarSeCnpjJaFoiConsultado(consulta);
        }else if(consulta.tipoConsulta == 'CPF'){
            $scope.totalTipoConsultas.cpf += 1;
            verificarSeCpfJaFoiConsultado(consulta);
        }
    }

    verificarSeCnpjJaFoiConsultado = function(consulta){
        if(!$scope.totalTipoConsultas.listaCnpjConsultados.includes(consulta.chave)){
            $scope.totalTipoConsultas.cnpjConsultados += 1;
            $scope.totalTipoConsultas.listaCnpjConsultados.push(consulta.chave);
        }
        

    }

    verificarSeCpfJaFoiConsultado = function(consulta){
        if(!$scope.totalTipoConsultas.listaCpfConsultados.includes(consulta.chave)){
            $scope.totalTipoConsultas.cpfConsultados += 1;
            $scope.totalTipoConsultas.listaCpfConsultados.push(consulta.chave);
        }
        

    }

    addZero = function(n){
        if(n < 10){
            return '0' + n;
        }

        return n;
    }

    init = function(){
        console.log("INICIANDO 2 . . .");
        getNewAccessToken();
        pegarInformacoesDoUsuario();

        $('#kt_daterangepicker_2').daterangepicker({
            language : 'br',
            buttonClasses: ' btn',
            applyClass: 'btn-primary',
            cancelClass: 'btn-secondary',
            format: 'DD-MM-YYYY',
        }, function(start, end, label) {
            $('#kt_daterangepicker_2 .form-control').val( start.format('DD-MM-YYYY') + ' | ' + end.format('DD-MM-YYYY'));

        });

        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");

        getUsuarios();

        setTimeout(function() {
            buscarConsultas(1000 * 60 * 60 * 24 * 07);
        }, 3000);
        
    };

    init();

    $window.setInterval( function pegarToken(){ getNewAccessToken(); }, 300000);

    $scope.logout = function(){
        $http({
            method: 'DELETE',
            url: urlRoot + '/token/revoke',
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            console.log(response);
            location.href = "/login.html";
        }, function errorCallback(response) {            
        });
    };


});


// Class Definition
var KTConsultasGeneral = function() {

    var desenharGrafico = function(consultas){
        //limpar o grafico
        $("#divGrafico").empty();
        $("#divGrafico").prepend("<div id='kt_morris_3' style='height:500px;width: 100%'></div>");
        
        new Morris.Bar( {
            element:"kt_morris_3", data: consultas, xkey:"y", ykeys:["a"], labels:["Consultas"], barColors:["#24a5ff"]

        })
    }

    var refreshTable = function(dados) {
        console.log('Desenhando tabela');
        //console.log(dados);
        $('#local_data').remove();
        $('#datatableContent').append('<div class="kt-datatable" id="local_data"></div>');
        var datatable = $('#local_data').KTDatatable({
			// datasource definition
			data: {
				type: 'local',
				source: dados,
				pageSize: 10,
			},

			// layout definition
			layout: {
				scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
				// height: 450, // datatable's body's fixed height
				footer: false, // display/hide footer
			},

			// column sorting
			sortable: true,

			pagination: true,

			search: {
				input: $('#generalSearch'),
			},

			// columns definition
			columns: [
				{
					field: 'dataConsulta',
                    title: 'dataConsulta',
                    template: function(row) {
						return convertTimeStampToData(row.dataConsulta);
					},
				}, {
					field: 'tipoConsulta',
                    title: 'tipoConsulta',
				}, {
					field: 'parametro',
					title: 'parametro',
				}, {
					field: 'chave',
                    title: 'chave',
                    sortable: false,
				}, {
					field: 'servico',
                    title: 'servico',
				},{
					field: 'source',
                    title: 'source',
                    sortable: false,
				},
			],
        });
        
    }

    var convertTimeStampToData = function(data){
        var newDate = new Date(data);
        //console.log(newDate);
        
		data =    addZero(newDate.getDate()) +
			'/' + addZero(newDate.getMonth()+1) +
			'/' + newDate.getFullYear() +
            ' ' + newDate.getHours() +
            ':' + newDate.getMinutes();

        return data;
    }

    var addZero = function(data){

        if(data < 10){
            return '0' + data;
        }

        return data;
    }
    
    var init = function(){

        /*new Morris.Bar( {
            element:"kt_morris_3", data:[
            {
                y: "2006", a: 100//, b: 90
            }
            ], xkey:"y", ykeys:["a"], labels:["Consultas"], barColors:["#24a5ff"]
        }
        )*/
    }
   

    // Public Functions
    return {
        // public functions
        init: function() {            
            init();
        },
        desenharGrafico: function(consultas) {
            desenharGrafico(consultas);
        },
        refreshTable: function (dados) {
            refreshTable(dados);
        }
    };
}();

// Class Initialization
jQuery(document).ready(function() {
    KTConsultasGeneral.init();
});