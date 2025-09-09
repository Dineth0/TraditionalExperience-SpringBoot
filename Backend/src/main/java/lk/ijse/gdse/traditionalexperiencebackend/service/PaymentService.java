package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.PaymentDTO;

public interface PaymentService {
    int savePayment(PaymentDTO paymentDTO);
}
