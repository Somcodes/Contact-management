package com.scm.services;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.scm.entities.User;

public interface ExcelService {
    void readAndSaveExcel(MultipartFile file, Map<String, String> map, User user);
}
