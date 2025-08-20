$(document).ready(function(){
    let params = new URLSearchParams(window.location.search);
    loadInstructorsDropDown();
    loadItemsDropDown();
    loadWorkshopsForTable();
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
    $('#addTimeButton').on('click', function(){
        $('#timeSection').append(' <input class="form-control time-input" type="time" name="fee" id="time" required>')
    });

    $('#workshopForm').on('submit', function(e){
        e.preventDefault();


        let token = localStorage.getItem("authtoken");

        let times = [];
        $('.time-input').each(function(){
            if($(this).val()){
                times.push($(this).val());
            }
        });

        let workshop ={
            title: $('#workshopTitle').val(),
            description: $('#description').val(),
            location: $('#location').val(),
            duration: $('#duration').val(),
            language: $('#language').val(),
            participantCount: $('#participantCount').val(),
            fee: $('#fees').val(),
            address: $('#address').val(),
            itemId:$('#itemSelect').val(),
            instructorId:$('#instructorSelect').val(),
            time:times

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
    function loadWorkshopsForTable(){
        let token = localStorage.getItem("authtoken");
        $.ajax({
            url:'http://localhost:8080/api/v1/workshop/getAllWorkshops',
            method:'GET',
            headers: token ? { 'Authorization': 'Bearer ' + token } : {},
            success: function (response) {
                let workshops = response.data;
                // let container = $(".card-container");
                let tbody = $('.workshop-tbody');

                tbody.empty();
                // container.empty()

                // if(!workshops || workshops.length === 0) {
                //     container.html('<p>No items added yet</p>');
                //     return;
                // }
                workshops.forEach(workshops => {
                    let workshopTitle = workshops.title;
                    let workshopDescription = workshops.description;
                    let location = workshops.location;
                    let duration = workshops.duration;
                    let language = workshops.language;
                    let participantCount = workshops.participantCount;
                    let fee = workshops.fee;
                    let address = workshops.address;
                    let times= workshops.time;
                    let imagePaths = workshops.image || []

                    let imagesHtml = imagePaths.length > 0
                        ? imagePaths.map(img => `<img src="http://localhost:8080/uploads/${img}" alt="Item Image" width="80" style="margin-right: 5px;">`).join('')
                        : 'No Images Found';

                    let row = `
                    <tr class="workshop-row" 
                        data-workshop-title="${workshopTitle}"
                        data-description="${workshopDescription}"
                        data-location="${location}"
                        data-deration="${duration}"
                        data-language="${language}"
                        data-participantCount="${participantCount}"
                        data-fee="${fee}"
                        data-address="${address}"
                        data-time="${times}"
                        data-image="${imagePaths.join(';')}">
                        <td>${workshopTitle}</td>
                        <td>${workshopDescription}</td>
                        <td>${location}</td>
                        <td>${duration}</td>
                        <td>${language}</td>
                        <td>${participantCount}</td>
                        <td>${fee}</td>
                        <td>${address}</td>
                         <td>${times.join(', ')}</td>
                        <td>${imagesHtml}</td>
                        <td>
                            <button class="btn btn-sm" style="background-color:bisque" ">Edit</button>
                            <button class="btn btn-sm" style="background-color: cornflowerblue">Delete</button>
                        </td>
                    </tr>`;
                    tbody.append(row);
                })



            },
            error: function (xhr) {
                if (xhr.status === 204) {
                    $(".card-container").html('<p>No items added yet</p>');
                } else if (xhr.status === 403) {
                    Swal.fire({
                        icon: "error",
                        title: "Not Authorized",
                        text: "Please log in again",
                        showConfirmButton: false,
                        timer: 2000
                    }).then(() => {
                        localStorage.removeItem("authtoken");
                        window.location.href = "SignIn.html";
                    });
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Error Loading Instructors",
                        text: xhr.responseText || "Please try again"
                    });
                }
            }

        })
    }

})