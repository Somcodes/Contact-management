package com.scm.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.forms.EmailForm;
import com.scm.forms.UploadForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.EmailServiceWithAttachment;
import com.scm.services.ExcelService;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    private ContactService contactService;

    private ImageService imageService;

    private UserService userService;

    private ExcelService excelService;

    private EmailServiceWithAttachment emailServiceWithAttachment;

    
    @Autowired
    public ContactController(ContactService contactService, ImageService imageService, UserService userService,
            ExcelService excelService, EmailServiceWithAttachment emailServiceWithAttachment) {
        this.contactService = contactService;
        this.imageService = imageService;
        this.userService = userService;
        this.excelService = excelService;
        this.emailServiceWithAttachment = emailServiceWithAttachment;
    }
    @RequestMapping("/testing") 
    public String testing() {
        return "testing";
    }
    @RequestMapping("/excelfileuplaod")
    public String ExcelUpload(Model model) {
        UploadForm uploadForm = new UploadForm();
        model.addAttribute("uploadForm", uploadForm);
        return "user/upload_excel";
    }
    
    @RequestMapping(value = "/processexcel", method = RequestMethod.POST)
    public String processExcelForm(@Valid @ModelAttribute("uploadForm") UploadForm uploadForm, BindingResult bindingResult,
                            Authentication authentication, HttpSession session) {
        if(bindingResult.hasErrors()) {
            return "user/upload_excel";
        }

        Map<String, String>  map = new HashMap<>();
        map.put(uploadForm.getName(), "name");
        map.put(uploadForm.getEmail(), "email");
        map.put(uploadForm.getPhoneNumber(), "phoneNumber");
        map.put(uploadForm.getAddress(), "address");
        map.put(uploadForm.getDescription(), "description");
        map.put(uploadForm.getWebsiteLink(), "websiteLink");
        map.put(uploadForm.getLinkedInLink(), "linkedInLink");
        System.out.println(map);
        String email = Helper.getEmailOfLoggedInUser(authentication);
        User userByEmail = userService.getUserByEmail(email);

        excelService.readAndSaveExcel(uploadForm.getExcelFile(), map, userByEmail);
        
        session.setAttribute("message", Message.builder()
        .content("Your excel data is saved to smart contact manager")
        .type(MessageType.green)
        .build());
        
        return "redirect:/user/contacts/excelfileupload";
    }

    @RequestMapping("/add")
    // add contact page: handler
    public String addContactView(Model model) {

        ContactForm contactForm = new ContactForm();
        // contactForm.setName("Soumitra");
        // contactForm.setFavourite(true);

        model.addAttribute("contactForm", contactForm);

        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        // validate form
        // TODO: for me

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> logger.info(error.toString()));
            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        // process the form data
        String username = Helper.getEmailOfLoggedInUser(authentication);

        // form ---> contact
        User user = userService.getUserByEmail(username);

        // process the contact picture

        // image process

        // upload krne ka code

        
        Contact contact = new Contact();
        contact.setFavourite(contactForm.isFavourite());
        contact.setEmail(contactForm.getEmail());
        contact.setName(contactForm.getName());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            String filename = UUID.randomUUID().toString(); 
            String fileUrl = imageService.uploadImage(contactForm.getContactImage(), filename);
            
            contact.setPicture(fileUrl);
            contact.setCloudinaryImagePublicId(filename);

        }

        // save to database
        contactService.save(contact);
        System.out.println(contactForm);

        // set the contact picture url

        // set message to be displayed on the view

        session.setAttribute("message", Message.builder()
                .content("You have successfully added a new contact")
                .type(MessageType.green)
                .build());
        return "redirect:/user/contacts/add";
    }

    // view contacts

    @GetMapping
    public String viewContacts(
        @RequestParam(value="page", defaultValue= "0") int page, 
        @RequestParam(value="size", defaultValue= "10") int size,
        @RequestParam(value="sortBy", defaultValue= "name") String sortBy,
        @RequestParam(value="direction", defaultValue= "acs") String direction,
        Model model, Authentication authentication) {


        //load all the user contacts

        String username = Helper.getEmailOfLoggedInUser(authentication);
        
        User user = userService.getUserByEmail(username);

        Page<Contact> pageContacts = contactService.getByUser(user, page, size, sortBy, direction);
        List<Contact>totalContacts=contactService.getByUserId(user.getUserId());
        model.addAttribute("totalContacts", totalContacts);
            
        model.addAttribute("pageContacts", pageContacts);
        
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    // search handler

    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
        Authentication authentication
    ){

        logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact=null;
        List<Contact> totalContacts=null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction, user);
            totalContacts = contactService.getByName(user, contactSearchForm.getValue());
            
        } 
        else if(contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction, user);
            totalContacts = contactService.getByEmail(user, contactSearchForm.getValue());
        }  
        else if(contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction, user);
            totalContacts = contactService.getByPhone(user, contactSearchForm.getValue());

        }else if(contactSearchForm.getField().equalsIgnoreCase("Select")) {
            return "redirect:/user/contacts?selectfield=false";
        }
        System.out.println("\n\n\n\n\n");
        logger.info(totalContacts.toString());
        System.out.println("\n\n\n\n\n");

        logger.info("pageContact {}", pageContact);
        
        
        model.addAttribute("totalContacts", totalContacts);
        model.addAttribute("pageContacts", pageContact);        
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("ContactSearchForm", contactSearchForm);

        return "user/search";
    }
    
    //delete contact
    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId, HttpSession session) {
        contactService.delete(contactId);
        logger.info("contactId{} deleted", contactId);
        session.setAttribute("message", 
            Message.builder()
            .content("content is Deleted successfully")
            .type(MessageType.green)
            .build()
        );
        return "redirect:/user/contacts";
    }

    //update contact form view
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable String contactId, Model model) {
        
        var contact= contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setEmail(contact.getEmail());
        contactForm.setName(contact.getName());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update_contact_view";
    }

    @RequestMapping(value="/update/{contactId}", method=RequestMethod.POST)
    public String updateContact(@PathVariable String contactId, @ModelAttribute @Valid ContactForm contactForm, BindingResult bindingResult, Model model) {

        //update the contact
        if(bindingResult.hasErrors()) {
            return "user/update_contact_view";
        }

        var con = contactService.getById(contactId);

        con.setId(contactId);
        con.setFavourite(contactForm.isFavourite());
        con.setEmail(contactForm.getEmail());
        con.setName(contactForm.getName());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setLinkedInLink(contactForm.getLinkedInLink());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        //process image

        if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()) {
            logger.info("file is not empty");
            String fileName = UUID.randomUUID().toString();
            
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            con.setCloudinaryImagePublicId(fileName);
            con.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);
        } else {
            logger.info("file is Empty");
        }

        
        //update contact in db
        var updateCon = contactService.update(con);
        logger.info("upadtes contact {}", updateCon);
        model.addAttribute("message", Message.builder().content("Contact Updated!!").type(MessageType.green).build());

        return "redirect:/user/contacts/view/"+contactId;
    }

    @RequestMapping(value = "/sendemail", method = RequestMethod.GET) 
    public String sendEmail(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "user/send_email";
    }

    @RequestMapping(value="/sendemail", method=RequestMethod.POST)
    public String processMail(@Valid @ModelAttribute("emailForm") EmailForm emailForm, BindingResult bindingResult, HttpSession session) {

        if(bindingResult.hasErrors()) {
            return "user/send_email";
        }

        emailServiceWithAttachment.sendEmailWithAttachment(emailForm.getTo(), emailForm.getSubject(), emailForm.getMessage(), emailForm.getFile());
        session.setAttribute("message", Message.builder().content("Email sent").type(MessageType.green).build());
        return "redirect:/user/contacts/sendemail";
    }
    
}
