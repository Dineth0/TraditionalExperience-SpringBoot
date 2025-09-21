$(document).ready(function(){
    let params = new URLSearchParams(window.location.search);
    loadInstructorsDropDown();
    loadItemsDropDown();
    // loadWorkshopsForTable();

    let currentPage = 0;
    const pageSize = 2;
    let totalPages = 0;
    loadWorkshopForPage();

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
        window.location.href = "index.html";
    }

    function loadItemsDropDown(selectedItemId = null) {
        let token = localStorage.getItem("authtoken");
        $.ajax({
            url: 'http://localhost:8080/api/v1/item/getAllItems',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            },
            success: function (response) {
                let items = response.data;
                let select = $('#itemSelect');
                select.empty();
                select.append('<option value="">Select Item</option>');
                items.forEach(item => {
                    select.append(`<option value="${item.id}">${item.itemName}</option>`);
                })
                let editSelect = $('#EditItemSelect');
                editSelect.empty();
                editSelect.append('<option value="">Edit Item</option>');
                items.forEach(item => {
                    let selected = (item.id === selectedItemId) ? 'selected' : '';
                    editSelect.append(`<option value="${item.id}"${selected}>${item.itemName}</option>`);
                })
            }
        })
    }

    function loadInstructorsDropDown(selectedInstructorId = null) {
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
                        let displayName = instructor.instructorName;
                        if(instructor.category && instructor.category){
                            displayName += " - " + instructor.category;
                        }
                        select.append(`<option value="${instructor.id}">${displayName}</option>`);
                    });
                    let editSelect = $('#EditInstructorSelect');
                    editSelect.empty();
                    editSelect.append('<option value="">Edit Item</option>');
                    instructors.forEach(instructor => {
                        let displayName = instructor.instructorName;
                        if (instructor.category && instructor.category){
                            displayName += " - " + instructor.category
                        }
                        let selected = (instructor.id === selectedInstructorId) ? 'selected' : '';
                        editSelect.append(`<option value="${instructor.id}"${selected}>${displayName}</option>`);
                    })
                }

            }
        })
    }
    $('#addTimeButton').on('click', function(){
        $('#timeSection').append(' <input class="form-control time-input" type="time" name="fee" id="time" required>')
    });
    $('#addIncludeButton').on('click', function(){
        $('#includeSection').append(' <input class="form-control include-input" type="text" name="include" id="include" required>')
    })

    $('#workshopForm').on('submit', function(e){
        e.preventDefault();


        let token = localStorage.getItem("authtoken");

        let times = [];
        $('.time-input').each(function(){
            if($(this).val()){
                times.push($(this).val());
            }
        });
        let includes=[];
        $('.include-input').each(function(){
            if($(this).val()){
                includes.push($(this).val());
            }
        })

        let workshop ={
            title: $('#workshopTitle').val(),
            description: $('#description').val(),
            duration: $('#duration').val(),
            language: $('#language').val(),
            participantCount: $('#participantCount').val(),
            fee: $('#fees').val(),
            address: $('#address').val(),
            instructorName:$('#instructorSelect').val(),
            itemId:$('#itemSelect').val(),
            instructorId:$('#instructorSelect').val(),
            time:times,
            include:includes

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
                    let duration = workshops.duration;
                    let language = workshops.language;
                    let participantCount = workshops.participantCount;
                    let include = workshops.include;
                    let fee = workshops.fee;
                    let address = workshops.address;
                    let instructorName = workshops.instructorName;
                    let times= workshops.time;
                    let imagePaths = workshops.image || []

                    let imagesHtml = imagePaths.length > 0
                        ? imagePaths.map(img => `<img src="http://localhost:8080/uploads/${img}" alt="Item Image" width="40" style="margin-right: 5px;">`).join('')
                        : 'No Images Found';

                    let row = `
                    <tr class="workshop-row" 
                        data-workshop-title="${workshopTitle}"
                        data-description="${workshopDescription}"
                        data-duration="${duration}"
                        data-language="${language}"
                        data-participantCount="${participantCount}"
                        data-include="${include}"
                        data-fee="${fee}"
                        data-address="${address}"
                        data-instructorName = "${instructorName}"
                        data-time="${times}"
                       
                        data-image="${imagePaths.join(';')}">
                        <td>${workshopTitle}</td>
                        <td>${workshopDescription}></td>
                        <td>${duration}</td>
                        <td>${language}</td>
                        <td>${participantCount}</td>
                        <td>${include}</td>
                        <td>${fee}</td>
                        <td>${address}</td>
                        <td>${instructorName}</td>
                         <td>${times.join(', ')}</td>
                        
                        <td class="wokshop-images">${imagesHtml}</td>
                        <td>
                            <button class="btn btn-sm editBtn" style="background-color:bisque" data-id="${workshops.id}" >Edit</button>
                            <button class="btn btn-sm" style="background-color: cornflowerblue" data-id="${workshops.id}" id="deleteBtn">Delete</button>
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
    $(document).on('click','.editBtn' , function (){
        let id = $(this).data('id')
        editWorkshop(id)
    })

    function editWorkshop(id){
        $.ajax({
            method:"GET",
            url: `http://localhost:8080/api/v1/workshop/getWorkshopById/${id}`,
            success: function (response) {
                let workshop = response.data;

               loadItemsDropDown(workshop.itemId);
               loadInstructorsDropDown(workshop.instructorId);

                $('#EditWorkshopId').val(workshop.id);
                $('#EditWorkshopTitle').val(workshop.title);
                    $('#EditDescription').val(workshop.description);
                   $('#EditDuration').val(workshop.duration);
                   $('#EditLanguage').val(workshop.language);
                    $('#EditParticipantCount').val(workshop.participantCount);
                    $('#EditFees').val(workshop.fee);
                    $('#EditAddress').val(workshop.address);
                    $('#EditInstructorSelect').val(workshop.instructorName);
                    $('#EditTime').val(workshop.time);
                    $('#EditInclude').val(workshop.include);

                $('#EditTimeSection').empty();
                workshop.time.forEach(t => {
                    $('#EditTimeSection').append(`
      <input class="form-control time-input" type="time" name="time" value="${t}" required>
   `);
                });
                $('#EditTimeSection').append(`<button type="button" id="addEditTimeButton" class="btn btn-primary">Add Another Time</button>`);


                $('#EditIncludeSection').empty();
                workshop.include.forEach(i => {
                    $('#EditIncludeSection').append(`
      <input class="form-control include-input" type="text" name="include" value="${i}" required>
   `);
                });
                $('#EditIncludeSection').append(`<button type="button" id="addEditIncludeButton" class="btn btn-primary">Add Another Include</button>`);



                let imagesHtml = '';
                    if(workshop.image && workshop.image.length > 0) {
                        workshop.image.forEach(img => {
                            imagesHtml += `<img src="http://localhost:8080/uploads/${img}" width="100" style="margin-right:10px"/>`

                        })
                    }else {
                        imagesHtml = "No Images"
                    }
                    $('#EditWorkshopImg').html(imagesHtml)
                    $('#editWorkshopModal').modal('show')
            },
            error:function (xhr, status, error){
                console.log(xhr.responseText)
                alert("Faild to Edit Workshop")
            }
        })
    }

    $('#workshopEditForm').submit(function (event) {
        event.preventDefault();

        let token = localStorage.getItem('authtoken');

        let existingImage = $('#EditWorkshopImg img').map(function(){
            return $(this).attr('src').replace("http://localhost:8080/uploads/", "");
        }).get();
        let formData = new FormData();

        let workshop = {
             id:$('#EditWorkshopId').val(),
            title: $('#EditWorkshopTitle').val(),
            description: $('#EditDescription').val(),
            duration: $('#EditDuration').val(),
            language: $('#EditLanguage').val(),
            participantCount: parseInt($('#EditParticipantCount').val()),
            fee: $('#EditFees').val(),
            address: $('#EditAddress').val(),
            instructorName: $('#EditInstructorSelect').val(),
            itemId:$('#EditItemSelect').val(),
            instructorId:$('#EditInstructorSelect').val(),
            image: existingImage,
        };
        formData.append('workshop', new Blob([JSON.stringify(workshop)], {type: 'application/json'}));
        let files = $('#EditWorkshopFileInput')[0].files;
        for(let i = 0; i < files.length; i++) {
            formData.append('file', files[i]);
        }
        $.ajax({
            url:`http://localhost:8080/api/v1/workshop/updateWorkshop`,
            method: 'PUT',
            headers:{
                "Authorization" : 'Bearer ' + token
            },
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                if (response.code === 200) {
                    Swal.fire({
                        icon: "success",
                        title: "Updated Successful",
                        showConfirmButton: "OK",
                        timer: 2000
                    }).then(() => {
                        $('#editWorkshopModal').modal('hide')
                        loadWorkshopsForTable();
                        // window.location.href = "item-details.html";
                    })
                } else if (response.code === 502) {
                    Swal.fire({
                        icon: "error",
                        title: response.message,
                        showConfirmButton: false,
                        timer: 2000
                    })
                    console.log(response.error)
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
    $(document).on('click', '#deleteBtn', function () {
        let id = $(this).data("id");
        let row = $(this).closest(".workshop-row");

        Swal.fire({
            title: "Are you sure?",
            text: "You want to Delete this Workshop?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Yes, Delete it",

        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: `http://localhost:8080/api/v1/workshop/deleteWorkshop/${id}`,
                    method: "DELETE",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    success: function(response) {
                        Swal.fire("Deleted!"," Workshop has been canceled.","success");
                        row.fadeOut(400,()=>row.remove());
                    },
                    error: function(error) {
                        Swal.fire("Error","Unable to delete Workshop","error");
                    }
                })
            }
        })
    })

    function loadWorkshopForPage() {
        let token = localStorage.getItem("authtoken")
        $.ajax({
            url: `http://localhost:8080/api/v1/workshop/paginated?page=${currentPage}&size=${pageSize}`,
            type: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (res) {
                const workshops = res || []; // backend returns raw array
                let rows = "";
                workshops.forEach(workshops => {
                    let workshopTitle = workshops.title;
                    let duration = workshops.duration;
                    let language = workshops.language;
                    let participantCount = workshops.participantCount;
                    let include = workshops.include;
                    let fee = workshops.fee;
                    let address = workshops.address;
                    let instructorName = workshops.instructorName;
                    let times= workshops.time;
                    let imagePaths = workshops.image || []

                    let imagesHtml = imagePaths.length > 0
                        ? imagePaths.map(img => `<img src="http://localhost:8080/uploads/${img}" alt="Item Image" width="40" style="margin-right: 5px;">`).join('')
                        : 'No Images Found';

                     rows += `
                    <tr class="workshop-row" 
                        data-workshop-title="${workshopTitle}"
                     
                        data-duration="${duration}"
                        data-language="${language}"
                        data-participantCount="${participantCount}"
                        data-include="${include}"
                        data-fee="${fee}"
                        data-address="${address}"
                        data-instructorName = "${instructorName}"
                        data-time="${times}"
                       
                        data-image="${imagePaths.join(';')}">
                        <td>${workshopTitle}</td>
                     
                        <td>${duration}</td>
                        <td>${language}</td>
                        <td>${participantCount}</td>
                        <td>${include}</td>
                        <td>${fee}</td>
                        <td>${address}</td>
                        <td>${instructorName}</td>
                         <td>${times.join(', ')}</td>
                        
                        <td>${imagesHtml}</td>
                        <td>
                            <button class="btn btn-sm editBtn" style="background-color:bisque" data-id="${workshops.id}" >Edit</button>
                            <button class="btn btn-sm" style="background-color: cornflowerblue" data-id="${workshops.id}" id="deleteBtn">Delete</button>
                        </td>
                    </tr>`;
                });
                $('.workshop-tbody').html(rows);
                loadPagination();
            },

            error: function(xhr){
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
        });
    }

    function loadPagination() {
        let token = localStorage.getItem("authtoken")
        $.ajax({
            url: `http://localhost:8080/api/v1/workshop/total-pages?size=${pageSize}`,
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
            error: function (xhr , error) {
                console.error("Error loading pagination:", xhr.responseText);
                if(xhr.status === 403){
                    Swal.fire({
                        icon: "error",
                        title: "Not Authenticated",
                        showConfirmButton: false,
                        timer: 2000
                    })
                    console.log(error)
                }
            }
        });
    }

    window.goToPage = function(page) {
        currentPage = page;
        loadWorkshopForPage();
    }
    $(document).on('click', '#prevPage', function(e) {
        e.preventDefault();
        if (currentPage > 0) goToPage(currentPage - 1);
    });

    $(document).on('click', '#nextPage', function(e) {
        e.preventDefault();
        if (currentPage < totalPages - 1) goToPage(currentPage + 1);
    });

    function searchWorkshops(){
        let keyword = $('#searchInput').val();
        let token  = localStorage.getItem("authtoken")

        if(keyword.trim() === ''){
            loadWorkshopForPage();
            return;
        }
        $.ajax({
            method: "GET",
            url: `http://localhost:8080/api/v1/workshop/searchWorkshops/${encodeURIComponent(keyword)}`,
            headers: token ? { 'Authorization': 'Bearer ' + token } : {},

            success: function (response) {
                let workshops = response.data;
                let container = $(".card-container");
                let tbody = $('.workshop-tbody');

                tbody.empty();
                container.empty();

                if (!workshops || workshops.length === 0) {
                    container.html('<p>No items added yet</p>');
                    return;
                }

                workshops.forEach(workshop => {
                    let workshopTitle = workshop.title;

                    let duration = workshop.duration;
                    let language = workshop.language;
                    let participantCount = workshop.participantCount;
                    let include = workshop.include;
                    let fee = workshop.fee;
                    let address = workshop.address;
                    let instructorName = workshop.instructorName;
                    let times= workshop.time;
                    let imagePaths = workshop.image || []

                    let imagesHtml = imagePaths.length > 0
                        ? imagePaths.map(img => `<img src="http://localhost:8080/uploads/${img}" alt="Item Image" width="40" style="margin-right: 5px;">`).join('')
                        : 'No Images Found';

                    let row = `
                    <tr class="workshop-row" 
                        data-workshop-title="${workshopTitle}"
                   
                        data-duration="${duration}"
                        data-language="${language}"
                        data-participantCount="${participantCount}"
                        data-include="${include}"
                        data-fee="${fee}"
                        data-address="${address}"
                        data-instructorName = "${instructorName}"
                        data-time="${times}"
                       
                        data-image="${imagePaths.join(';')}">
                        <td>${workshopTitle}</td>
                 
                        <td>${duration}</td>
                        <td>${language}</td>
                        <td>${participantCount}</td>
                        <td>${include}</td>
                        <td>${fee}</td>
                        <td>${address}</td>
                        <td>${instructorName}</td>
                         <td>${times.join(', ')}</td>
                        
                        <td>${imagesHtml}</td>
                        <td>
                            <button class="btn btn-sm editBtn" style="background-color:bisque" data-id="${workshops.id}" >Edit</button>
                            <button class="btn btn-sm" style="background-color: cornflowerblue" data-id="${workshops.id}" id="deleteBtn">Delete</button>
                        </td>
                    </tr>`;
                    tbody.append(row);
                })


            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                alert("Failed to search Bookings!");
            }
        })
    }
    $('#searchInput').on('keyup', function () {
        searchWorkshops();
    })



})