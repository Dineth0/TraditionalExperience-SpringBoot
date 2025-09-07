$(document).ready(function () {
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



})