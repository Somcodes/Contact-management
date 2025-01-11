package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface EmailServiceWithAttachment {
    public void sendEmailWithAttachment(String toEmail, String subject, String body, MultipartFile file);
}
