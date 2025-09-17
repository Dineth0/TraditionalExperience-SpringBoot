$(document).ready(function () {
    loadTotalUserCount();
    loadTotalWorkshopCount();
    loadWorkshopWiseBookings();
    function loadTotalUserCount() {
        let token = localStorage.getItem('authtoken');
        $.ajax({
            url: `http://localhost:8080/api/v1/user/total-count`,
            method: "GET",
            headers: {
                "Authorization": 'Bearer ' + token
            },
            success: function (res) {
                $("#totalUsers").text(res.data);
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
            }
        })
    }

    function loadTotalWorkshopCount() {
        let token = localStorage.getItem('authtoken');
        $.ajax({
            url: `http://localhost:8080/api/v1/workshop/total-count`,
            method: "GET",
            headers: {
                "Authorization": 'Bearer ' + token
            },
            success: function (res) {
                $("#workshopCount").text(res.data);
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
            }
        })
    }


    function loadWorkshopWiseBookings() {
        let token = localStorage.getItem('authtoken');
        $.ajax({
            url: `http://localhost:8080/api/v1/workshopRegistration/summary/workshop-wise`,
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (data) {
                const titles = data.map(item => item.title);
                const counts = data.map(item => item.totalBookings);

                const all = document.getElementById('workshopBookingsChart').getContext('2d');

                new Chart(all, {
                    type: 'bar',
                    data: {
                        labels: titles,
                        datasets: [{
                            label: 'Total Bookings',
                            data: counts,
                            backgroundColor: 'rgba(105,52,9,0.2)',
                        }]
                    },
                    options: {
                        indexAxis: 'y',
                        responsive: true,
                        scales: {
                            x:{
                                beginAtZero: true,
                                title:{display: true, text:'Booking'},
                            },
                            y:{
                                title:{display: true, text:'Workshop title'},
                            }
                        },
                        plugins: {
                            legend:{display: false},
                            tooltips: {enabled: true},
                        }
                    }
                })
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
            }
        })
    }
})