
var appReceita = angular.module('appReceita', []);
var urlRoot = '';

appReceita.controller('baseController', function($scope, $http, $window) {
    $scope.usuarioLogado = {nome : 'Gunter'};

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
            //$window.localStorage['redCompany_WM3_accessToken'] = response.data.access_token;
        }, function errorCallback(response) {
            console.log('Não conseguiu pegar o access token');
            location.href = '/login.html';
        });
    };

    pegarInformacoesDoUsuario = function(){
        $http({
            method: 'POST',
            url: urlRoot + '/api/usuario/infoPessoais',
            headers: {'Content-Type': 'application/json;'},
            data: {},
            data: {}
        }).then(function successCallback(response) {
            //console.log(response.data);
            $scope.usuarioLogado = response.data;
        }, function errorCallback(response) {
            console.log(response.status);
            if(response.data.error == "invalid_token"){
                getNewAccessToken();
                pegarInformacoesDoUsuario();
            }
        });
    };

    $scope.logout = function(){
        $http({
            method: 'DELETE',
            url: urlRoot + '/token/revoke',
            headers: {'Content-Type': 'application/json;'},
            data: {},
            data: {}
        }).then(function successCallback(response) {
            console.log(response);
            location.href = "/login.html";
        }, function errorCallback(response) {            
        });
    };

   
    init = function(){
        console.log("INICIANDO BASE. . .");
        getNewAccessToken();
        pegarInformacoesDoUsuario();

        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");
    };

    init();

    $window.setInterval( function pegarToken(){ getNewAccessToken(); }, 300000);
});

// Class definition
/*var KTDashboard = function() {

    
    var init = function(){
        console.log('Bem-vindo!');
    };

    return {
        // Init demos
        init: function() {
            // init charts
            init();
            

            // demo loading
            var loading = new KTDialog({'type': 'loader', 'placement': 'top center', 'message': 'Loading ...'});
            loading.show();

            setTimeout(function() {
                loading.hide();
            }, 3000);
        }
    };
}();

// Class initialization on page load
jQuery(document).ready(function() {
    KTDashboard.init();
});*/