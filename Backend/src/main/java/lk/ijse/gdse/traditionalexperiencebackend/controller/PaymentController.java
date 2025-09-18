package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.PaymentDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ResponseDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ReviewDTO;
import lk.ijse.gdse.traditionalexperiencebackend.service.PaymentService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/savePayment")
    public ResponseEntity<ResponseDTO> savePayment(@RequestBody PaymentDTO paymentDTO){

        int response = paymentService.savePayment(paymentDTO);

        if (response == VarList.Created) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Created, "Payment Saved", paymentDTO));
        }else {
            System.out.println(response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error While Saving", null));

        }
    }

    @GetMapping("/getPayments")
    public ResponseEntity<ResponseDTO> getAllPayments() {
        try{
            List<PaymentDTO> payments = paymentService.getAllPayments();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK,"suceess", payments));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/paginated")
    public List<PaymentDTO> getPaginatedItems(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        return paymentService.getPaymentsForPage(page, size);
    }

    @GetMapping("/total-pages")
    public int getTotalPages(@RequestParam(defaultValue = "2") int size) {
        return paymentService.getTotalPages(size);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ResponseDTO> searchPayments(@PathVariable("keyword") String keyword) {
        List<PaymentDTO> payments = paymentService.searchPaymentsByWorkshopName(keyword);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(VarList.OK, "Search Success", payments));
    }
}
