package com.scm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scm.services.EmailServiceWithAttachment;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceWithAttachmentImpl implements EmailServiceWithAttachment {

    private JavaMailSender mailSender;


    
    @Autowired
    public EmailServiceWithAttachmentImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }




    @Override
    public void sendEmailWithAttachment(String toEmail, String subject, String body, MultipartFile file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
           
            if(body!=null && !body.isEmpty())
            helper.setText(body, true); // Use true for HTML content

            // Attach the MultipartFile
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }

            mailSender.send(message);
            System.out.println("Email sent successfully with attachment.");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
