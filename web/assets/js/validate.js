export { anyElementNullOrEmpty, anyStringNullOrEmpty, checkEmail, checkPhoneNumber, checkPassword };

function anyElementNullOrEmpty(elements) {
    return elements.some((element) => element.value === null || element.value === "");
}

function anyStringNullOrEmpty(values) {
    return values.some((value) => value === null || value === "");
}

function checkEmail(email) {
    return email.includes("@") && email.includes(".");
}

function checkPhoneNumber(phoneNumber) {
    return /^\d+$/.test(phoneNumber);
}

function checkPassword(password) {
    return password.length >= 8;
}
