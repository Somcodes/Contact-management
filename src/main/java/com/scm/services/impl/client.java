package com.scm.services.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class client {

    GEmailSender gEmailSender;
    
    @Autowired
    public client(GEmailSender gEmailSender) {
        this.gEmailSender = gEmailSender;
    }

    public void clientMethod(String to, String subject, String text) {

        

        String from = "soumitraagrawal164gmail.com";
        File file=new File("new FileInput");
        

        boolean b = gEmailSender.sendEmail(to, from, subject, text);
        if(b) {
            System.out.println("\n\n\n\n\nEmail is sent successfully\n\n\n\n\n");
        } else {
            System.out.println("\n\n\n\n\nThere is a problem in sending email\n\n\n\n\n\n");
        }
    }
}
