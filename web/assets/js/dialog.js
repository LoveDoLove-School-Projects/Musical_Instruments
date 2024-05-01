export {
showInfoDialog,
        showWarningDialog,
        showErrorDialog,
        showSuccessDialog,
        showProgressDialog,
        showConfirmDialog,
        showConfirmWithImageDialog,
        showEnterTextDialog,
        dismissDialog,
};

function showInfoDialog(text) {
    Swal.fire({
        title: "Info",
        text: text,
        icon: "info",
    });
}

function showWarningDialog(text) {
    Swal.fire({
        title: "Warning",
        text: text,
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes",
        cancelButtonText: "No",
    });
}

function showErrorDialog(text) {
    Swal.fire({
        title: "Error",
        text: text,
        icon: "error",
    });
}

function showSuccessDialog(text) {
    Swal.fire({
        title: "Success",
        text: text,
        icon: "success",
    });
}

function showProgressDialog(text) {
    Swal.fire({
        title: "Please wait",
        text: text,
        icon: "info",
        showConfirmButton: false,
    });
}

function showConfirmDialog(text, action) {
    Swal.fire({
        title: "Are you sure?",
        text: text,
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes",
        cancelButtonText: "No",
    }).then((result) => {
        if (result.isConfirmed) {
            action();
        }
    });
}

function showConfirmWithImageDialog(text, imageUrl, imageWidth, imageHeight, action) {
    Swal.fire({
        title: "Are you sure?",
        text: text,
        imageUrl: imageUrl,
        imageWidth: imageWidth,
        imageHeight: imageHeight,
        showCancelButton: true,
        confirmButtonText: "Yes",
        cancelButtonText: "No",
    }).then((result) => {
        if (result.isConfirmed) {
            action();
        }
    });
}

function showEnterTextDialog(text) {
    let input = "";
    Swal.fire({
        title: "Enter text",
        text: text,
        icon: "info",
        input: "text",
        showCancelButton: true,
        confirmButtonText: "Yes",
        cancelButtonText: "No",
    }).then((result) => {
        if (result.isConfirmed) {
            input = result.value;
        }
    });
    return input;
}

function dismissDialog() {
    Swal.close();
}
