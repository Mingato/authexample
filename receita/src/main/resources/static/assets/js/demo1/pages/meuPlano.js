
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

function getUrlParameter(sParam) {
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

var datatable;
var urlRoot = '';
var rows = [];
var className = 'usuario';
var initTable = function () {

    datatable = $('.kt-datatable').KTDatatable({
        // datasource definition
        data: {
            type: 'remote',
            source:  urlRoot + '/api/usuario/todos',
            pageSize: 10,
        },

        // layout definition
        layout: {
            scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
            footer: false // display/hide footer
        },

        // column sorting
        sortable: true,

        pagination: true,

        search: {
            input: $('#tableGeneralSearch')
        },

        // columns definition
        columns: [
            {
                field: "id",
                title: "ID",
                width: 0,
                sortable: false,
                visible: false,
            },
            {
                field: "nome",
                title: "Nome",
                //width: 150,
            },
            {
                field: "email",
                title: "Email",
                //width: 150,
            },
            {
                field: "perfil",
                title: "Perfil",
                //width: 150,
            },
            {
                field: 'Actions',
                title: 'Ações',
                sortable: false,
                width: 70,
                autoHide: false,
                overflow: 'visible',
                locked: {right: 'xl'},
                template: function(row, index) {
                    rows.push(row);
                    console.log(row.id + " '" + row.nome + "'");
                    return '\
                    <a href="javascript:;" name="' + index + '" class="btn btn-sm btn-clean btn-icon btn-icon-md btnSetTable"  title="Alterar" data-toggle="modal" data-target="#ModalAdd" data-backdrop="static">\
                        <i class="la la-edit"></i>\
                    </a>\
                    ';
                },
            }],

    });

    $('#kt_form_status').on('change', function() {
        datatable.search($(this).val().toLowerCase(), 'nome');
    });

};

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

    $scope.plano = {};

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

    var pegarInformacoesDoUsuario = function(){
        $http({
            method: 'POST',
            url: urlRoot + '/api/usuario/infoPessoais',
            headers: {'Content-Type': 'application/json;'},
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

    var getPlano = function(id){
        //console.log('Buscando plano . . .');
        uri = '/'+id;
        if(id == undefined || id == ''){
            uri = '';
            $("#frmModelAdd input[name=id]")[0].value = $scope.usuarioLogado.id;
            $scope.usuario.nome = $scope.usuarioLogado.nome;
        }else{
            $("#frmModelAdd input[name=id]")[0].value = id;
        }

        $http({
            method: 'GET',
            url: urlRoot + '/api/usuario/planos' + uri,
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            //console.log("Buscou plano.", response.data);
            $scope.plano = response.data;
        }, function errorCallback(response) {
            console.log('Erro ao buscar plano');
        });
    
    };

    var save = function(dado, methodType) {
        console.log("salvando " + className + " . . . ");
        console.log(urlRoot + '/api/'+ className + ' METHOD: ' + methodType);
        
        $http({
            method: 'PUT',
            url: urlRoot + '/api/usuario/planos/'+ dado.id,
            headers: {
                'Content-Type': 'application/json'
            },
            data: dado
        }).then(function successCallback(response) {
            //alert(response.data);
            console.log(className + ' salva');
            swal.fire("Plano alterado!", "", "success");
            //$scope.limparCampos();
            //rows = [];
            //datatable.reload();
        }, function errorCallback(response) {
            console.log(response.status);
            if(response.status == 409){
                $scope.mensagem = $scope.className +' com este nome já existe'
                $('#frmModelAdd_msg').css("display", "none")
                $('#frmModelAdd_msg').css("display", "block")
            }else if(response.data.error == "invalid_token"){
                getNewAccessToken();
                save(dado, methodType);
            }
        });
    };

    $scope.salvar = function(){
        console.log('btnSave clicked');
        $scope.validarCamposESalvar();
    };

    $scope.validarCamposESalvar = function(){
        console.log('validarCamposESalvar');
        if($('#frmModelAdd').valid()){
            console.log("valido");
            if(document.frmModelAdd.id.value != ""){//alterar
                swal.fire({
                    title: 'Deseja alterar o plano?',
                    text: "",
                    type: 'warning',
                    showCancelButton: true,
                    cancelButtonText: 'Não!',
                    confirmButtonText: 'Sim!',
                    reverseButtons: true
                }).then(function(result) {
                    if (result.value) {
                        console.log($scope.plano);

                        save($scope.plano, 'PUT');

                    }else{
                        return;
                    }
                });
            }else{
                swal.fire("Selecione um usuário!", "", "warning");
                
                /* var dado = parseForm($('#frmModelAdd'));
                console.log(dado);
                save(dado, 'POST'); */
            }
        }else{
            console.log("inValido");
        }
    };

    $scope.atualizarCreditos = function(idUsuario){

        swal.fire({
            title: 'Deseja renovar os créditos?',
            text: "",
            type: 'warning',
            showCancelButton: true,
            cancelButtonText: 'Não!',
            confirmButtonText: 'Sim!',
            reverseButtons: true
        }).then(function(result) {
            if (result.value) {
                $http({
                    method: 'PUT',
                    url: urlRoot + '/api/usuario/planos/renovar/'+ idUsuario,
                }).then(function successCallback(response) {
                    alert(response.data);
                    console.log(className + ' salva');
                    swal.fire("Créditos renovados!", "", "success");
                }, function errorCallback(response) {
                    console.log(response.status);
                });

            }else{
                return;
            }
        });
        
    }


    var editar = function(index){//TODO
        var obj = rows[index];
        
        for(var prop in obj){
            if($("#frmModelAdd input[name="+prop+"]")[0] != undefined)
                $("#frmModelAdd input[name="+prop+"]")[0].value = obj[prop];
        }
        /*document.frmModelAdd.id.value = rows[index].id;
        document.frmModelAdd.nome.value = rows[index].nome;
        document.frmModelAdd.email.value = rows[index].email;
        document.frmModelAdd.razaoSocial.value = rows[index].razaoSocial;
        document.frmModelAdd.telefone.value = rows[index].telefone;*/
    }

    
    
    $scope.limparCampos = function() {
        console.log('limpando campos');
        
        $("#frmModelAdd input[name=id]")[0].value = '';

        $.each($('#frmModelAdd').serializeArray(), function(i, field) {
            if($("#frmModelAdd input[name="+field.name+"]")[0] != undefined)
                $("#frmModelAdd input[name="+field.name+"]")[0].value = '';
        });
    }

  
    var parseForm = function(form){
        var serialized = form.serializeArray();
        var s = '';
        var data = {};
        for(s in serialized){
            data[serialized[s]['name']] = serialized[s]['value']
        }
        data.perfil = 'USUARIO';
        return JSON.stringify(data);
    }
    

    var init = function() {
        console.log('iniciando . . ');
        getNewAccessToken();
        pegarInformacoesDoUsuario()
        initTable();

        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");

        
        datatable.on('kt-datatable--on-layout-updated', function(event){
            $(".btnSetTable").on("click", function(event){
                console.log("clicouuu " + this.name);
                //editar(this.name);
                getPlano(rows[this.name].id);
                $scope.usuario.nome = rows[this.name].nome;
            });
            
        });

        getPlano(getUrlParameter('id'));
    };

    init();

    $window.setInterval( function pegarToken(){ getNewAccessToken(); }, 300000);
});



// Class Definition
var KTLoginGeneral = function() {

    
    var validateForm = function(){
        console.log('validando form');
        if($("#form_adicionarUsuario").valid()){
            console.log("campos validos. ");
            return true;
        }
        return false;
    }
    
    

    var initiValidateForm = function() {
    
        $("#frmModelAdd").validate({
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
            return validateForm();
        }
    };
}();

// Class Initialization
jQuery(document).ready(function() {
    KTLoginGeneral.init();
});