//var appReceita = angular.module('appReceita', []);
//var urlRoot = '';
appReceita.controller('bodyController', function($scope, $http, $window) {
    $scope.usuarioLogado = {nome : 'Gunter'};
    $scope.result = '';
    $scope.cnpj = '';
    $scope.tpConsulta = 'suframa';
    $scope.token = '';

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
        
        $("#btnConsultar").addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
        $http({
            method: 'GET',
            url: urlRoot + '/api/receita/cnpj/' + $scope.cnpj + '?tp_consulta=' + $scope.tpConsulta + '&token=' + $scope.token,
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            $("#btnConsultar").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
            console.log("realizou consulta.");            
            $scope.result = JSON.stringify(response.data, null, '\t');
        }, function errorCallback(response) {
            $("#btnConsultar").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
            console.log(response.status);
            if(response.data.error == "invalid_token"){
                $scope.result = JSON.stringify(response.data, null, '\t');;
                getNewAccessToken();
                $scope.consultar();
            }
        });
    };
   
    init = function(){
        console.log("INICIANDO 2 . . .");
        getNewAccessToken();

        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");
    };

    init();

    $window.setInterval( function pegarToken(){ getNewAccessToken(); }, 300000);
       
});