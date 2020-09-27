package com.tiny.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class FileService {

    @Value("${drive.base.folder}")
    private String driveBaseFolder;

    private void fileUploader(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();

        Path fileToSave = Paths.get(driveBaseFolder, filename);
        file.transferTo(fileToSave.toFile());

    }

    private void saveMultipleFiles(MultipartFile[] files) throws IOException{
        for(MultipartFile file : files) fileUploader(file);
    }

    public void saveIndividualFile(MultipartFile file) throws IOException {
        fileUploader(file);
    }

}
