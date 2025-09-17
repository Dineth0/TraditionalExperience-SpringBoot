$(document).ready(function () {
    let token = localStorage.getItem("authtoken");
    if (!token) {
        Swal.fire({
            icon: "error",
            title: "Not Authenticated",
            showConfirmButton: false,
            timer: 2000
        })
    }
    $.ajax({
        url:`http://localhost:8080/api/v1/user/getByEmail`,
        method: "GET",
        headers:{
            Authorization: `Bearer ` + token
        },
        success: function (data) {
            const fullName = data.username;
            const letter = fullName.charAt(0).toUpperCase()

            $('#userIcon').text(letter);
            $('#userName').text(fullName);
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
        }
    })
})