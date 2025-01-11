package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.repositories.UserRepo;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    private UserRepo userRepo;

    
    @Autowired
    public AuthController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }



    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session) {
        
        User user = userRepo.findByEmailToken(token).orElse(null);
        
        if(user!=null) {
            //user fetch hua hai
            if(user.getEmailToken().equals(token)) {
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);

                session.setAttribute("message", Message.builder()
            .content("Your email is verified, Now you can login")
            .type(MessageType.green)
            .build());
                System.out.println("\n\n\n\n\n");
                System.out.println("Is this working");
                System.out.println("\n\n\n\n\n");
                return "success_page";
            }
        }
        session.setAttribute("message", Message.builder()
        .content("Email not verified! Token is not associated with user")
        .type(MessageType.red)
        .build());
        return "error_page";
        
    }
    
}
