$(document).ready(function(){
    let currentCardPage = 0;
    const CardPageSize = 3;
    let totalCardPages = 0;

    loadItemsForCardPage();

    function loadItemsForCardPage() {
        let token = localStorage.getItem("authtoken")
        $.ajax({
            url: `http://localhost:8080/api/v1/item/CardPaginated?page=${currentCardPage}&size=${CardPageSize}`,
            type: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (res) {
                const items = res || []; // backend returns raw array
                let cards = "";
                items.forEach(item => {
                    let itemName = item.itemName;
                    let shortDescription = item.itemShortDescription;
                    let imagePaths = item.itemImage || [];


                    let firstImageUrl = imagePaths.length > 0
                        ? `http://localhost:8080/uploads/${imagePaths[0]}`
                        : 'default-placeholder.png';

                    cards += `
            <div class="card">
                <img src="${firstImageUrl}" alt="${itemName}">
                <div class="card-body">
                    <div class="card-title">${itemName}</div>
                    <div class="card-text">${shortDescription}</div>
                    <a href="Item-Details.html?id=${item.id}" class="btn">View More</a>
                </div>
            </div>`;
                });
                $('.card-container').html(cards);
                loadPaginationForCard();
            },

            error: function (err) {
                console.log(err);
                alert("Failed to load page..SignUp First");
            }
        });
    }
    function loadPaginationForCard() {
        let token = localStorage.getItem("authtoken")
        $.ajax({
            url: `http://localhost:8080/api/v1/item/total-CardPages?size=${CardPageSize}`,
            method: "GET",
            headers: { 'Authorization': 'Bearer ' + token },
            success: function (tp) {
                totalCardPages = tp;
                let paginationHTML = "";


                paginationHTML += `
                    <li class="CardPage-item ${currentCardPage === 0 ? 'disabled' : ''}">
                            <a class="CardPage-link prev-card" href="#">Previous</a>
                        </li>
                `;

                const windowSize = 3
                let start = currentCardPage - 1;
                let end = currentCardPage + 1

                if(start < 0) { start = 0}
                if(end >= totalCardPages) { end = totalCardPages - 1}

                if(end - start + 1 < windowSize && totalCardPages >= windowSize) {
                    if(start === 0){
                        end = windowSize - 1;
                    }else if(end === totalCardPages - 1){
                        start = totalCardPages - windowSize;
                    }
                }
                for(let i = start; i <= end; i++) {
                    paginationHTML += `
               <li class="CardPage-item ${i === currentCardPage ? 'active' : ''}">
                            <a class="CardPage-link" href="#" data-page="${i}">${i + 1}</a>
                        </li>
            `
                }
                paginationHTML += `
                 <li class="CardPage-item ${currentCardPage === totalCardPages - 1 ? 'disabled' : ''}">
                            <a class="CardPage-link next-card" href="#">Next</a>
                        </li>
          `



                $('#CardPagination').html(paginationHTML);
            },
            error: function (xhr) {
                console.error("Error loading pagination:", xhr.responseText);
            }
        });
    }


    $(document).on('click', '.CardPage-link[data-page]', function(e) {
        e.preventDefault();
        let page = parseInt($(this).data('page'));
        currentCardPage = page;
        loadItemsForCardPage();
    });

    // Previous button
    $(document).on('click', '.CardPage-link.prev-card', function(e) {
        e.preventDefault();
        if(currentCardPage > 0) {
            currentCardPage--;
            loadItemsForCardPage();
        }
    });

    $(document).on('click', '.CardPage-link.next-card', function(e) {
        e.preventDefault();
        if(currentCardPage < totalCardPages - 1) {
            currentCardPage++;
            loadItemsForCardPage();
        }
    });
    window.goToCardPage = function(page){
        currentCardPage = page;
        loadItemsForCardPage();
    }

    function searchItemsInUserSide(){
        let keyword = $('#searchItemInput').val();
        let token  = localStorage.getItem("authtoken")

        if(keyword.trim() === ''){
            loadItemsForCardPage();
            return;
        }
        $.ajax({
            method: "GET",
            url: `http://localhost:8080/api/v1/item/searchItems/${encodeURIComponent(keyword)}`,
            headers: token ? { 'Authorization': 'Bearer ' + token } : {},

            success: function (response) {
                let items = response.data;
                let container = $(".card-container");

                container.empty();

                if (!items || items.length === 0) {
                    container.html('<p>No items added yet</p>');
                    return;
                }

                items.forEach(item => {
                    let itemName = item.itemName;
                    let shortDescription = item.itemShortDescription;
                    let imagePaths = item.itemImage || [];


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
                })
                // loadPagination();

            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
                // alert("Failed to search Bookings!");
            }
        })
    }
    $('#searchItemInput').on('keyup', function () {
        searchItemsInUserSide();
    })
})