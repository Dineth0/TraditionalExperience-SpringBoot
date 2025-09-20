$(document).ready(function () {
    let currentPage = 0;
    const pageSize = 2;
    let totalPages = 0;

    let currentCardPage = 0;
    const CardPageSize = 3;
    let totalCardPages = 0;
    // loadItems();
    loadItemsForPage();
    // loadItemsForCardPage();
    $('#itemForm').on('submit', function (e) {
        e.preventDefault();

        let token = localStorage.getItem("authtoken");

        let item = {
            itemName: $('#itemName').val(),
            itemShortDescription: $('#itemShortDescription').val(),
            itemDescription: $('#itemDescription').val(),


        };
        let formData = new FormData();
        formData.append('item', new Blob([JSON.stringify(item)], {type: 'application/json'}));

        let files = $('#itemImg')[0].files;
        for (let i = 0; i < files.length; i++) {
            formData.append('file', files[i]);
        }
        $.ajax({
            url: 'http://localhost:8080/api/v1/item/addItem',
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                if (response.code === 201) {
                    Swal.fire({
                        icon: "success",
                        title: "Added Successful",
                        showConfirmButton: false,
                        timer: 2000
                    }).then(() => {
                        loadItems();
                        window.location.href = "items.html";
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
            error: function (xhr) {
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

    function loadItems() {
        let token = localStorage.getItem("authtoken");
        $.ajax({
            url: 'http://localhost:8080/api/v1/item/getAllItems',
            method: 'GET',
            headers: token ? {'Authorization': 'Bearer ' + token} : {},

            success: function (response) {
                let items = response.data;
                let container = $(".card-container");
                let tbody = $('.item-tbody');

                tbody.empty();
                container.empty();

                if (!items || items.length === 0) {
                    container.html('<p>No items added yet</p>');
                    return;
                }

                items.forEach(item => {
                    let itemName = item.itemName;
                    let shortDescription = item.itemShortDescription;
                    let imagePaths = item.itemImage || [];

                    let imagesHtml = imagePaths.length > 0
                        ? imagePaths.map(img => `<img src="http://localhost:8080/uploads/${img}" alt="Item Image" width="40" style="margin-right: 5px">`).join('')
                        : 'No Images Found';

                    let row = `
                    <tr class="item-row" 
                        data-item-name="${itemName}"
                        data-description="${shortDescription}"
                        data-image="${imagePaths.join(';')}">
                        <td>${itemName}</td>
                        <td>${shortDescription}</td>
                        <td>${imagesHtml}</td>
                        <td>
                            <button class="btn btn-sm" style="background-color:bisque" data-id="${item.id}" id="editBtn">Edit</button>
                            <button class="btn btn-sm" style="background-color: cornflowerblue">Delete</button>
                        </td>
                    </tr>`;
                    tbody.append(row);

                    let firstImageUrl = imagePaths.length > 0
                        ? `http://localhost:8080/uploads/${imagePaths[0]}`
                        : 'default-placeholder.png';

                    let card = `
                    <div class="card">
                        <img src="${firstImageUrl}" alt="${itemName}">
                        <div class="card-body">
                            <div class="card-title">${itemName}</div>
                            <div class="card-text">${shortDescription}</div>
            <a href="Item-Details.html?id=${item.id}" class="btn">View More</a>
                        </div>
                    </div>`;
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
                        title: "Error Loading Items",
                        text: xhr.responseText || "Please try again"
                    });
                }
            }
        })
    }


    let token = localStorage.getItem("authtoken");
    if (!token) {
        window.location.href = "SignIn.html"
        return
    }
    let payload = JSON.parse(atob(token.split('.')[1]));
    if (payload.role !== "ADMIN") {
        Swal.fire({
            icon: "Error",
            title: "Not Authenticated",
            showConfirmButton: false,
            timer: 2000
        })
        window.location.href = "home.html";
    }

    $(document).on('click', '#editBtn', function () {
        let id = $(this).data('id');
        editJob(id)
    })

    function editJob(id) {
        $.ajax({
            method: "GET",
            url: `http://localhost:8080/api/v1/item/getItem/${id}`,
            success: function (response) {
                let item = response.data
                $('#editItemId').val(item.id)
                $('#editItemName').val(item.itemName);
                $('#editItemShortDescription').val(item.itemShortDescription);
                $('#editItemDescription').val(item.itemDescription);

                let imagesHtml = '';
                if (item.itemImage && item.itemImage.length > 0) {
                    item.itemImage.forEach(img => {
                        imagesHtml += `<img src="http://localhost:8080/uploads/${img}" width="100" style="margin-right:10px"/>`;
                    });
                } else {
                    imagesHtml = 'No Images';
                }
                $('#EditItemImg').html(imagesHtml)
                $('#editItemModal').modal('show');
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                alert("Failed to edit job!");
            }
        })
    }

    $('#editItemForm').submit(function (event) {
        event.preventDefault();

        let token = localStorage.getItem("authtoken");

        let existingImages = $('#EditItemImg img').map(function () {
            return $(this).attr('src').replace("http://localhost:8080/uploads/", "");
        }).get();
        let formData = new FormData();

        let item = {
            id: $('#editItemId').val(),
            itemName: $('#editItemName').val(),
            itemShortDescription: $('#editItemShortDescription').val(),
            itemDescription: $('#editItemDescription').val(),
            itemImage: existingImages


        };
        formData.append('item', new Blob([JSON.stringify(item)], {type: 'application/json'}));
        let files = $('#EditItemFileInput')[0].files;
        for (let i = 0; i < files.length; i++) {
            formData.append('file', files[i]);
        }

        $.ajax({
            url: 'http://localhost:8080/api/v1/item/updateItem',
            method: 'PUT',
            headers: {
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
                        loadItems();
                        window.location.href = "items.html";
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
            error: function (xhr) {
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


    function loadItemsForPage() {
        let token = localStorage.getItem("authtoken")
        $.ajax({
            url: `http://localhost:8080/api/v1/item/paginated?page=${currentPage}&size=${pageSize}`,
            type: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (res) {
                const items = res || []; // backend returns raw array
                let rows = "";
                items.reverse().forEach(item => {
                    let itemName = item.itemName;
                    let shortDescription = item.itemShortDescription;
                    let imagePaths = item.itemImage || [];

                    let imagesHtml = imagePaths.length > 0
                        ? imagePaths.map(img => `<img src="http://localhost:8080/uploads/${img}" alt="Item Image" width="40" style="margin-right: 5px">`).join('')
                        : 'No Images Found';

                    rows += `
                                    <tr class="item-row"
                                        data-item-name="${itemName}"
                                        data-description="${shortDescription}"
                                        data-image="${imagePaths.join(';')}">
                                        <td>${itemName}</td>
                                        <td>${shortDescription}</td>
                                        <td>${imagesHtml}</td>
                                        <td>
                                            <button class="btn btn-sm" style="background-color:bisque" data-id="${item.id}" id="editBtn">Edit</button>
                                            <button class="btn btn-sm" style="background-color: cornflowerblue">Delete</button>
                                        </td>
                                    </tr>`;
                });
                $('.item-tbody').html(rows);
                loadPagination();
            },

            error: function (err) {
                console.log(err);
                alert("Failed to load Feedback");
            }
        });
    }

    function loadPagination() {
        let token = localStorage.getItem("authtoken")
        $.ajax({
            url: `http://localhost:8080/api/v1/item/total-pages?size=${pageSize}`,
            method: "GET",
            headers: {
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


                $('#ItemPagination').html(paginationHTML);
            },
            error: function (xhr) {
                console.error("Error loading pagination:", xhr.responseText);
            }
        });


    }

    window.goToPage = function (page) {
        currentPage = page;
        loadItemsForPage();
    }
    $(document).on('click', '#prevPage', function(e) {
        e.preventDefault();
        if (currentPage > 0) goToPage(currentPage - 1);
    });

    $(document).on('click', '#nextPage', function(e) {
        e.preventDefault();
        if (currentPage < totalPages - 1) goToPage(currentPage + 1);
    });

    function searchItems(){
        let keyword = $('#searchInput').val();
        let token  = localStorage.getItem("authtoken")

        if(keyword.trim() === ''){
            loadItemsForPage();
            return;
        }
        $.ajax({
            method: "GET",
            url: `http://localhost:8080/api/v1/item/searchItems/${encodeURIComponent(keyword)}`,
            headers: token ? { 'Authorization': 'Bearer ' + token } : {},

            success: function (response) {
                let items = response.data;
                let container = $(".card-container");
                let tbody = $('.item-tbody');

                tbody.empty();

                if (!items || items.length === 0) {
                    container.html('<p>No items added yet</p>');
                    return;
                }

                items.forEach(item => {
                    let itemName = item.itemName;
                    let shortDescription = item.itemShortDescription;
                    let imagePaths = item.itemImage || [];

                    let imagesHtml = imagePaths.length > 0
                        ? imagePaths.map(img => `<img src="http://localhost:8080/uploads/${img}" alt="Item Image" width="40" style="margin-right: 5px">`).join('')
                        : 'No Images Found';

                    let row = `
                    <tr class="item-row"
                        data-item-name="${itemName}"
                        data-description="${shortDescription}"
                        data-image="${imagePaths.join(';')}">
                        <td>${itemName}</td>
                        <td>${shortDescription}</td>
                        <td>${imagesHtml}</td>
                        <td>
                            <button class="btn btn-sm" style="background-color:bisque" data-id="${item.id}" id="editBtn">Edit</button>
                            <button class="btn btn-sm" style="background-color: cornflowerblue">Delete</button>
                        </td>
                    </tr>`;
                    tbody.append(row);
                })
                        // loadPagination();

            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                alert("Failed to search Bookings!");
            }
        })
    }
    $('#searchInput').on('keyup', function () {
        searchItems();
    })









});
