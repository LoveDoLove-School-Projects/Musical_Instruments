import { showConfirmDialog, showErrorDialog, showProgressDialog } from "./dialog.js";

 var buttonPlus = $(".qty-btn-plus");
            var buttonMinus = $(".qty-btn-minus");

            var incrementPlus = buttonPlus.click(function () {
                var $n = $(this)
                        .parent(".qty-container")
                        .find(".input-qty");
                $n.val(Number($n.val()) + 1);
            });

            var incrementMinus = buttonMinus.click(function () {
                var $n = $(this)
                        .parent(".qty-container")
                        .find(".input-qty");
                var amount = Number($n.val());
                if (amount > 0) {
                    $n.val(amount - 1);
                }
            });

            let logoutForm = document.getElementById("logoutForm");
            if (logoutForm != null) {
                logoutForm.addEventListener("submit", function (event) {
                    event.preventDefault(); // Prevent the default form submission behavior

                    let action = () => {
                        // Execute the form submission if the user confirms
                        logoutForm.submit();
                    };

                    showConfirmDialog("Are you sure you want to logout?", action);
                });
            }