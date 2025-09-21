$(document).ready(function () {
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        let email = $("#loginEmail").val().trim();
        let password = $("#loginPassword").val().trim();

        if (!email || !password) {
            Swal.fire("Oops...", "Please fill all fields!", "warning");
            return;
        }
        if (!validateEmail(email)) {
            Swal.fire("Invalid Email", "Enter a valid email address.", "error");
            return;
        }
        if (password.length < 6) {
            Swal.fire("Weak Password", "Password must be at least 6 characters.", "error");
            return;
        }

        console.log("Sending:", { email, password });

        $.ajax({
            url: "http://localhost:8080/api/v1/auth/authenticate",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ email, password }),
            success: function (response) {

                if(response.code === 201 && response.data && response.data.token) {
                    localStorage.setItem("authtoken", response.data.token);

                    const decoded = parseJwt(response.data.token);
                    localStorage.setItem("userId", decoded.id);
                    sessionStorage.setItem("userId", decoded.id);

                    if (email === "sltraditionalportal@gmail.com") {
                        Swal.fire({
                            icon: "success",
                            title: "Login Successful",
                            text: "Redirecting to Admin Dashboard...",
                            showConfirmButton: false,
                            timer: 2000
                        }).then(() => {
                            window.location.href = "AdminDashboard.html"; // Redirect to admin dashboard
                        });
                    }else {
                        Swal.fire({
                            icon: "success",
                            title: "Login Successful",
                            text: "Redirecting...",
                            showConfirmButton: false,
                            timer: 2000
                        }).then(() => {
                            const token = response.data.token;
                            const decoded = parseJwt(token);
                            console.log("Decoded token:", decoded);

                            if (decoded && decoded.role) { // Change to 'role'
                                const role = decoded.role.toUpperCase(); // Use 'role' instead of 'accountType'

                                if (role === "USER") {
                                    window.location.href = "items.html";
                                }
                            } else {
                                console.log("role not found in token");

                            }
                        })
                    }
                }else {
                    Swal.fire("Login Failed", "Invalid email or password.", "error");
                }
            },
            error: function (xhr) {
                console.log("Error:", xhr.responseText);
                Swal.fire("Login Failed", "Check your email and password.", "error");
            }
        })

    })
    function validateEmail(email) {
        let regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }
})

function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("Invalid token:", e);
        return null;
    }
}

$(document).on('click', '#logout', function () {
    localStorage.removeItem("authtoken");
    sessionStorage.removeItem("authtoken")

    window.location.href= "home.html"
})

$(document).on('click', '.dropdown-item', function () {
    localStorage.removeItem("authtoken");
    sessionStorage.removeItem("authtoken")

    window.location.href= "home.html"
})