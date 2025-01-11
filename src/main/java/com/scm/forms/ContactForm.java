package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.validfile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
// @ToString
public class ContactForm {

    @NotBlank(message="Name is required")
    @Size(min = 3, message = "Mininmum 3 character is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "phone number is required")
    @Size(min = 10, max = 10, message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;
    
    // @NotBlank(message = "Description is required")
    // @Size(min = 10, message = "Atleast 10 characters is required")
    private String description;

    private boolean favourite;

    private String websiteLink;

    // @NotBlank(message = "LinkedIn Profile is needed")
    private String linkedInLink;

    //annotation create karenge jo file validate karega
    //size
    //resolution
    //type
    
    @validfile
    private MultipartFile contactImage;

    private String picture;

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getLinkedInLink() {
        return linkedInLink;
    }

    public void setLinkedInLink(String linkedInLink) {
        this.linkedInLink = linkedInLink;
    }

    public MultipartFile getContactImage() {
        return contactImage;
    }

    public void setContactImage(MultipartFile contactImage) {
        this.contactImage = contactImage;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "ContactForm [name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber + ", address="
                + address + ", description=" + description + ", favourite=" + favourite + ", websiteLink=" + websiteLink
                + ", linkedInLink=" + linkedInLink + ", contactImage=" + contactImage + ", picture=" + picture + "]";
    }



}
