import { showErrorDialog, showProgressDialog } from "./dialog.js";
import { anyStringNullOrEmpty, checkEmail } from "./validate.js";

let emailElement;
let forgotPasswordForm;

function checkAllFields() {
  const email = emailElement.value;
  if (anyStringNullOrEmpty([email])) {
    showErrorDialog("Email are required");
    return false;
  }
  if (!checkEmail(email)) {
    showErrorDialog("Invalid email!");
    return false;
  }
  return true;
}

function setforgotPasswordForm() {
  if (forgotPasswordForm === null) {
    return;
  }
  forgotPasswordForm.onsubmit = function (event) {
    event.preventDefault();
    if (checkAllFields()) {
      showProgressDialog("Please Wait...");
      forgotPasswordForm.submit();
    }
  };
}

function init() {
  emailElement = document.getElementById("email");
  forgotPasswordForm = document.getElementById("forgotPasswordForm");
  setforgotPasswordForm();
}

window.onload = init;
