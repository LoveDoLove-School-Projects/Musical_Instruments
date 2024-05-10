import { showConfirmDialog, showErrorDialog } from "./dialog.js";
var buttonPlus = $(".qty-btn-plus");
var buttonMinus = $(".qty-btn-minus");
var incrementPlus = buttonPlus.click(function () {
  var $n = $(this).parent(".qty-container").find(".input-qty");
  $n.val(Number($n.val()) + 1);
});
var incrementMinus = buttonMinus.click(function () {
  var $n = $(this).parent(".qty-container").find(".input-qty");
  var amount = Number($n.val());
  if (amount > 0) {
    $n.val(amount - 1);
  }
});
let addToCartForm = document.getElementById("addToCartForm");

let editCartForm = document.getElementById("editCartForm");
function addToCartProductForm() {
  if (addToCartForm !== null) {
    addToCartForm.onsubmit = function (event) {
      event.preventDefault();
      Swal.fire({
        title: "Are you sure add this product?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes !",
      }).then((result) => {
        if (!result.isConfirmed) {
          event.preventDefault();
          return;
        }
        addToCartForm.submit();
      });
    };
  }
}
addToCartProductForm();

function editCartProductForm() {
  if (editCartForm !== null) {
    editCartForm.onsubmit = function (event) {
      event.preventDefault();
      Swal.fire({
        title: "Are you sure you want update ?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes !",
      }).then((result) => {
        if (!result.isConfirmed) {
          event.preventDefault();
          return;
        }
        editCartForm.submit();
      });
    };
  }
}
editCartProductForm();

function setRatingForm() {
  $("#submitButton").click(function (event) {
    event.preventDefault();
    if ($("#comment").val().length < 1) {
      return showErrorDialog("Please enter a comment!");
    }
    showConfirmDialog("You are about to submit your rating!", function () {
      $("#ratingForm").submit();
    });
  });
}
setRatingForm();
