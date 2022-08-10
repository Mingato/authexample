//== Class Definition
var SnippetLogin = function() {
    
    var urlRoot = '';

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

    var initiValidateForm = function() {

        $("#form_alterasenha").validate({
            rules: {
                password1: {
                    required: true,
                    
                },
                password2: {
                    required: true
                }
            }
        });
        
    }

    var getUrlParameter = function getUrlParameter(sParam) {
        var sPageURL = window.location.search.substring(1),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;
    
        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');
    
            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
            }
        }
    };

    var validateForm = function(){
        console.log('validando form');
        if($('#form_alterasenha').valid()){
            return true;
        }
        return false;
    }

    var initAngular = function(){
        var app = angular.module('appReceita', []);

        app.controller('bodyController', function($scope, $http, $window, $document){
            $scope.password1 = "";
            $scope.password2 = "";
            $scope.codigo = getUrlParameter('codigo');
                    
            $scope.alterar = function(){
                if(validateForm()){
                    if($scope.password1 == $scope.password2){
                        console.log("alterando senha . . ");
                        $("#btnAlterar").addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
                        $http({
                            method: 'PUT',
                            url: urlRoot + '/seguranca/senha/trocar/' + $scope.codigo,
                            data: $scope.password1,
                        }).then(function successCallback(response) {
                            console.log("trocou senha.");
                            showErrorMsg($('#form_alterasenha'), "success", "Senha alterada!");
                            $("#btnAlterar").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                            swal("Senha alterada!", "", "success");
                            windowwindow.open("/login2.html", "_self");
                        }, function errorCallback(response) {
                            console.log('erro ao alterar senha');
                            $("#btnAlterar").removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                            showErrorMsg($('#form_alterasenha'), "danger", "Não foi possível alterar a senha!");
                        });
                    }else{
                        console.log("senhas diferentes");
                        showErrorMsg($('#form_alterasenha'), "warning", "As senhas devem ser iguais!");
                    }
                }
            };

            $scope.saveCredencials = function(){
                console.log("salvando cache . . . ");
                
                //$window.localStorage['redCompany_WM3_password'] = $scope.password;
            };
        });
        
    }

    //== Public Functions
    return {
        // public functions
        init: function() {
            initiValidateForm();
            initAngular(); 
        }
    };
}();

//== Class Initialization
jQuery(document).ready(function() {
    SnippetLogin.init();

});