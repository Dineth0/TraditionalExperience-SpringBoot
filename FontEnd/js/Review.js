$(document).ready(function () {
    loadAllReview();
    $(document).on('click', '#ReviewBtn', function () {
        $('#ReviewModal').modal('show')
    })

    $(function(){
        // rating click
        $(".star").on("click", function(){
            let value = $(this).data("value");
            $("#rating").val(value);

            $(".star").each(function(){
                if($(this).data("value") <= value){
                    $(this).addClass("checked");
                }else{
                    $(this).removeClass("checked");
                }
            });

            $("#ratingText").text(value + " / 5");
        });

        // form validation
        $("#reviewForm").on("submit", function(e){
            if($("#rating").val() == "0"){
                e.preventDefault();
                $("#ratingText").text("Please select a rating").addClass("text-danger");
            }
            if(!this.checkValidity()){
                e.preventDefault();
                e.stopPropagation();
                $(this).addClass("was-validated");
            }
        });
    });

    $("#reviewForm").on("submit", function(e){
        e.preventDefault();

        let token = localStorage.getItem("authtoken");

        let rating = $("#rating").val();
        if(rating === "0"){
            $("#ratingText").text("Please select a rating").addClass("text-danger");
            return;
        }

        let review = {
            visitorName:$("#name").val(),
            rating:$("#rating").val(),
            title:$("#title").val(),
            description:$("#description").val(),
            wentDate:$("#wentDate").val(),
            reviewDate: new Date(),
            userId: localStorage.getItem("userId"),
            workshopId : new URLSearchParams(window.location.search).get('id')

        }


        $.ajax({
            url:`http://localhost:8080/api/v1/review/addReview`,
            method: 'POST',
            data: JSON.stringify(review),
            contentType:'application/json',
            headers:{
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                if(response.code === 201){
                    Swal.fire("Success", "Review saved successfully!", "success")
                    $("#ReviewModal").modal("hide");
                    $("#reviewForm")[0].reset();
                    $("#rating").val(0);
                    $(".star").removeClass("checked");
                    $("#ratingText").text("No rating yet").removeClass("text-danger");
                }

            },
            error:function (err){
                Swal.fire({
                    icon: "error",
                    title: "Error Adding Review!"
                });
                console.error(err);
            }
        })
    })
    function renderStars(rating) {
        let stars = "";
        for (let i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars += '<i class="fa-solid fa-star" style="color:#FFD700;"></i>'; // filled star
            } else {
                stars += '<i class="fa-regular fa-star" style="color:#FFD700;"></i>'; // empty star
            }
        }
        return stars;
    }

    function loadAllReview(){
        let token = localStorage.getItem('authtoken');

        $.ajax({
            url: 'http://localhost:8080/api/v1/review/getAllReviews',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (response) {
                let reviews = response.data;
                let table = $('.review-tbody');
                table.empty();

                if(reviews.length === 0 || !reviews){
                    table.append('<tr><td colspan="10" class="text-center">No Reviews</td></tr>');
                    return;
                }
                reviews.forEach((review) => {
                    let row = `
                            <tr class="review-row">
                                <td>${review.workshopName}</td>
                                <td>${review.visitorName}             </td>
                                <td>${renderStars(review.rating)}</td>
                                <td>${review.title}</td>
                                <td>${review.description}</td>
                                <td>${review.wentDate}</td>
                                <td>${review.reviewDate}</td>
                                
                                <td>
                                    <button class="btn btn-sm btn-warning deleteBtn" data-id="${review.id}" >
                                    Delete
                                    </button>
                                </td>
                            </tr>
                        `;
                    table.append(row);
                })
            },
            error: function (error) {
                console.log(error);
                alert(error.message);
            }
        })
        $(document).on("click", ".deleteBtn", function() {
            let id = $(this).data("id");
            let row = $(this).closest(".review-row");

            Swal.fire({
                title: "Are you sure?",
                text: "You want to Delete this Review?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Yes, Delete it",

            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: `http://localhost:8080/api/v1/review/deleteReview/${id}`,
                        method: "DELETE",
                        headers: {
                            "Authorization": "Bearer " + token
                        },
                        success: function(response) {
                            Swal.fire("Deleted!","Your Review has been Deleted.","success");
                            row.fadeOut(400,()=>row.remove());
                        },
                        error: function(error) {
                            Swal.fire("Error","Unable to Delete Review","error");
                        }
                    })
                }
            })
        })
    }



})