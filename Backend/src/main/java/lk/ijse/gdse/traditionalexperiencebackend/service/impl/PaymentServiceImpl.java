package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.PaymentDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Payment;
import lk.ijse.gdse.traditionalexperiencebackend.entity.PaymentMethod;
import lk.ijse.gdse.traditionalexperiencebackend.entity.WorkshopRegistration;
import lk.ijse.gdse.traditionalexperiencebackend.repo.PaymentRepo;
import lk.ijse.gdse.traditionalexperiencebackend.repo.UserRepo;
import lk.ijse.gdse.traditionalexperiencebackend.repo.WorkshopRegistrationRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.PaymentService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final WorkshopRegistrationRepo workshopRegistrationRepo;
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

            paymentRepo.save(payment);
            workshopRegistration.setPaymentStatus("Success");
            return VarList.Created;
        }catch(RuntimeException e){
            e.printStackTrace();
            return VarList.Internal_Server_Error;
        }
    }

}
