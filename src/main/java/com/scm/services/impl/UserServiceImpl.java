package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cloudinary.provisioning.Account;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourcesNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    private EmailService emailService;



    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepo userRepo, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id).orElseThrow(() -> new ResourcesNotFoundException("User not Found"));
        userRepo.delete(user2);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2 = userRepo.findById(userId).orElse(null);
        return user2!=null ? true : false;

    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return  user!=null ? true : false;
    }

    @Override
    public User saveUser(User user) {
        //user id : have to generate
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        //password encode
        //user.setPassword(userId)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info(user.getProvider().toString());
        //set the user role

        user.setRoleList(List.of(AppConstants.ROLE_USER));
        //user.setPassword(userId);
        // user.setProfilePic(userId);
        logger.info(user.getProvider().toString());
       



        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser = userRepo.save(user);
        String emailLink = Helper.getLinkForEmailVerification(emailToken);
        emailService.sendEmail(savedUser.getEmail(), "verify Account : Smart contact Manager", emailLink);
        // emailService.sendEmail(savedUser.getEmail(), "verify Account : Smart Contact Manager", emailLink);
        return savedUser;
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepo.findById(user.getUserId()).orElseThrow(() -> new ResourcesNotFoundException("User not Found"));
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        //save the user in database

        User save = userRepo.save(user2);

        return Optional.ofNullable(save);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public User getUserByToken(String token) {
        return userRepo.findByEmailToken(token).orElse(null);
    }
    
}
