$(document).ready(function () {
    $('#forgotPasswordForm').submit(function (e) {
        e.preventDefault();

        var email = $('#forgotEmail').val().trim();
        console.log(email);

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/forgot-password',
            type: 'post',
            data: {email: email},

            success: function (response) {
                Swal.fire({
                    title: 'Success!',
                    text: 'OTP sent to your email.',
                    icon: 'success',
                });
                console.log(email)
                $('#forgotPasswordForm').hide();
                $('#verifyOtpForm').show();
            },
            error: function (xhr, status, error) {
                Swal.fire({
                    title: 'Error!',
                    text: 'Something went wrong. Please try again.',
                    icon: 'error',
                });
                console.log(email + "email")
            }
        })
    })


    $('#verifyOtpForm').submit(function(e) {
        e.preventDefault();

        var email = $('#forgotEmail').val();
        var otp = $('#otp').val();

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/verify-otp',
            type: 'POST',
            data: { email: email, otp: otp },
            success: function(response) {
                Swal.fire({
                    title: 'Success!',
                    text: 'OTP verified.',
                    icon: 'success',
                });
                $('#verifyOtpForm').hide();
                $('#resetPasswordForm').show();
            },
            error: function(xhr, status, error) {
                Swal.fire({
                    title: 'Error!',
                    text: 'Invalid OTP. Please try again.',
                    icon: 'error',
                });
            }
        });
    });

    $('#resetPasswordForm').submit(function (e) {
        e.preventDefault();

        var email = $('#forgotEmail').val();
        var newPassword = $('#newPassword').val();

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/reset-password',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email, newPassword: newPassword }),            success: function (response) {
                Swal.fire({
                    title: 'Success!',
                    text: 'Password updated successfully!',
                    icon: 'success',
                });
                window.location.href = 'home.html';
            },
            error: function(xhr, status, error) {
                Swal.fire({
                    title: 'Error!',
                    text: 'Could not update the password. Please try again.',
                    icon: 'error',
                });
            }
        })
    })
})

