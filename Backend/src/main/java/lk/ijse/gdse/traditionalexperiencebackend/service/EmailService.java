package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.entity.Email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    Email sendBookingDetailsEmail(Long registrationId, String toEmail);
}
