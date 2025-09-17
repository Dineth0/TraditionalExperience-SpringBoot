$(document).ready(function () {
    loadTotalUserCount();
    function loadTotalUserCount() {
        let token = localStorage.getItem('authtoken');
        $.ajax({
            url: `http://localhost:8080/api/v1/user/total-count`,
            method: "GET",
            headers: {
                "Authorization": 'Bearer ' + token
            },
            success: function (res) {
                $("#totalUsers").text(res);
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
            }
        })
    }
})