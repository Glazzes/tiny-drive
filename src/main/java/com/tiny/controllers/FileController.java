package com.tiny.controllers;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import com.tiny.services.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
public class FileController {

    private FileService filesService;
    public FileController(FileService filesService){
        this.filesService = filesService;
    }

    @PostMapping
    public ResponseEntity<?> fileSingleFile(@RequestPart("file") MultipartFile file){
        try{
            filesService.saveIndividualFile(file);
            return ResponseEntity.ok().build();
        }catch(IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(
            @RequestParam(name = "folder", required = false, defaultValue = "") String folder,
            @RequestParam(name = "filename", required = true, defaultValue = "") String filename,
            HttpServletResponse response
    ){
        try{
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            Path file = Paths.get("/home/glaze/Projects/tinyDrive/Drive/", folder, filename);
            outputStream.write(Files.readAllBytes(file));
            outputStream.flush();
            outputStream.close();

            return ResponseEntity.ok().body("File downloaded!");
        }catch (IOException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
