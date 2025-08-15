$(document).ready(function () {
    $('#signupForm').submit(function (e) {
        e.preventDefault();

        var username = $('#registerUsername').val().trim();
        var email = $('#registerEmail').val().trim();
        var password = $('#registerPassword').val().trim();
        var confirmPassword = $("#confirmPassword").val().trim();

        if (!username || !email || !password) {
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
        if(confirmPassword !== password) {
            Swal.fire("Password Miss Match", "Please enter matching password", "error");
            return;
        }

        var userData = {
            username: username,
            email: email,
            password: password,
        };

        $.ajax({
            url: 'http://localhost:8080/api/v1/user/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function (response) {
                console.log("Response:", response);
                console.log("token", response.data);

                if (response.code === 201) {

                    const token = response.data.token;


                    localStorage.setItem("authtoken", token);


                    localStorage.setItem("jwt_debug", JSON.stringify(response));

                    console.log("Token stored: ", token);

                    Swal.fire({
                        icon: "success",
                        title: "Registration Successful",
                        text: "Redirecting...",
                        showConfirmButton: false,
                        timer: 2000
                    }).then(() => {

                            window.location.href = "SignIn.html";


                    });
                } else {
                    Swal.fire("Registration Failed", response.message, "error");
                }
            },
            error: function (xhr) {
                console.log("Error:", xhr.responseText);
                Swal.fire("Error!", "There was an error: " + xhr.responseText, "error");
            }
        });
    });

    function validateEmail(email) {
        let regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }
});