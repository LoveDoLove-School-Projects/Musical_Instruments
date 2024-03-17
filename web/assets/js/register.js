import { showErrorDialog, showProgressDialog, showSuccessDialog } from "./dialog.js";
import { anyStringNullOrEmpty, checkEmail, checkPassword, checkPhoneNumber } from "./validate.js";

let usernameElement;
let addressElement;
let passwordElement;
let confirm_passwordElement;
let emailElement;
let phone_numberElement;
let otpElement;
let otpButtonElement;
let registerForm;

function checkAllFields() {
  const username = usernameElement.value;
  const address = addressElement.value;
  const password = passwordElement.value;
  const confirm_password = confirm_passwordElement.value;
  const email = emailElement.value;
  const phone_number = phone_numberElement.value;
  const otp = otpElement.value;
  if (anyStringNullOrEmpty([username, address, password, confirm_password, email, phone_number, otp])) {
    showErrorDialog("All fields are required");
    return false;
  }
  if (password !== confirm_password) {
    showErrorDialog("Password and Confirm Password should be same");
    return false;
  }
  if (!checkPassword(password)) {
    showErrorDialog("Password should be at least 8 characters long");
    return false;
  }
  if (!checkEmail(email)) {
    showErrorDialog("Invalid email!");
    return false;
  }
  if (!checkPhoneNumber(phone_number)) {
    showErrorDialog("Phone number should contain only digits");
    return false;
  }
  return true;
}

function setRegisterForm() {
  if (registerForm === null) {
    return;
  }
  registerForm.onsubmit = function (event) {
    event.preventDefault();
    if (checkAllFields()) {
      registerForm.submit();
    }
  };
}

function setSendOtp() {
  if (otpButtonElement === null) {
    return;
  }
  otpButtonElement.addEventListener("click", () => {
    const email = emailElement.value;
    if (anyStringNullOrEmpty([email])) {
      showErrorDialog("Email is required");
      return;
    }
    showProgressDialog("Sending OTP");
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "services/sendOtp", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
        try {
          let response = JSON.parse(xhr.responseText);
          if (response.status === "success") {
            showSuccessDialog("Success", "OTP sent successfully");
          } else {
            showErrorDialog("Error", "Failed to send OTP");
          }
        } catch (e) {
          showErrorDialog("Error", "Failed to send OTP");
        }
      }
    };
    xhr.send("email=" + email);
  });
}

function init() {
  usernameElement = document.getElementById("username");
  addressElement = document.getElementById("address");
  passwordElement = document.getElementById("password");
  confirm_passwordElement = document.getElementById("confirm_password");
  emailElement = document.getElementById("email");
  phone_numberElement = document.getElementById("phone_number");
  otpElement = document.getElementById("otp");
  otpButtonElement = document.getElementById("otpButton");
  registerForm = document.getElementById("registerForm");
  setRegisterForm();
  setSendOtp();
}

window.onload = init;
