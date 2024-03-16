let uploadPictureElement = document.getElementById("uploadPicture");
let removePictureElement = document.getElementById("removePicture");
let xhr = new XMLHttpRequest();
function uploadPicture() {
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
    if (confirm("Are you sure you want to upload this picture?")) {
        formData.append("uploadPicture", uploadPictureElement.files[0]);
        xhr.open("POST", "pages/profile/uploadPicture", true);
        xhr.send(formData);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                location.reload();
            }
        };
    }
}
function removePicture() {
    if (confirm("Are you sure you want to remove your profile picture?")) {
        xhr.open("POST", "pages/profile/removePicture", true);
        xhr.send();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                location.reload();
            }
        };
    }
}
function init() {
    if (uploadPictureElement) {
        uploadPictureElement.addEventListener("change", uploadPicture);
    }
    if (removePictureElement) {
        removePictureElement.addEventListener("click", removePicture);
    }
}
window.onload = init;
