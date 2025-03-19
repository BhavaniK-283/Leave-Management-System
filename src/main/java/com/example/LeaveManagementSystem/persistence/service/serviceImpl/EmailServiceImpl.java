package com.example.LeaveManagementSystem.persistence.service.serviceImpl;

import com.example.LeaveManagementSystem.persistence.entity.UserEntity;
import com.example.LeaveManagementSystem.persistence.repository.UserRepository;
import com.example.LeaveManagementSystem.persistence.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String senderMail;

    @Override
    public void sendLeaveAppliedEmail(UserEntity user, String startDate, String endDate) {
        String subject = "Leave Application Submitted";
        String message = "Dear " + user.getName() + ",\n\n" +
                "Your leave application from " + startDate + " to " + endDate +
                " has been submitted and is currently pending approval.\n\n" +
                "Regards,\nLeave Management System";

        sendEmail(user.getEmailId(), subject, message);
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
