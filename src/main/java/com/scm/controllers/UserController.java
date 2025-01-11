package com.scm.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ContactService;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    private ContactService contactService;

    // @ModelAttribute
    // public void addLoggedInUserInformation(Model model, Authentication authentication) {
    //     System.out.println("Adding logged in user information to the model");
    //     String username = Helper.getEmailOfLoggedInUser(authentication);
    //     logger.info("User logged in: {}", username);

    //     //database se data ko fetch : get user from db

    //     User user = userService.getUserByEmail(username);

    //     System.out.println(user.getName());
    //     System.out.println(user.getEmail());

    //     model.addAttribute("loggedInUser", user);
    // }


    //user dashboard page

    

    @RequestMapping("/dashboard") 
    public String userDashboard(Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        List<Contact> contactList = contactService.getByUserId(user.getUserId());
        model.addAttribute("totalContacts", contactList.size());

        List<Contact> favContactList = contactService.getByUserandFavourite(user);
        model.addAttribute("totalFavEle", favContactList.size());
        return "user/dashboard";    
    }

    @Autowired
    public UserController(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }


    @RequestMapping("/profile") 
    public String userProfile(Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}", username);

        //database se data ko fetch : get user from db

        User user = userService.getUserByEmail(username);

        System.out.println(user.getName());
        System.out.println(user.getEmail());

        model.addAttribute("loggedInUser", user);
        return "user/profile";
    }
    //user add contact page

    //user view contact page

    //user edit contact

    //user delete contact

    //user search contact


}
