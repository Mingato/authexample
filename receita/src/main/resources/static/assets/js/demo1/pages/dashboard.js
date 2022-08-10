"use strict";

// Class definition
var KTDashboard = function() {

    
    var init = function(){
        console.log('Bem-vindo!');

        $("#pageBody").css("display", "block");
        $("#pageLoader").css("display", "none");
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
});