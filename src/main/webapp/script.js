// script Vue
var vm = new Vue({

    el: '#app',
    data:{

        isActive:true,
        formEmpty:false

    },

    methods: {

        showForm(){

            this.isActive = !this.isActive;

        }


    }

});

// script Ajax
function isEmpty( el ){
    return !$.trim(el.html())
}


$(document).on("click", "#logga", function() {

    var utente = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    //se un input del form è vuoto rendi visibile il messaggio di warning
    if (utente == "" || password == "" ){
        vm.formEmpty = true;
    }
    //se il form è compilato mandiamo una richiesta alla servlet che contatta il DAO per vedere se l'account ricevuto in input è valido
    else {
        vm.formEmpty = false;
        azione = "checkACC";

        $.post("ModificaServlet", {utente: utente, password: password, azione: azione}, function (data) {
            $("#app").html(data);
        },);

        var sessionValue= $("#hdnSession").data('value');
        console.log(session);
        var loginValido = $.session.get("loginValido");
        console.log(loginValido);
        //se l'account è valido procediamo con l'accesso alla pagina del cliente / amministratore
        if(loginValido == "cliente" || loginValido == "amministratore"){
            vm.formEmpty = false;
            azione = "login";
            $.post("ModificaServlet", {utente: utente, password: password, azione: azione},
                function (data) {
                    $("#app").html(data);
                },
            );
        }
        else{
            vm.formEmpty = true;
        }
    }

});


$(document).on("click", "#ripetizioni", function() {

    var azione = "showRipetizioni";

    $.post("ModificaServlet", {azione:azione},
        function (data) {
            $("#loggato").html(data);


        },


    );

});

//script Javascript
function clear()
{
    document.getElementById("loggato").innerHTML = "";
    document.getElementById("app").innerHTML = "";
}