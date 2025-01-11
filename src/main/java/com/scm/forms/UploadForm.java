package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.validfile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class UploadForm {

    @NotBlank(message="Name column is required")
    private String name;

    @NotBlank(message = "Email column is required")
    private String email;

    @NotBlank(message = "phone number column is required")
    private String phoneNumber;

    @NotBlank(message = "Address column is required")
    private String address;

    private String description;

    private String websiteLink;

    private String linkedInLink;

    @validfile
    private MultipartFile excelFile;


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

    public MultipartFile getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(MultipartFile excelFile) {
        this.excelFile = excelFile;
    }

    @Override
    public String toString() {
        return "UploadForm [name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber + ", address=" + address
                + ", description=" + description + ", websiteLink=" + websiteLink + ", linkedInLink=" + linkedInLink
                + ", contactImage=" + excelFile + "]";
    }

    
}
