import { showConfirmDialog, showErrorDialog, showInfoDialog, showProgressDialog } from "./dialog.js";
import { anyStringNullOrEmpty } from "./validate.js";

let verifyFormElement;
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
      verifyFormElement.submit();
    }
  };
}

function setCancelButton() {
  $("#cancel").click(function (event) {
    event.preventDefault();
    showConfirmDialog("Are you sure you want to cancel the payment?", function () {
      window.location.href = "payments/cancel?transaction_number=${transaction.getTransactionNumber()}";
    });
  });
}

function setResendOtpButton() {
  $("#resendOtp").click(function () {
    $.ajax({
      url: "api/payments/ccdc/resendOtp",
      type: "POST",
      success: function (data) {
        showInfoDialog(data.message);
      },
    });
  });
}

function init() {
  verifyFormElement = document.getElementById("verifyForm");
  otpElement = document.getElementById("otp");
  submitButtonElement = document.getElementById("submitButton");
  setSubmitButton();
  setCancelButton();
  setResendOtpButton();
}

window.onload = init;
