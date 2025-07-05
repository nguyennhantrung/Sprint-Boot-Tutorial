package com.tutorial.apidemo.controllers;

import com.tutorial.apidemo.models.ResponseObject;
import com.tutorial.apidemo.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/api/v1/FileUpload")
public class FileUploadController {
    @Autowired
    private ImageStorageService storageService;
    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // save file to a folder => use a service
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "upload file successfully", generatedFileName));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", e.getMessage(), "")
            );
        }
    }


    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
        try {
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getUploadedFiles() {
        try {
            List<String> urls = storageService.loadAll()
                .map(path -> {
                    String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "readDetailFile",
                            path.getFileName().toString()).build().toUri().toString();
                    return urlPath;
                })
                .collect(Collectors.toList());
            return  ResponseEntity.ok(new ResponseObject("OK", "List files successfully", urls));
        } catch (Exception e) {
            return  ResponseEntity.ok(new ResponseObject("ERROR", "List files failed", new String[] {}));

        }
    }
}
