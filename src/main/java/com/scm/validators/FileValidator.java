package com.scm.validators;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<validfile, MultipartFile> {


    private static final long MAX_FILE_SIZE = 1024 * 1024 * 2;

    //type

    //height

    //width

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        
        if(file==null || file.isEmpty()) {
            // context.disableDefaultConstraintViolation();
            // context.buildConstraintViolationWithTemplate("File cannot be Empty").addConstraintViolation();

            return true;
        }
        if(file.getSize()>MAX_FILE_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size should be less than 2MB").addConstraintViolation();
            return false;
        }

        //resolution
        // try {
        //     BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            //with the help of buffered image we can check height, width, resolution
        //     if(bufferedImage.get)
            
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        return true;
    }

}
