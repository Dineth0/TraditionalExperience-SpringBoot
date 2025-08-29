$(document).ready(function(){
    loadInstructors();
    let token = localStorage.getItem('authtoken');
    if (!token) {
        window.location.href = 'signIn.html';
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

    $('#instructorForm').on('submit', function(e){
        e.preventDefault();

        let token = localStorage.getItem("authtoken");
        let instructor ={
            instructorName: $('#instructorName').val(),
            age: $('#instructorAge').val(),
            category: $('#instructorCategory').val(),
            instructorEmail: $('#instructorEmail').val(),
            instructorPhone: $('#instructorPhone').val(),
        };
        let formData = new FormData();
        formData.append('instructor', new Blob([JSON.stringify(instructor)], {type: 'application/json'}));

        let files = $('#instructorImg')[0].files;
        for(let i = 0; i < files.length; i++) {
            formData.append('file', files[i]);
        }
        $.ajax({
            url: 'http://localhost:8080/api/v1/instructor/addInstructor',
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
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
                         loadInstructors();
                        window.location.href = "Instructors.html";
                    })
                }else if(response.code === 406) {
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

    function loadInstructors() {
        let token = localStorage.getItem("authtoken");
        $.ajax({
            url: 'http://localhost:8080/api/v1/instructor/getAllInstructors',
            method: 'GET',
            headers: token ? { 'Authorization': 'Bearer ' + token } : {},

            success: function (response) {
                let instructors = response.data;
                let container = $("#instructorCards");
                let tbody = $('.instructor-tbody');

                tbody.empty();
                container.empty();

                if (!instructors || instructors.length === 0) {
                    container.html('<p>No items added yet</p>');
                    return;
                }

                instructors.forEach(instructor => {
                    let instructorName = instructor.instructorName;
                    let age = instructor.age;
                    let category = instructor.category;
                    let instructorEmail = instructor.instructorEmail;
                    let instructorPhone = instructor.instructorPhone;
                    let imagePath = instructor.image;

                    let imageUrl = imagePath
                    ?`http://localhost:8080/uploads/${imagePath}`
                        :null

                    let imagesHtml = imageUrl
                    ?`<img src="${imageUrl}" alt="image" width="40" style="margin-right: 5px;">:`
                        :'No Images Found';



                    let row = `
                    <tr class="item-row" 
                        data-item-name="${instructorName}"
                        data-age="${age}"
                        data-category="${category}"
                        data-email="${instructorEmail}"
                        data-phone="${instructorPhone}"
                        data-image="${imagePath}">
                        <td>${instructorName}</td>
                        <td>${age}</td>
                        <td>${category}</td>
                        <td>${instructorEmail}</td>
                        <td>${instructorPhone}</td>
                        <td>${imagesHtml}</td>
                        <td>
                            <button class="btn btn-sm" style="background-color:bisque" data-id="${instructor.id}" id="editBtn">Edit</button>
                            <button class="btn btn-sm" style="background-color: cornflowerblue">Delete</button>
                        </td>
                    </tr>`;
                    tbody.append(row);


                   let card = `
                        <div class="col-md-3">
                            <div class="card card-custom h-100">
                                ${imageUrl ? `<img src="${imageUrl}" class="card-img-top" alt="${instructorName}">`
                                 : null}
                                
                                <div class="card-body">
                                    <h5 class="card-title">${instructorName}</h5>
                                    <p class="card-text"><strong>Age : </strong>${age}</p>
                                    <p class="card-text"><strong>Category : </strong>${category}</p>
                                    <p class="card-text"><strong>Email : </strong>${instructorEmail}</p>
                                    <p class="card-text"><strong>Phone : </strong>${instructorPhone}</p>
                                </div>
                            </div>
                        </div>
                   `;
                   container.append(card);

                });
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

    $(document).on('click', '#editBtn', function () {
        let id = $(this).data('id');
        editInstructor(id)
    })
    function editInstructor(id){
        $.ajax({
            url: `http://localhost:8080/api/v1/instructor/getInstructorById/${id}`,
            method: "GET",
            success : function (response){
                let instructor = response.data;
                $('#editInstructorId').val(instructor.id);
                $('#editInstructorName').val(instructor.instructorName);
                $('#editInstructorAge').val(instructor.age);
                $('#editInstructorCategory').val(instructor.category);
                $('#editInstructorEmail').val(instructor.instructorEmail);
                $('#editInstructorPhone').val(instructor.instructorPhone);

                let imagesHtml = '';
                if (instructor.image) {
                    imagesHtml = `<img src="http://localhost:8080/uploads/${instructor.image}" width="100" style="margin-right:10px"/>`;
                } else {
                    imagesHtml = 'No Images';
                }

                $('#EditInstructorImg').html(imagesHtml)
                $('#editInstructorModal').modal('show');
            },
            error: function (xhr, status, error) {
                    console.log(xhr.responseText);
                    alert("Failed to edit Instructor!");
            }

        })
    }

    $('#editInstructorForm').submit(function (event) {
        event.preventDefault();

        let token = localStorage.getItem('authtoken');

        let existingImage = $('#EditInstructorImg img').attr('src')
            ? $('#EditInstructorImg img').attr('src').replace("http://localhost:8080/uploads/", "")
            : null;
        let formData= new FormData();

        let instructor = {
           id: $('#editInstructorId').val(),
            instructorName: $('#editInstructorName').val(),
             age: $('#editInstructorAge').val(),
            category: $('#editInstructorCategory').val(),
            instructorEmail: $('#editInstructorEmail').val(),
            instructorPhone: $('#editInstructorPhone').val(),
            image: existingImage,

        }
        formData.append('instructor', new Blob([JSON.stringify(instructor)], {type: 'application/json'}));

        let files = $('#EditInstructorFileInput')[0].files;
        for(let i = 0; i < files.length; i++) {
            formData.append('file', files[i]);
        }
        $.ajax({
            url:'http://localhost:8080/api/v1/instructor/updateInstructor',
            method: 'PUT',
            headers:{
                'Authorization': 'Bearer ' + token
            },
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                if (response.code === 200) {
                    Swal.fire({
                        icon: "success",
                        title: "Updated Successful",
                        showConfirmButton: false,
                        timer: 2000
                    }).then(() => {
                        loadInstructors();
                        window.location.href = "Instructor.html";
                    })
                } else if (response.code === 502) {
                    Swal.fire({
                        icon: "error",
                        title: response.message,
                        showConfirmButton: false,
                        timer: 2000
                    })
                }
            },
            error:function (xhr){
                if (xhr.status === 403) {
                    Swal.fire({
                        icon: "error",
                        title: "Not Authenticated",
                        showConfirmButton: false,
                        timer: 2000
                    })
                }
            }
        })
    })
})