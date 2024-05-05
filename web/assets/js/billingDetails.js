import { showConfirmDialog, showErrorDialog, showWarningDialog } from "./dialog.js";
import { anyStringNullOrEmpty } from "./validate.js";

let firstNameElement;
let lastNameElement;
let countryElement;
let addressElement;
let cityElement;
let stateElement;
let zipCodeElement;
let phoneElement;
let updateBillingDetailsForm;

function setUpdateBillingDetailsForm() {
  const updateBillingDetailsForm = document.getElementById("updateBillingDetailsForm");
  updateBillingDetailsForm.addEventListener("submit", function (event) {
    event.preventDefault();
    if (checkAllFields()) {
      showConfirmDialog("Are you sure you want to update your billing details?", function () {
        updateBillingDetailsForm.submit();
      });
    } else {
      showErrorDialog("Please fill in all fields.");
    }
  });
}
function checkAllFields() {
  const firstName = firstNameElement.value;
  const lastName = lastNameElement.value;
  const country = countryElement.value;
  const address = addressElement.value;
  const city = cityElement.value;
  const state = stateElement.value;
  const zipCode = zipCodeElement.value;
  const phone_number = phoneElement.value;
  if (anyStringNullOrEmpty([firstName, lastName, country, address, city, state, zipCode, phone_number])) {
    showWarningDialog("Please fill in all fields.");
    return false;
  }
  return true;
}
function init() {
  firstNameElement = document.getElementById("firstName");
  lastNameElement = document.getElementById("lastName");
  countryElement = document.getElementById("country");
  addressElement = document.getElementById("address");
  cityElement = document.getElementById("city");
  stateElement = document.getElementById("state");
  zipCodeElement = document.getElementById("zipCode");
  phoneElement = document.getElementById("phone_number");
  updateBillingDetailsForm = document.getElementById("updateBillingDetailsForm");
  setUpdateBillingDetailsForm();
}
window.onload = init;
