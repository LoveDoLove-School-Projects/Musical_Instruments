import { showErrorDialog, showProgressDialog } from "./dialog.js";
import { anyStringNullOrEmpty, checkEmail, checkPassword } from "./validate.js";

let emailElement;
let newPassowrdElement;
let confirmNewPassowrdElement;
let changePasswordForm;

function checkAllFields() {
    const email = emailElement.value;
    const newPassword = newPassowrdElement.value;
    const confirmNewPassword = confirmNewPassowrdElement.value;
    if (anyStringNullOrEmpty([email, newPassword, confirmNewPassword])) {
        showErrorDialog("All fields are required");
        return false;
    }
    if (!checkEmail(email)) {
        showErrorDialog("Invalid email!");
        return false;
    }
    if (newPassword !== confirmNewPassword) {
        showErrorDialog("New Password and Confirm New Password should be same");
        return false;
    }
    if (!checkPassword(newPassword)) {
        showErrorDialog("New Password should be at least 8 characters long");
        return false;
    }
    return true;
}

function setChangePasswordForm() {
    if (changePasswordForm === null) {
        return;
    }
    changePasswordForm.onsubmit = function (event) {
        event.preventDefault();
        if (checkAllFields()) {
            showProgressDialog("Please Wait...");
            changePasswordForm.submit();
        }
    };
}

function init() {
    emailElement = document.getElementById("email");
    newPassowrdElement = document.getElementById("newPassword");
    confirmNewPassowrdElement = document.getElementById("confirmNewPassword");
    changePasswordForm = document.getElementById("changePasswordForm");
    setChangePasswordForm();
}

window.onload = init;
