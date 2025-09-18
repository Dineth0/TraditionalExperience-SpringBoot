package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.PaymentDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ReviewDTO;

import java.util.List;

public interface PaymentService {
    int savePayment(PaymentDTO paymentDTO);
    public List<PaymentDTO> getAllPayments();
    public List<PaymentDTO> getPaymentsForPage(int page, int size);
    public int getTotalPages(int size);
    public List<PaymentDTO> searchPaymentsByWorkshopName(String keyword);
}
