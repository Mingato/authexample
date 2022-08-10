
var showErrorMsg = function(form, type, msg) {
    var alert = $('<div class="kt-alert kt-alert--outline alert alert-' + type + ' alert-dismissible" role="alert">\
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>\
        <span></span>\
    </div>');

    form.find('.alert').remove();
    alert.prependTo(form);
    //alert.animateClass('fadeIn animated');
    KTUtil.animateClass(alert[0], 'fadeIn animated');
    alert.find('span').html(msg);
}

appReceita.controller('bodyController', function($scope, $http, $window){
    $scope.nome = '';
    $scope.email = '';
    $scope.razaoSocial = '';
    $scope.perfil = 'USUARIO';
    $scope.telefone = '';

    $scope.senhaAtual = '';
    $scope.senha = '';
    $scope.rsenha = '';

    $scope.usuarioLogado = {nome : 'Gunter'};


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

    var pegarInformacoesDoUsuario = function(){
        console.log('pegarInformacoesDoUsuario');
        $http({
            method: 'POST',
            url: urlRoot + '/api/usuario/infoPessoais',
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            console.log(response.data);
            $scope.usuarioLogado = response.data;
            $scope.email =  $scope.usuarioLogado.email;
            $scope.nome =  $scope.usuarioLogado.nome;
            $scope.razaoSocial =  $scope.usuarioLogado.razaoSocial;
            $scope.perfil = 'USUARIO';
            $scope.telefone =  $scope.usuarioLogado.telefone;

        }, function errorCallback(response) {
            console.log(response.status);
            if(response.data.error == "invalid_token"){
                getNewAccessToken();
                pegarInformacoesDoUsuario();
            }
        });
    };

    $scope.alterar = function(){
        console.log('alterarndo usuario . . .' + $scope.nome);
        
        if(KTLoginGeneral.validateForm()){
            console.log(convertToJSON());
            $('#btnAlterar').addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
            $http({
                method: 'POST',
                url: urlRoot + '/api/usuario/seAlterar',
                data: convertToJSON(),
                headers: {
                    'Content-Type': 'application/json'
                },
            }).then(function successCallback(response) {
                console.log("criou usuario.");
                $('#btnAlterar').removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);                        
                showErrorMsg($("#form_alteracaoUsuario"), "success", "Usuario alterado!");
            }, function errorCallback(response) {
                console.log('Erro ao alterar usuario');
                $('#btnAlterar').removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                showErrorMsg($("#form_alteracaoUsuario"), "danger", "Erro ao alterar!");
            });

        }    
    };

    var convertToJSON = function(){
        var data = {};
        
        data.id = '';
        data.nome = $scope.nome;
        data.email = $scope.email;
        data.razaoSocial = $scope.razaoSocial;
        data.perfil = $scope.usuarioLogado.perfil;
        data.telefone = $scope.telefone;
        data.senha = $scope.senha;
      
        return JSON.stringify(data);
    }

    $scope.alterarSenha = function(){
        console.log('alterarndo senha . . .');

        if($scope.senha == $scope.rsenha){
            if(KTLoginGeneral.validateFormSenha()){
                $('#btnAlterarSenha').addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
                if(senhaValida()){
                    $http({
                        method: 'POST',
                        url: urlRoot + '/api/usuario/senha/alterar/' + $scope.senhaAtual + '?novaSenha='+$scope.senha,
                        headers: {'Content-Type': 'application/json;'},
            data: {}
                    }).then(function successCallback(response) {
                        console.log("senha alterada.");
                        $('#btnAlterarSenha').removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);                        
                        showErrorMsg($("#form_alteracaoSenha"), "success", "Senha alterada!");
                    }, function errorCallback(response) {
                        console.log('Erro ao alterar senha');
                        $('#btnAlterarSenha').removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                        showErrorMsg($("#form_alteracaoSenha"), "danger", "Erro ao alterar!");
                    });
                }else{
                    showErrorMsg($("#form_alteracaoSenha"), "warning", "Senha Atual incorreta!");
                }
    
            }
        }else{
            showErrorMsg($("#form_alteracaoSenha"), "warning", "Senhas estão diferentes!");
        }
    };

    var senhaValida = function(){
        var credenciais = 'username='+ $scope.usuarioLogado.email + 
                          '&password='+ $scope.senhaAtual +
                          '&client=angular&grant_type=password';
        //console.log(credenciais);

        $http({
            method: 'POST',
            url: urlRoot + '/oauth/token' + '?' + credenciais,
            headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Authorization' : 'Basic YW5ndWxhcjpAYW5ndWxhcg=='}
        }).then(function successCallback(response) {
            console.log("senha valida.");
            return true;
        }, function errorCallback(response) {
            console.log('erro ao fazer loging' + response.status + ' ');
        });

        return false;
    };

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
    

    var init = function() {
        console.log('iniciado');
        getNewAccessToken();
        pegarInformacoesDoUsuario();

        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");
    };

    init();

    $window.setInterval( function pegarToken(){ getNewAccessToken(); }, 300000);
});



// Class Definition
var KTLoginGeneral = function() {


    var validateFormAlterarUsuario = function(){
        console.log('validando form');
        if($("#form_alteracaoUsuario").valid()){
            console.log("campos validos. ");
            return true;
        }
        return false;
    }
    
    var validateFormAlterarSenha = function(){
        console.log('validando form');
        if($("#form_alteracaoSenha").valid()){
            console.log("campos validos. ");
            return true;
        }
        return false;
    }
    
    
    var initiValidateForm = function() {
    
        $("#form_alteracaoUsuario").validate({
            rules: {
                nome: {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                },
                razaoSocial: {
                    required: true
                },
                telefone: {
                    //required: true,
                    phoneUS: true 
                }
            }
        });
    
        $("#form_alteracaoSenha").validate({
            rules: {
                senhaAtual: {
                    required: true
                },
                senha: {
                    required: true
                },
                rsenha: {
                    required: true
                }
            }
        });
    }

    // Public Functions
    return {
        // public functions
        init: function() {            
            initiValidateForm();
        },
        validateForm: function() {            
            return validateFormAlterarUsuario();
        },
        validateFormSenha: function() {            
            return validateFormAlterarSenha();
        }
    };
}();

// Class Initialization
jQuery(document).ready(function() {
    KTLoginGeneral.init();
});