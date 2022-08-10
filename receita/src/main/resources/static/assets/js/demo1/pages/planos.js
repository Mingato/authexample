var urlRoot = '';

appReceita.controller('bodyController', function($scope, $http, $window){
    $scope.usuario = {
        id: '',
        nome: '',
        email: '',
        razaoSocial: '',
        perfil: 'USUARIO',
        telefone: ''
    };
    $scope.id = '';
    $scope.nome = '';
    $scope.email = '';
    $scope.razaoSocial = '';
    $scope.perfil = 'USUARIO';
    $scope.telefone = '';

    $scope.senha = '';
    $scope.rsenha = '';

    $scope.usuarios = [];

    $scope.usuarioLogado = {nome : 'Gunter'};

    $scope.className = 'Usuário';

    var getNewAccessToken = function(){
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

    

    var init = function() {
        console.log('iniciando . . ');
        getNewAccessToken();

        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");

    };

    init();

    $window.setInterval( function pegarToken(){ getNewAccessToken(); }, 300000);
});



// Class Definition
var KTLoginGeneral = function() {

    // Public Functions
    return {
        // public functions
        init: function() {
            
        },
        validateForm: function() {
            return validateForm();
        }
    };
}();

// Class Initialization
jQuery(document).ready(function() {
    KTLoginGeneral.init();
});