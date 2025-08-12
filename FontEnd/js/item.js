
$(document).ready(function(){
    loadItems();
    $('#itemForm').on('submit', function(e){
        e.preventDefault();

        let token = localStorage.getItem("authtoken");

        let item = {
            itemName: $('#itemName').val(),
            itemDescription:$('#itemDescription').val()

        };
        let formData = new FormData();
        formData.append('item', new Blob([JSON.stringify(item)], {type: 'application/json'}));

        let files = $('#itemImg')[0].files;
        for(let i = 0; i < files.length; i++) {
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
                Swal.fire({
                    icon: "success",
                    title: "Added Successful",
                    showConfirmButton: false,
                    timer: 2000
                }).then(() => {
                    loadItems();
                    window.location.href = "items.html";
                })

            },
            error: function () {
                if(xhr.status === 403){
                    Swal.fire({
                        icon: "Error",
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
            headers: token ? { 'Authorization': 'Bearer ' + token } : {}, // ✅ token optional

            success: function (items) {
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
                    let description = item.itemDescription;
                    let imagePaths = item.itemImage || [];

                    let imagesHtml = imagePaths.length > 0
                        ? imagePaths.map(img => `<img src="http://localhost:8080/uploads/${img}" alt="Item Image" width="80" style="margin-right: 5px">`).join('')
                        : 'No Images Found';

                    let row = `
                    <tr class="item-row" 
                        data-item-name="${itemName}"
                        data-description="${description}"
                        data-image="${imagePaths.join(';')}">
                        <td>${itemName}</td>
                        <td>${description}</td>
                        <td>${imagesHtml}</td>
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
                            <div class="card-text">${description}</div>
                            <a href="Lacquer.html" class="btn">View More</a>
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
    if(!token){
        window.location.href = "SignIn.html"
        return
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
})
