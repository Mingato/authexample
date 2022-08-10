//var appReceita = angular.module('appReceita', []);
//var urlRoot = '';
appReceita.controller('bodyController', function($scope, $http, $window) {
    $scope.usuarioLogado = {nome : 'Gunter'};
    $scope.token = 'ASFUYSGDFSDFH9ASDASDAJSBD8QH3EGB-ASD-ASDASD_TESTE';

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
    
    var buscarToken = function(){
        $http({
            method: 'GET',
            url: urlRoot + '/api/receita/token',
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            console.log("realizou consulta.");
            $scope.token = response.data.token;
        }, function errorCallback(response) {
            console.log(response.status);
            if(response.data.error == "invalid_token"){
                $scope.result = JSON.stringify(response.data, null, '\t');;
                getNewAccessToken();
                buscarToken();
            }        
        });
    };

    $scope.gerarNovoToken = function(){
        
        $("#btnGerarNovoToken").addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
        $http({
            method: 'POST',
            url: urlRoot + '/api/receita/token',
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            $("#btnGerarNovoToken").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
            console.log("criou novo token.");            
            $scope.token = response.data.token;
        }, function errorCallback(response) {
            $("#btnGerarNovoToken").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
            console.log(response.status);
            if(response.data.error == "invalid_token"){
                $scope.result = JSON.stringify(response.data, null, '\t');;
                getNewAccessToken();
                $scope.gerarNovoToken();
            } 
        });
    };
   
    init = function(){
        console.log("INICIANDO 2 . . .");
        getNewAccessToken();
        
        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");
        
        buscarToken();
    };

    init();
});