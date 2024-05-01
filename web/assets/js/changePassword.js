import { showErrorDialog, showProgressDialog } from "./dialog.js";
import { anyStringNullOrEmpty, checkPassword } from "./validate.js";

let currentPasswordElement;
let newPassowrdElement;
let confirmNewPassowrdElement;
let changePasswordForm;

function checkAllFields() {
    const currentPassword = currentPasswordElement.value;
    const newPassword = newPassowrdElement.value;
    const confirmNewPassword = confirmNewPassowrdElement.value;
    if (anyStringNullOrEmpty([currentPassword, newPassword, confirmNewPassword])) {
        showErrorDialog("All fields are required");
        return false;
    }
    if (newPassword !== confirmNewPassword) {
        showErrorDialog("New Password and Confirm New Password should be same");
        return false;
    }
    if (!checkPassword(currentPassword) || !checkPassword(newPassword) || !checkPassword(confirmNewPassword)) {
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
    currentPasswordElement = document.getElementById("currentPassword");
    newPassowrdElement = document.getElementById("newPassword");
    confirmNewPassowrdElement = document.getElementById("confirmNewPassword");
    changePasswordForm = document.getElementById("changePasswordForm");
    setChangePasswordForm();
}

window.onload = init;
