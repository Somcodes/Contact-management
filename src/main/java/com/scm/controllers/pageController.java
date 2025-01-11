package com.scm.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;
import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class pageController {

    private UserService userService;

    private ImageService imageService;

    
    @Autowired
    public pageController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
        model.addAttribute("name", "Substring Technologies");
        model.addAttribute("youtubeChannel", "Learn Code With Durgesh");
        model.addAttribute("githubRepo", "https://github.com/learncodewithdurgesh/scm-springboot");
        return "home";
    }

    @RequestMapping("/home2")
    public String home2() {
        return "redirect:/home#home";
    }
    // about route


    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("islogin", false);
        System.out.println("About page loading");
        return "redirect:/home#about";
    }

    //services
    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "redirect:/home#services";
    }

    // contact
    @GetMapping("/contact")
    public String contact() {
        return "redirect:/home#contact";
    }

    //this is showing login page
    @GetMapping("/login") 
    public String login() {
        return new String("login");
    }

    // registration page
    @GetMapping("/register") 
    public String register(Model model) {
        UserForm userForm = new UserForm();
        //default data bhi daal sakte hai
        model.addAttribute("userForm", userForm);
        return "register";
    }

    //processing register
    @PostMapping(value = "/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult, HttpSession session) {
        System.out.println("Processing Registration");
        //fetch form data 
        //userForm
        System.out.println(userForm);
        //validate form data

        if(rBindingResult.hasErrors()) {
            return "register";
        }

        //save to database

        //userservice

        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("https://www.bing.com/ck/a?!&&p=bdf49035f096b1698914ea924b4faea0186c20a975b985b3012eb773d8d78606JmltdHM9MTczMDQxOTIwMA&ptn=3&ver=2&hsh=4&fclid=129c6a47-8ef3-60fd-021e-7bc28f5e61e9&u=a1L2ltYWdlcy9zZWFyY2g_cT1kZWZhdWx0JTIwcHJvZmlsZSUyMHBpY3R1cmUmRk9STT1JUUZSQkEmaWQ9RDExN0E5NERGNjA2OUQ4MTQzNTY1NTQ3QkJEMjIzNEY0RjQwNjhFQg&ntb=1")
        // .build();


        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        
        
        if(userForm.getImage() != null && !userForm.getImage().isEmpty()) {
            String filename = UUID.randomUUID().toString(); 
            String fileUrl = imageService.uploadImage(userForm.getImage(), filename);

            user.setProfilePic(fileUrl);

        } else {
            user.setProfilePic("https://thumbs.dreamstime.com/b/default-avatar-profile-flat-icon-vector-contact-symbol-illustration-184752213.jpg");
        }

        User saveUser = userService.saveUser(user);

        System.out.println("user saved");

        //message = "Registration Successful"

        //add the message

        Message message = Message.builder().content("Registration Successful! Email sent for verification").type(MessageType.green).build();

        session.setAttribute("message", message);

        //redirect to register page
        return "redirect:/register";
    }
}
