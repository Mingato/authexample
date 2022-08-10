"use strict";

// Class Definition
var KTLoginGeneral = function() {

    var urlRoot = '';

    var login = $('#kt_login');

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

    // Private Functions
    var displaySignUpForm = function() {
        login.removeClass('kt-login--forgot');
        login.removeClass('kt-login--signin');

        login.addClass('kt-login--signup');
        KTUtil.animateClass(login.find('.kt-login__signup')[0], 'flipInX animated');
    }

    var displaySignInForm = function() {
        login.removeClass('kt-login--forgot');
        login.removeClass('kt-login--signup');

        login.addClass('kt-login--signin');
        KTUtil.animateClass(login.find('.kt-login__signin')[0], 'flipInX animated');
        //login.find('.kt-login__signin').animateClass('flipInX animated');
    }

    var displayForgotForm = function() {
        login.removeClass('kt-login--signin');
        login.removeClass('kt-login--signup');

        login.addClass('kt-login--forgot');
        //login.find('.kt-login--forgot').animateClass('flipInX animated');
        KTUtil.animateClass(login.find('.kt-login__forgot')[0], 'flipInX animated');

    }

    var handleFormSwitch = function() {
        $('#kt_login_forgot').click(function(e) {
            e.preventDefault();
            displayForgotForm();
        });

        $('#kt_login_forgot_cancel').click(function(e) {
            e.preventDefault();
            displaySignInForm();
        });

        $('#kt_login_signup').click(function(e) {
            e.preventDefault();
            displaySignUpForm();
        });

        $('#kt_login_signup_cancel').click(function(e) {
            e.preventDefault();
            displaySignInForm();
        });
    }

    var validateForm = function(){
        console.log('validando form');
        if($('#formLogin').valid()){
            return true;
        }
        return false;
    }


    var validateFormAlteraSenha = function(){
        console.log('validando form');
        if($("#form_alterasenha").valid()){
            console.log("campos validos. ");
            return true;
        }
        return false;
    }

    var validateFormCriarUsuario = function(){
        console.log('validando form');
        if($("#form_criacaoUsuario").valid()){
            console.log("campos validos. ");
            return true;
        }
        return false;
    }
    

    var initiValidateForm = function() {

        $("#formLogin").validate({
            rules: {
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true
                }
            }
        });

        $("#form_alterasenha").validate({
            rules: {
                email: {
                    required: true,
                    email: true
                }
            }
        });

        $("#form_criacaoUsuario").validate({
            rules: {
                fullname: {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                },
                passwordCriar: {
                    required: true
                },
                rpassword: {
                    required: true
                },
                agree: {
                    required: true
                },
                razaoSocial: {
                    required: true
                },
                telefone: {
                    required: true,
                    phoneUS: true 
                }
            }
        });
    }

    

    var initAngular = function(){
        var app = angular.module('myApp', []);
        
        app.controller('loginController', function($scope, $http, $window, $document){
            $scope.username = "";
            $scope.password = "";
            $scope.email = "";
            
            $scope.rpassword = '';
            $scope.nome = '';
            $scope.senha = '';
            $scope.razaoSocial = '';
            $scope.perfil = 'USUARIO';
            $scope.telefone = '';
            
            $scope.logar = function(){
                console.log("logando . . . ");
                if(validateForm()){
                    $("#kt_login_signin_submit").addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
                    console.log("campos validos. ");
                    login();
                }
            };

            var login = function(){
                var credenciais = 'username='+ $scope.username + 
                                  '&password='+ $scope.password +
                                  '&client=angular&grant_type=password';
                //console.log(credenciais);

                $http({
                    method: 'POST',
                    url: urlRoot + '/oauth/token' + '?' + credenciais,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                                'Authorization' : 'Basic YW5ndWxhcjpAYW5ndWxhcg=='}
                }).then(function successCallback(response) {
                    console.log("realizou login.");
                    clicouEmLembrar();
                    redirecionar();
                }, function errorCallback(response) {
                    $("#kt_login_signin_submit").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                    console.log('erro ao fazer loging' + response.status + ' ');
                    showErrorMsg($("#formLogin"), "warning", "Usuario ou senha incorretos!");
                });
            };

            var redirecionar = function(){
				location.href = 'pages/consulta.html';
            }

            var clicouEmLembrar = function(){
                if($document[0].getElementById('cbxRemember').checked){
                    console.log("salvando cache . . . ");

                    $window.localStorage['redCompany_WM3_username'] = $scope.username;
                }else{
                    console.log("limpando cache . . . ");
                    $window.localStorage['redCompany_WM3_username'] = "";
                }
            };
            

            var init = function() {
                var username = $window.localStorage['redCompany_WM3_username'];
				//alert(username);
				if(username != undefined){
                    if (username != "") {
                        $scope.username = username;
                        
                        $document[0].getElementById('cbxRemember').checked = "true";
					}
                }
                console.log('iniciado');
            };

            init();

            $scope.criarUsuario = function(){
                console.log('criando usuario . . .' + $scope.nome);                
                
                if($scope.senha == $scope.rpassword){
                    if(validateFormCriarUsuario()){
                        console.log(convertToJSON());
                        $('#kt_login_signup_submit').addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
                        $http({
                            method: 'POST',
                            url: urlRoot + '/api/usuario/novo',
                            data: convertToJSON(),
                            headers: {
                                'Content-Type': 'application/json'
                            },
                        }).then(function successCallback(response) {
                            console.log("criou usuario.");
                            $('#kt_login_signup_submit').removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);                        
                            showErrorMsg($("#form_criacaoUsuario"), "success", "Usuario criado!");
                            showErrorMsg($("#formLogin"), "success", "Usuario criado!");
                            displaySignInForm();
                        }, function errorCallback(response) {
                            console.log('Erro ao criar usuario');
                            $('#kt_login_signup_submit').removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                            showErrorMsg($("#form_criacaoUsuario"), "warning", "Erro ao criar usuario!");
                        });

                    }
                }else{
                    showErrorMsg($("#form_criacaoUsuario"), "warning", "Senhas estão diferentes!");
                }

            };

            var convertToJSON = function(){
                var data = {};
                
                data.id = '';
                data.nome = $scope.nome;
                data.email = $scope.email;
                data.razaoSocial = $scope.razaoSocial;
                data.perfil = 'USUARIO';
                data.telefone = $scope.telefoneFixo;
                data.senha = $scope.senha;
              
                return JSON.stringify(data);
            }

            $scope.alterarSenha = function(){
                console.log("alterando de senha . . . ");
                
                if(validateFormAlteraSenha()){
                    $('#kt_login_forgot_submit').addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
                    $http({
                        method: 'POST',
                        url: urlRoot + '/seguranca/senha/esqueci',
                        data: $scope.username,
                    }).then(function successCallback(response) {
                        console.log("trocou a senha.");
                        $('#kt_login_forgot_submit').removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);                        
                        showErrorMsg($("#form_alterasenha"), "success", "Email enviado para a troca de senha!");
                    }, function errorCallback(response) {
                        console.log('erro para alterar a senha');
                        $('#kt_login_forgot_submit').removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                        showErrorMsg($("#form_alterasenha"), "warning", "Erro ao enviar email!");
                    });

                }
            };
        });
    
    }

    // Public Functions
    return {
        // public functions
        init: function() {
            handleFormSwitch();
            //handleSignUpFormSubmit();
            //handleForgotFormSubmit();
            initiValidateForm();
            initAngular();
        }
    };
}();

// Class Initialization
jQuery(document).ready(function() {
    KTLoginGeneral.init();
});