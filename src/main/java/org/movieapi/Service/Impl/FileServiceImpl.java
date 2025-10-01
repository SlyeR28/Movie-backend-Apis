package org.movieapi.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.movieapi.Service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {

        // get name of the file
        String fileName = file.getOriginalFilename();

        // get the file path
        String filePath = path + File.separator + fileName;

         // create file object
        File f = new File(path);
        if(!f.exists()) {
            f.mkdir();
        }
        //copy the file  or upload file to the path
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        return new FileInputStream(filePath);
    }

    @Override
    public void deleteFile(String uploadDir, String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) return;

        Path filePath = Paths.get(uploadDir, fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath); // Deletes the file
        } else {
            System.out.println("File not found on server: " + filePath.toString());
        }
    }
}
