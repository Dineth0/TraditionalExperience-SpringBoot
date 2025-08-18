$(document).ready(function(){
    loadInstructorsDropDown();
    loadItemsDropDown();
    let token = localStorage.getItem("authtoken");
    if (!token) {
        window.location.href = 'SignIn.html';
        return;
    }
    let payload = JSON.parse(atob(token.split('.')[1]));
    if(payload.role !== "ADMIN"){
        Swal.fire({
            icon: "Error",
            title: "Not Authenticated",
            showConfirmButton: false,
            timer: 2000
        })
        window.location.href = "home.html";
    }

    function loadItemsDropDown() {
        let token = localStorage.getItem("authtoken");
        $.ajax({
            url: 'http://localhost:8080/api/v1/item/getAllItems',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            },
            success: function (items) {
                let select = $('#itemSelect');
                select.empty();
                select.append('<option value="">Select Item</option>');
                items.forEach(item => {
                    select.append(`<option value="${item.id}">${item.itemName}</option>`);
                })
            }
        })
    }

    function loadInstructorsDropDown() {
        let token = localStorage.getItem("authtoken");
        $.ajax({
            url: 'http://localhost:8080/api/v1/instructor/getAllInstructors',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            },
            success: function (response) {
                let instructors = response.data; // correct response mapping

                let select = $('#instructorSelect');
                select.empty();
                select.append('<option value="">Select Instructor</option>');

                if (instructors && instructors.length > 0) {
                    instructors.forEach(instructor => {
                        select.append(`<option value="${instructor.id}">${instructor.instructorName}</option>`);
                    });
                }
            }
        })
    }

    $('#workshopForm').on('submit', function(e){
        e.preventDefault();

        let token = localStorage.getItem("authtoken");

        let workshop ={
            title: $('#workshopTitle').val(),
            description: $('#description').val(),
            location: $('#location').val(),
            duration: $('#duration').val(),
            language: $('#language').val(),
            participantCount: $('#participantCount').val(),
            fee: $('#fees').val(),
            itemId:$('#itemSelect').val(),
            instructorId:$('#instructorSelect').val()

        }
        let formData = new FormData();
        formData.append('workshop', new Blob([JSON.stringify(workshop)], {type: 'application/json'}));

        let files = $('#workshopImg')[0].files;
        for(let i = 0; i < files.length; i++) {
            formData.append('file', files[i]);
        }
        $.ajax({
            url:'http://localhost:8080/api/v1/workshop/addWorkshop',
            method:'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
            },
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                if(response.code === 201) {


                    Swal.fire({
                        icon: "success",
                        title: "Added Successful",
                        showConfirmButton: false,
                        timer: 2000
                    }).then(() => {
                        // loadItems();
                         window.location.href = "addWorkshop.html";
                    })
                }else if (response.code === 502) {
                    Swal.fire({
                        icon: "error",
                        title: response.message,
                        showConfirmButton: false,
                        timer: 2000
                    })
                }
            },
            error: function (xhr, status, error) {
                if(xhr.status === 403){
                    Swal.fire({
                        icon: "error",
                        title: "Not Authenticated",
                        showConfirmButton: false,
                        timer: 2000
                    })
                    console.log(status,error)
                }
            }
        })
    })
})