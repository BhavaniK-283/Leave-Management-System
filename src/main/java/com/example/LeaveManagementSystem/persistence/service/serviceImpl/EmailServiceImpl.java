package com.example.LeaveManagementSystem.persistence.service.serviceImpl;

import com.example.LeaveManagementSystem.persistence.entity.UserEntity;
import com.example.LeaveManagementSystem.persistence.repository.UserRepository;
import com.example.LeaveManagementSystem.persistence.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private UserEntity userId;
    private String startDate;
    private String endDate;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String senderMail;

    @Override
    public void sendLeaveAppliedEmail(UserEntity userId, String startDate, String endDate) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;

        // Construct email content
        String subject = "Leave Application Submitted";
        String message = "Dear " + userId.getName() + ",\n\n" +
                "Your leave application from " + startDate + " to " + endDate + " has been submitted and is currently pending approval.\n\n" +
                "Regards,\nLeave Management System";

        // Send Email
        sendEmail(userId.getEmailId(), subject, message);
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    
}
