package org.movieapi.Controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.movieapi.Service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileControllers {

    private final FileService fileService;


    @Value("${project.poster}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file) throws IOException {
        if(file.getResource().exists()){
            throw new FileAlreadyExistsException("File already exists");
        }
        String uploadFile = fileService.uploadFile(path, file);
       return ResponseEntity.status(HttpStatus.CREATED).body(uploadFile);
    }

    @GetMapping("/{fileName}")
    public void downloadFile(@PathVariable String fileName , HttpServletResponse response) throws IOException {
        InputStream resourceFile = fileService.getResourceFile(path, fileName);
          response.setContentType(MediaType.IMAGE_PNG_VALUE);
           StreamUtils.copy(resourceFile, response.getOutputStream());
    }

}
