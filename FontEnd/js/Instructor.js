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
                        // loadItems();
                        // window.location.href = "items.html";
                    })
                }else if(response.code === 406) {
                    Swal.fire({
                        icon: "success",
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
                    ?`<img src="${imageUrl}" alt="image" width="80" style="margin-right: 5px>:`
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
})