
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
                    <a href="javascript:;" name="' + index + '" class="btn btn-sm btn-clean btn-icon btn-icon-md btnPlano"  title="Planos">\
                        <i class="la la-puzzle-piece"></i>\
                    </a>\
                    <a href="javascript:;" name="' + index + '" class="btn btn-sm btn-clean btn-icon btn-icon-md btnDeleteTable"  title="Deletar">\
                        <i class="la la-trash"></i>\
                    </a>\
                    ';
                },
            }],

    });

    $('#kt_form_status').on('change', function() {
        datatable.search($(this).val().toLowerCase(), 'nome');
    });

    

    //$('#kt_form_type').on('change', function() {
    //datatable.search($(this).val().toLowerCase(), 'Type');
    //});

    //$('#kt_form_status,#kt_form_type').selectpicker();

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

    var getUsuarios = function(){
        console.log('Buscando usuarios . . .');

        $http({
            method: 'GET',
            url: urlRoot + '/api/usuario/todos',
            headers: {'Content-Type': 'application/json;'},
            data: {}
        }).then(function successCallback(response) {
            console.log("Buscou usuarios.");
            $scope.usuarios = response.data;
        }, function errorCallback(response) {
            console.log('Erro ao buscar usuarios');
        });
    
    };

    var save = function(dado, methodType) {
        console.log("salvando " + className + " . . . ");
        console.log(urlRoot + '/api/'+ className + ' METHOD: ' + methodType);
        var uri = '/adicionar';
        if(methodType == 'PUT'){
            uri = '/alterar';
			console.log('PUT   ' + '/api/' + className + uri)
        }
        $http({
            method: methodType,
            url: urlRoot + '/api/' + className + uri,
            headers: {
                'Content-Type': 'application/json'
            },
            data: dado
        }).then(function successCallback(response) {
            //alert(response.data);
            console.log(className + ' salva');
            swal.fire($scope.className + " salvo!", "", "success");
            $scope.limparCampos();
            rows = [];
            datatable.reload();
            $('#ModalAdd').modal('hide');
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
                    title: 'Deseja alterar a ' + className + ' ?',
                    text: "",
                    type: 'warning',
                    showCancelButton: true,
                    cancelButtonText: 'Não!',
                    confirmButtonText: 'Sim!',
                    reverseButtons: true
                }).then(function(result) {
                    if (result.value) {
                        var dado = parseForm($('#frmModelAdd'));
                        console.log(dado);
                        save(dado, 'PUT');

                    }else{
                        return;
                    }
                });
            }else{
                var dado = parseForm($('#frmModelAdd'));
                console.log(dado);
                save(dado, 'POST');
            }
        }else{
            console.log("inValido");
        }
    };

    var excluir = function(index){
        console.log('Deletando ' + className + ' . . . ' + rows[index].nome);

        swal.fire({
            title: 'Deseja excluir a '+ className +' \n' + rows[index].nome + '?',
            text: "",
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Sim!',
            cancelButtonText: 'Não!',
            reverseButtons: true
        }).then(function(result){
            if (result.value) {
                $http({
                    method: 'DELETE',
                    url: urlRoot + '/api/' + className + '/deletar/' + rows[index].id,
                    headers: {'Content-Type': 'application/json;'},
            data: {}
                }).then(function successCallback(response) {
                    //alert(response.data);
                    console.log(className + ' excluida');
                    swal.fire('Excluido!', 'A '+ className +' foi excluida.','success');
                    $scope.limparCampos();
                    rows = [];
                    datatable.reload();
                }, function errorCallback(response) {
                    console.log(response.status);
                });
            } 
        });
    } 

    var editar = function(index){//TODO
        var obj = rows[index];
        
        for(var prop in obj){
            if($("#frmModelAdd input[name="+prop+"]")[0] != undefined)
                $("#frmModelAdd input[name="+prop+"]")[0].value = obj[prop];
        }
        document.frmModelAdd.id.value = rows[index].id;
        /*document.frmModelAdd.nome.value = rows[index].nome;
        document.frmModelAdd.email.value = rows[index].email;
        document.frmModelAdd.razaoSocial.value = rows[index].razaoSocial;
        document.frmModelAdd.telefone.value = rows[index].telefone;*/
    }

    
    
    $scope.limparCampos = function() {
        console.log('limpando campos');
        
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
		data.id = document.frmModelAdd.id.value;
        data.perfil = 'USUARIO';
        return JSON.stringify(data);
    }
    

    var init = function() {
        console.log('iniciando . . ');
        getNewAccessToken();
        getUsuarios();
        initTable();

        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");

        

        datatable.on('kt-datatable--on-layout-updated', function(event){
            $(".btnSetTable").on("click", function(event){
                console.log("clicouuu " + this.name);
                editar(this.name);
            });

            $(".btnDeleteTable").on("click", function(event){
                console.log("clicouuu " + this.name);
                excluir(this.name);
            });

            $(".btnPlano").on("click", function(event){
                console.log("clicouuu " + this.name);
                console.log(rows[this.name])
                location.href = "/pages/meuPlano.html?id=" + rows[this.name].id
            });
            
            
        });

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