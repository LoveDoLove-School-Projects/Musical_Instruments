import { showConfirmDialog, showConfirmWithImageDialog } from "./dialog.js";

let uploadPictureElement;
let removePictureElement;
let updateProfileElement;

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
function init() {
  uploadPictureElement = document.getElementById("uploadPicture");
  removePictureElement = document.getElementById("removePicture");
  updateProfileElement = document.getElementById("updateProfile");
  setUploadPicture();
  setRemovePicture();
}
window.onload = init;
