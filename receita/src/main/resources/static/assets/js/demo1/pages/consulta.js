//var appReceita = angular.module('appReceita', []);
//var urlRoot = '';
appReceita.controller('bodyController', function($scope, $http, $window) {
    $scope.usuarioLogado = {nome : 'Gunter'};
    $scope.result = '';
    $scope.cnpj = '';
    $scope.token = '';
    $scope.tp_consulta = 'receita';

    getNewAccessToken = function(){
        credenciais = 'grant_type=refresh_token';

        $http({
            method: 'POST',
            url: urlRoot + '/oauth/token',
            data: credenciais,
            headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Authorization' : 'Basic YW5ndWxhcjpAYW5ndWxhcg=='}
        }).then(function successCallback(response) {
            console.log("pegou novo access token.");
             
        }, function errorCallback(response) {
            console.log('Não conseguiu pegar o access token');
            location.href = '/login.html';
        });
    };
    
    $scope.consultar = function(){
        if(validateForm()){
            console.log('Buscando . . .');
            $("#btnConsultar").addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
            $scope.cnpj = $scope.cnpj.replace('.','');
            $scope.cnpj = $scope.cnpj.replace('.','');
            $scope.cnpj = $scope.cnpj.replace('/','');
            $scope.cnpj = $scope.cnpj.replace('-','');
            $http({
                method: 'GET',
                url: urlRoot + '/api/receita/cnpj/' + $scope.cnpj + '?tp_consulta=' + $scope.tp_consulta + '&token=' + $scope.token,
                headers: {'Content-Type': 'application/json'},
                data: {}
            }).then(function successCallback(response) {
                console.log("realizou consulta.");            
                $scope.result = JSON.stringify(response.data, null, '\t');
                $("#btnConsultar").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
            }, function errorCallback(response) {
                $("#btnConsultar").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                console.log(response)
                console.log(response.data)
                //$scope.result = JSON.stringify(response.data, null, '\t');
                console.log(response.status);
                if(response.status < 500)
                if(response.data.error == "invalid_token"){
                    $scope.result = JSON.stringify(response.data, null, '\t');;
                    getNewAccessToken();
                    $scope.consultar();
                }
            });
        }
    };

    $scope.check = function(tipo){
        if(!$scope.tp_consulta.includes(tipo)) {
            if($scope.tp_consulta.length > 0){
                $scope.tp_consulta = $scope.tp_consulta + ',' +tipo;
            }else{
                $scope.tp_consulta = tipo;
            }
        }else{
            $scope.tp_consulta = $scope.tp_consulta.replace(','+tipo, "");
            $scope.tp_consulta = $scope.tp_consulta.replace(tipo + ',', "");
            $scope.tp_consulta = $scope.tp_consulta.replace(tipo, "");
        }
    };
   
    init = function(){
        console.log("INICIANDO 2 . . .");
        getNewAccessToken();
        initiValidateForm();

        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");
	
    };

    var validateForm = function(){
        console.log('validando form');
        if($('#form_consulta').valid()){
            return true;
        }
        return false;
    }

    var initiValidateForm = function() {

        $("#form_consulta").validate({
            rules: {                
                cnpj: {
                    required: true
                },
                token:{
                    required: true
                }
            }
        });
    }

    init();

    $window.setInterval( function pegarToken(){ getNewAccessToken(); }, 10 * 60 * 1000);
       
});