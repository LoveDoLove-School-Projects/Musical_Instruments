import { showErrorDialog } from "./dialog.js";
import { anyStringNullOrEmpty } from "./validate.js";

let emailElement;
let passwordElement;
let loginForm;

function checkAllFields() {
  const email = emailElement.value;
  const password = passwordElement.value;
  if (anyStringNullOrEmpty([email, password])) {
    showErrorDialog("Email and password are required");
    return false;
  }
  if (!checkEmail(email)) {
    showErrorDialog("Invalid email!");
    return false;
  }
  if (!checkPassword(password)) {
    showErrorDialog("Password should be at least 8 characters long");
    return false;
  }
  return true;
}

function setLoginForm() {
  if (loginForm === null) {
    return;
  }
  loginForm.onsubmit = function (event) {
    event.preventDefault();
    if (checkAllFields()) {
      loginForm.submit();
    }
  };
}

function init() {
  emailElement = document.getElementById("email");
  passwordElement = document.getElementById("password");
  loginForm = document.getElementById("loginForm");
  setLoginForm();
}

window.onload = init;
