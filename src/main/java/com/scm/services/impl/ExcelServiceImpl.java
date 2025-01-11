package com.scm.services.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.services.ContactService;
import com.scm.services.ExcelService;

import jakarta.mail.Multipart;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ExcelServiceImpl implements ExcelService {

    private ContactService contactService;

    private Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

    
    @Autowired
    public ExcelServiceImpl(ContactService contactService) {
        this.contactService = contactService;
    }



    public void readAndSaveExcel(MultipartFile file, Map<String, String> map, User user) {

        try {
            try (InputStream inputStream = file.getInputStream();
                    Workbook workbook = new XSSFWorkbook(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

                Map<Integer, String> mapp = new HashMap<>();
                // Set<String> set = map.keySet();
                Row row = sheet.getRow(0);
                System.out.println(row.getLastCellNum());
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    System.out.println(row.getCell(i).getStringCellValue());
                    if (map.get(row.getCell(i).getStringCellValue()) != null) {
                        mapp.put(i, map.get(row.getCell(i).getStringCellValue()));
                    }
                }
                System.out.println(mapp);
                DataFormatter formatter = new DataFormatter(); //function inside this will be used to retrieve phone number from excel

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row1 = sheet.getRow(i);

                    Contact contact = new Contact();
                    Set<Integer> set = mapp.keySet();
                    contact.setUser(user);

                    for (Iterator<Integer> it = set.iterator(); it.hasNext();) {
                        int n = it.next();
                        switch (mapp.get(n)) {
                            case "name":
                                contact.setName(row1.getCell(n).getStringCellValue());
                                break;
                            case "email":
                                contact.setEmail(row1.getCell(n).getStringCellValue());
                                break;
                            case "phoneNumber":
                            
                                contact.setPhoneNumber(formatter.formatCellValue(row1.getCell(n)));
                                
                                break;
                            case "address":
                                contact.setAddress(row1.getCell(n).getStringCellValue());
                                break;
                            case "description":
                                contact.setDescription(row1.getCell(n).getStringCellValue());
                                break;
                            case "websiteLink":
                                contact.setWebsiteLink(row1.getCell(n).getStringCellValue());
                                break;
                            case "linkedInLink":
                                contact.setLinkedInLink(row1.getCell(n).getStringCellValue());
                                break;
                        }

                    }
                    logger.info(contact.toString());
                    contactService.save(contact);
                }

                // for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
                // Row row = sheet.getRow(i);

                // int id = (int) row.getCell(0).getNumericCellValue();
                // String name = row.getCell(1).getStringCellValue();
                // double salary = row.getCell(2).getNumericCellValue();

                // employees.add(new Employee(id, name, salary));
                // }
            }
            // return employees;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}