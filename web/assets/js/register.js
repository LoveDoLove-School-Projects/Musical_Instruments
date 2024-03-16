function checkAllFields() {
    let username = document.getElementById("username").value;
    let address = document.getElementById("address").value;
    let password = document.getElementById("password").value;
    let confirm_password = document.getElementById("confirm_password").value;
    let email = document.getElementById("email").value;
    let phone_number = document.getElementById("phone_number").value;
    let otp = document.getElementById("otp").value;
    if (username === "" || address === "" || password === "" || confirm_password === "" || email === "" || phone_number === "" || otp === "") {
        alert("Please fill all the fields");
        return false;
    }
    if (password !== confirm_password) {
        alert("Password and Confirm Password should be same");
        return false;
    }
    if (!/^\d+$/.test(phone_number)) {
        alert("Phone number should contain only digits");
        return false;
    }
    return true;
}
function sendOtp() {
    let email = document.getElementById("email").value;
    if (email === "") {
        alert("Please enter email");
        return;
    }
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "services/sendOtp", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            try {
                let response = JSON.parse(xhr.responseText);
                if (response.status === "success") {
                    alert("OTP sent successfully");
                } else {
                    alert("Failed to send OTP");
                }
            } catch (e) {
                alert("Failed to send OTP");
            }
        }
    };
    xhr.send("email=" + email);
}
function sendRegistrationForm() {
    if (!checkAllFields()) {
        return;
    }
    document.getElementById("submit").submit();
}
function init() {
    document.getElementById("otpButton").addEventListener("click", sendOtp);
    document.getElementById("submit").addEventListener("click", sendRegistrationForm);
}
window.onload = init;