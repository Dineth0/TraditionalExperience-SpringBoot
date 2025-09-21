$(document).ready(function(){

   let registrationId = sessionStorage.getItem('registrationId');
   let userId = sessionStorage.getItem('userId');
   let workshopId = sessionStorage.getItem('workshopId');




    if(!registrationId || !userId || !workshopId  ){
        Swal.fire({
            icon: "error",
            title: "Can't pay to payment",
            showConfirmButton: false,
            timer: 2000
        })
        $(".btn-pay").prop("disabled", false).text("Invalid Link")
        return;
    }
    $("form").submit(function(e){
        e.preventDefault();
        let paymentData = {
            registrationId:registrationId,
            userId:userId,
            workshopId:workshopId,
            amount: $("input[name='amount']").val(),
            paymentMethod: $("input[name='paymentMethod']:checked").val(),
            status: "PENDING",
            paymentDate: new Date().toISOString().split('T')[0]
        }
        let token = localStorage.getItem('authtoken');

        $.ajax({
            url: `http://localhost:8080/api/v1/payment/savePayment`,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(paymentData),
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                if(response.code === 200 || response.code === 201){
                    Swal.fire({
                        icon: "success",
                        title: "Payment Success ",
                        showConfirmButton: false,
                        timer: 2000
                    })

                }else {
                    Swal.fire({
                        icon: "error",
                        title: "Payment Not Success",
                        showConfirmButton: false,
                        timer: 2000
                    })
                    console.log(response)
                }
            },
            error:function (xhr, status, error){
                console.error(error)
                Swal.fire({
                    icon: "error",
                    title: "Something is Wrong",
                    showConfirmButton: false,
                    timer: 2000
                })
            }
        })
    })



})