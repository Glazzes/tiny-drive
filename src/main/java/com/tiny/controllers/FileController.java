package com.tiny.controllers;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@CrossOrigin("*")
public class FileController {

    @Value("${drive.base.folder}")
    private String driveBaseFolder;

    private void fileUploader( MultipartFile file ) throws IOException{
        Path incomingFile = Paths.get(driveBaseFolder, file.getOriginalFilename());
        Files.write(incomingFile, file.getBytes());
    }
    
    @PostMapping( consumes = "multipart/form-data" )
    public ResponseEntity<String> singleFileUpload(
        @RequestParam("file") MultipartFile file 
    ) throws IOException{

        try {
            fileUploader(file);
        } catch (IOException e) {
            return new ResponseEntity<>(
                "Something went wrgon while uploading your file, Error: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return new ResponseEntity<String>("File uploaded successfuly", HttpStatus.CREATED);
    }


    // @PreAuthorize( "isAuthenticated()" )
    @PostMapping( path = "/multiple", consumes = "multipart/form-data" )
    public ResponseEntity<?> multipleFileUpload(
        @RequestParam("files") MultipartFile[] files
    ) throws IOException{

        try {
            for( MultipartFile file : files ){ fileUploader(file);}
        } catch (IOException e) {
            return new ResponseEntity<>(
                "Something went wrong while uploading your files, Error: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/download")
    public @ResponseBody String userFilesDownload(
        @RequestParam(name="folder", defaultValue = "", required = false) String folder,
        @RequestParam( name = "filename", defaultValue = "", required = true ) String filename,
        HttpServletResponse response
    ){
        response.setHeader("Content-Disposition", "attachmnet; filename=" + filename);

        try {
            BufferedOutputStream outputStream = new BufferedOutputStream( response.getOutputStream() );
            Path fileFromDrive = Paths.get( driveBaseFolder, folder, filename );

            outputStream.write( Files.readAllBytes(fileFromDrive) );
            outputStream.flush();
            outputStream.close();

            return "Here you go,the file you were looking for";
        }catch(Exception e) {
            System.out.println( "We're sorry we couldn't find the file you were looking for, Error: " + e.getMessage() );
            return e.getMessage();
        }
    }
}
