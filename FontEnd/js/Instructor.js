$(document).ready(function(){
     // loadInstructors();
    let currentPage = 0;
    const pageSize = 2;
    let totalPages = 0;
    loadInstructorForPage();
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

    $(document).on("click", "#deleteBtn", function() {
        let id = $(this).data("id");
        let row = $(this).closest(".item-row");

        Swal.fire({
            title: "Are you sure?",
            text: "You want to Delete this Instructor?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Yes, Delete it",

        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: `http://localhost:8080/api/v1/instructor/deleteInstructor/${id}`,
                    method: "DELETE",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    success: function(response) {
                        Swal.fire("Deleted!"," Instructor has been canceled.","success");
                        row.fadeOut(400,()=>row.remove());
                    },
                    error: function(error) {
                        Swal.fire("Error","Unable to delete Instructor","error");
                    }
                })
            }
        })
    })

    function loadInstructorForPage() {
        let token = localStorage.getItem("authtoken")
        $.ajax({
            url: `http://localhost:8080/api/v1/instructor/paginated?page=${currentPage}&size=${pageSize}`,
            type: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (res) {
                const instructors = res || [];
                let rows = "";
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



                     rows += `
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
                            <button class="btn btn-sm" style="background-color: cornflowerblue " id="deleteBtn" data-id="${instructor.id}">Delete</button>
                        </td>
                    </tr>`;
                });
                $('.instructor-tbody').html(rows);
                loadPagination();
            },

            error: function(err){
                console.log(err);
                // alert("Failed to load Feedback");
            }
        });
    }

    function loadPagination() {
        let token = localStorage.getItem("authtoken")
        $.ajax({
            url: `http://localhost:8080/api/v1/instructor/total-pages?size=${pageSize}`,
            method: "GET",
            headers:{
                'Authorization': 'Bearer ' + token
            },
            success: function (tp) {
                totalPages = tp
                let paginationHTML = "";
                paginationHTML += `
                    <li class="page-item ${ currentPage === 0 ? 'disabled' : ''}">
                        <a class="page-link" href="#" id="prevPage" >Previous</a>
                    </li>
                `;

                const windowSize = 3
                let start = currentPage - 1;
                let end = currentPage + 1

                if(start < 0) { start = 0}
                if(end >= totalPages) { end = totalPages - 1}

                if(end - start + 1 < windowSize && totalPages >= windowSize) {
                    if(start === 0){
                        end = windowSize - 1;
                    }else if(end === totalPages - 1){
                        start = totalPages - windowSize;
                    }
                }
                for(let i = start; i <= end; i++) {
                    paginationHTML += `
               <li class="page-item ${i === currentPage ? 'active' : ''}">
                        <a class="page-link" href="#" onclick="goToPage(${i})">${i + 1}</a>
                    </li>
            `
                }
                paginationHTML += `
               <li class="page-item ${ currentPage === totalPages - 1 ? 'disabled' : ''}">
                        <a class="page-link" href="#" id="nextPage" >Next</a>
                    </li>
          `

                $('.pagination').html(paginationHTML);
            },
            error: function (xhr) {
                console.error("Error loading pagination:", xhr.responseText);
            }
        });
    }

    window.goToPage = function(page) {
        currentPage = page;
        loadInstructorForPage();
    }
    $(document).on('click', '#prevPage', function(e) {
        e.preventDefault();
        if (currentPage > 0) goToPage(currentPage - 1);
    });

    $(document).on('click', '#nextPage', function(e) {
        e.preventDefault();
        if (currentPage < totalPages - 1) goToPage(currentPage + 1);
    });

    function searchInstructors(){
        let keyword = $('#searchInput').val();
        let token  = localStorage.getItem("authtoken")

        if(keyword.trim() === ''){
            loadInstructorForPage();
            return;
        }
        $.ajax({
            method: "GET",
            url: `http://localhost:8080/api/v1/instructor/searchInstructors/${encodeURIComponent(keyword)}`,
            headers: token ? { 'Authorization': 'Bearer ' + token } : {},

            success: function (response) {
                let instructors = response.data;
                let container = $(".card-container");
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



                   let rows = `
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
                            <button class="btn btn-sm" style="background-color: cornflowerblue " id="deleteBtn" data-id="${instructor.id}">Delete</button>
                        </td>
                    </tr>`;
                    tbody.append(rows)
                });

            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                alert("Failed to search Bookings!");
            }
        })
    }
    $('#searchInput').on('keyup', function () {
        searchInstructors();
    })
})