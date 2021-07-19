(function () {
    'use strict';

    $(function () {
        var app = new Vue({
            el: '#vuediv',
            data: {
                product: []
            },
            computed: {
                editLink: function() {
                    return "editProduct?productCode=";
                }
            }
        })

        $.get("maintenanceJSON")
            .done(function (product) {
                //app.products is the global products in data
                app.product = product;
            });

        $('body').on('click', '.delete-product-link', function (event) {
            // Don't do normal form submission
            event.preventDefault();

            // Get productCode from row
            var row = $(event.target).closest("tr");
            var productCode = row.attr("productCode");
            var confirmed = window.confirm(
                "Are you sure you want to delete product " + productCode + "?");

            if (!confirmed) {
                return;
            }

            $.post("deleteProduct", {
                // parameter: variable --> parameter name use in getParameter() in servlet, variable is variable called in this
                productCode: productCode
            })
            .done(function () {
                app.product = app.product.filter(prod => prod.code != productCode);
            })
            .fail(function (err) {
                console.log("Error", err)
            });
        });

    });
}());
