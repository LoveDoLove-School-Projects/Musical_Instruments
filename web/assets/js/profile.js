import { showConfirmDialog, showConfirmWithImageDialog } from "./dialog.js";
import { anyStringNullOrEmpty } from "./validate.js";

let usernameElement;
let addressElement;
let phone_numberElement;
let uploadPictureElement;
let removePictureElement;
let updateProfileForm;

let xhr = new XMLHttpRequest();
function setUploadPicture() {
  if (uploadPictureElement === null) {
    return;
  }
  uploadPictureElement.addEventListener("change", () => {
    let formData = new FormData();
    let files = uploadPictureElement.files;
    let acceptAllImageFileType = "image/*";
    let fileSize = 5 * 1024 * 1024;
    if (files.length === 0) {
      alert("Please select a file to upload.");
      return;
    }
    if (!files[0].type.match(acceptAllImageFileType)) {
      alert("Invalid file type. Please select an image file.");
      return;
    }
    if (files[0].size > fileSize) {
      alert("The file size is too large. Please select a file that is less than 5MB.");
      return;
    }

    let action = () => {
      formData.append("uploadPicture", files[0]);
      xhr.open("POST", "pages/profile/uploadPicture", true);
      xhr.send(formData);
      xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
          location.reload();
        }
      };
    };
    showConfirmWithImageDialog("Are you sure to upload this picture?", URL.createObjectURL(files[0]), 400, 400, action);
  });
}
function setRemovePicture() {
  if (removePictureElement === null) {
    return;
  }
  removePictureElement.addEventListener("click", () => {
    let action = () => {
      xhr.open("POST", "pages/profile/removePicture", true);
      xhr.send();
      xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
          location.reload();
        }
      };
    };
    showConfirmDialog("Are you sure you want to remove your profile picture?", action);
  });
}
function checkAllFields() {
  const username = usernameElement.value;
  const address = addressElement.value;
  const phone_number = phone_numberElement.value;
  if (anyStringNullOrEmpty([username, address, phone_number])) {
    alert("Please fill in all fields.");
    return false;
  }
  return true;
}
function setUpdateProfileForm() {
  if (updateProfileForm === null) {
    return;
  }
  updateProfileForm.onsubmit = function (event) {
    event.preventDefault();
    let action = () => {
      if (checkAllFields()) {
        updateProfileForm.submit();
      }
    };
    showConfirmDialog("Are you sure to update your profile?", action);
  };
}
function init() {
  usernameElement = document.getElementById("username");
  addressElement = document.getElementById("address");
  phone_numberElement = document.getElementById("phone_number");
  uploadPictureElement = document.getElementById("uploadPicture");
  removePictureElement = document.getElementById("removePicture");
  updateProfileForm = document.getElementById("updateProfileForm");
  setUploadPicture();
  setRemovePicture();
  setUpdateProfileForm();
}
window.onload = init;
