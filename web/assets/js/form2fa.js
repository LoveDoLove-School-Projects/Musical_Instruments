import { showErrorDialog, showProgressDialog } from "./dialog.js";
import { anyStringNullOrEmpty } from "./validate.js";

let otpFormElement;
let otpElement;
let submitButtonElement;

function checkAllFields() {
    const otp = otpElement.value;
    if (anyStringNullOrEmpty([otp])) {
        showErrorDialog("Please fill in all fields.");
        return false;
    }
    if (otp.length !== 6) {
        showErrorDialog("OTP must be 6 characters long.");
        return false;
    }
    if (!/^\d+$/.test(otp)) {
        showErrorDialog("OTP must contain only digits.");
        return false;
    }
    return true;
}

function setSubmitButton() {
    if (submitButtonElement === null) {
        return;
    }
    submitButtonElement.onclick = function () {
        if (checkAllFields()) {
            showProgressDialog("Verifying OTP...");
            otpFormElement.submit();
        }
    };
}

function init() {
    otpFormElement = document.getElementById("otpForm");
    otpElement = document.getElementById("otp");
    submitButtonElement = document.getElementById("submitButton");
    setSubmitButton();
}
window.onload = init;
