package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.PaymentDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ReviewDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Payment;
import lk.ijse.gdse.traditionalexperiencebackend.entity.PaymentMethod;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Review;
import lk.ijse.gdse.traditionalexperiencebackend.entity.WorkshopRegistration;
import lk.ijse.gdse.traditionalexperiencebackend.repo.PaymentRepo;
import lk.ijse.gdse.traditionalexperiencebackend.repo.UserRepo;
import lk.ijse.gdse.traditionalexperiencebackend.repo.WorkshopRegistrationRepo;
import lk.ijse.gdse.traditionalexperiencebackend.repo.WorkshopRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.PaymentService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final WorkshopRegistrationRepo workshopRegistrationRepo;
    private final WorkshopRepo workshopRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public int savePayment(PaymentDTO paymentDTO) {
        try{
            WorkshopRegistration workshopRegistration =
                    workshopRegistrationRepo.findById(paymentDTO.getRegistrationId())
                            .orElseThrow(() -> new RuntimeException("Registration not found"));

            Payment payment = new Payment();
            payment.setAmount(workshopRegistration.getTotalFee());
            payment.setPaymentDate(paymentDTO.getPaymentDate());

            if(paymentDTO.getPaymentMethod() == PaymentMethod.CARD || paymentDTO.getPaymentMethod() == PaymentMethod.WALLET){
                payment.setStatus("Success");
            }else if(paymentDTO.getPaymentMethod() == PaymentMethod.BANK_TRANSFER){
                payment.setStatus("Pending");
            }
            payment.setPaymentMethod(paymentDTO.getPaymentMethod());
            payment.setWorkshopRegistration(workshopRegistration);
            payment.setUser(userRepo.findById(paymentDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found")));

            payment.setWorkshop(workshopRepo.findById(paymentDTO.getWorkshopId())
                            .orElseThrow(()-> new RuntimeException("Workshop not found")));

            paymentRepo.save(payment);
            workshopRegistration.setPaymentStatus("Success");
            return VarList.Created;
        }catch(RuntimeException e){
            e.printStackTrace();
            return VarList.Internal_Server_Error;
        }
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepo.findAll();
        return payments.stream()
                .map(reg -> {
                    PaymentDTO paymentDTO = modelMapper.map(reg, PaymentDTO.class);
                    if(reg.getWorkshop() != null){
                        paymentDTO.setWorkshopName(reg.getWorkshop().getTitle());
                    }

                    return paymentDTO;
                }).toList();
    }

    @Override
    public List<PaymentDTO> getPaymentsForPage(int page, int size) {
        int offset = page * size;
        List<Payment> payments = paymentRepo.findPaymentPaginated(size, offset);
        return payments.stream()
                .map(pay -> {
                    PaymentDTO paymentDTO = modelMapper.map(pay, PaymentDTO.class);
                    if(pay.getWorkshop() != null){
                        paymentDTO.setWorkshopName(pay.getWorkshop().getTitle());
                        paymentDTO.setUserName(pay.getUser().getUsername());
                    }
                    return paymentDTO;
                }).toList();
    }

    @Override
    public int getTotalPages(int size) {
        int paymentCount = paymentRepo.getTotalPaymentCount();
        return (int) Math.ceil((double) paymentCount / size);
    }

}
