package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopRegistrationDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Email;
import lk.ijse.gdse.traditionalexperiencebackend.entity.WorkshopRegistration;
import lk.ijse.gdse.traditionalexperiencebackend.repo.EmailRepo;
import lk.ijse.gdse.traditionalexperiencebackend.repo.WorkshopRegistrationRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.EmailService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailRepo emailRepo;
    private final WorkshopRegistrationRepo registrationRepo;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("sltraditionalportal@gmail.com");
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);
            mailSender.send(simpleMailMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public Email sendBookingDetailsEmail(Long registrationId, String toEmail) {
        WorkshopRegistration workshopRegistration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        WorkshopRegistrationDTO workshopRegistrationDTO = new WorkshopRegistrationDTO();
        workshopRegistrationDTO.setId(workshopRegistration.getId());
        workshopRegistrationDTO.setEmail(workshopRegistration.getEmail());
        workshopRegistrationDTO.setPhone(workshopRegistration.getPhone());
        workshopRegistrationDTO.setCountry(workshopRegistration.getCountry());
        workshopRegistrationDTO.setRegistrationDate(workshopRegistration.getRegistrationDate());
        workshopRegistrationDTO.setSelectWorkshopDate(workshopRegistration.getSelectWorkshopDate());
        workshopRegistrationDTO.setWorkshopTime(workshopRegistration.getWorkshopTime());
        workshopRegistrationDTO.setMember(workshopRegistration.getMember());
        workshopRegistrationDTO.setTotalFee(workshopRegistration.getTotalFee());
        workshopRegistrationDTO.setPaymentStatus(workshopRegistration.getPaymentStatus());


        if(workshopRegistration.getUser() != null){
            workshopRegistrationDTO.setUserId(workshopRegistration.getUser().getId());
        }

        if (workshopRegistration.getWorkshop() != null) {
            workshopRegistrationDTO.setWorkshopId(workshopRegistration.getWorkshop().getId());
            workshopRegistrationDTO.setWorkshopName(workshopRegistration.getWorkshop().getTitle());

            if(workshopRegistration.getWorkshop().getInstructor() != null){
                workshopRegistrationDTO.setInstructorName(workshopRegistration.getWorkshop().getInstructor().getInstructorName());
            }
        }

        String subject = "Booking Details for Your Workshop " + workshopRegistrationDTO.getWorkshopName();

        String body = String.format(
                "Hello Instructor,\n\n" +
                        "You have received a new workshop booking detail. Please find the details below:\n\n" +
                        "Workshop Name: %s\n" + // Added Workshop Name
                        "Selected Date: %s\n" +
                        "Selected Time: %s\n" +
                        "Number of Members: %d\n\n" +
                        "Customer Contact Details:\n" +
                        "- Email: %s\n" +
                        "- Phone: %s\n" +
                        "- Country: %s\n\n" +
                        "Payment Status: %s\n\n" +
                        "Thank you.",

                workshopRegistrationDTO.getWorkshopName(),
                workshopRegistrationDTO.getSelectWorkshopDate(),
                workshopRegistrationDTO.getWorkshopTime(),
                workshopRegistrationDTO.getMember(),

                workshopRegistrationDTO.getEmail(),
                workshopRegistrationDTO.getPhone(),
                workshopRegistrationDTO.getCountry(),
                workshopRegistrationDTO.getPaymentStatus()


        );
        Email email = new Email();
        email.setSubject(subject);
        email.setBody(body);
        email.setCreatedAt(LocalDateTime.now());

        if(workshopRegistration.getWorkshop() != null && workshopRegistration.getWorkshop().getInstructor() != null){
            email.setInstructor(workshopRegistration.getWorkshop().getInstructor());
        }else {
            System.out.println(registrationId);
        }
        try {
            this.sendEmail(toEmail, subject, body);
            workshopRegistration.setEmailSent(true);
            registrationRepo.save(workshopRegistration);
            return emailRepo.save(email);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
