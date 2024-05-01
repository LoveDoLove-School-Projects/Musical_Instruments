import { showConfirmDialog } from "./dialog.js";
let navbar_toggler = document.querySelector(".navbar-toggler");
let navbar_collapse = document.querySelector(".navbar-collapse");
navbar_toggler.addEventListener("click", function () {
    navbar_collapse.classList.toggle("show");
});
// when the user clicks outside the navbar, the navbar will be hidden
document.addEventListener("click", function (e) {
    if (!navbar_collapse.contains(e.target) && !navbar_toggler.contains(e.target)) {
        navbar_collapse.classList.remove("show");
    }
});
let logoutBtn = document.getElementById("logoutBtn");
if (logoutBtn != null) {
    logoutBtn.addEventListener("click", function () {
        let action = () => {
            location.href = "pages/logout";
        };
        showConfirmDialog("Are you sure you want to logout?", action);
    });
}
