package com.tutorial.apidemo.service;

import ch.qos.logback.classic.spi.TurboFilterList;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService {
    private final Path storageFolder;

    // constructor
    public ImageStorageService() {
        this.storageFolder = Paths.get("uploads");
        try {
            Files.createDirectories(storageFolder);
        }catch (IOException e) {
            throw new RuntimeException("Cannot initialize storage", e);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png", "jpg", "jpeg", "gif", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("haha");
            if(file.isEmpty()) {
                throw new RuntimeException("Failed to store file " + file.getOriginalFilename());
            }
            if(!isImageFile(file)) {
                throw new RuntimeException("You can only upload image files");
            }

            float fileSize = file.getSize() / 1_000_000.0f;
            if(fileSize > 5.0f) {
                throw new RuntimeException("File must be < 5mb");
            }

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(
                    Paths.get(generatedFileName))
                            .normalize().toAbsolutePath();
            if(!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside curretn directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return generatedFileName;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return  Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder))
                    .map(this.storageFolder::relativize);
        }catch (IOException e) {
            throw new RuntimeException("Failed to load all files", e);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else {
                throw new RuntimeException("Could not read file: " + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file " + fileName, e);
        }
    }

    @Override
    public void deleteAllFile(String fileName) {

    }
}
