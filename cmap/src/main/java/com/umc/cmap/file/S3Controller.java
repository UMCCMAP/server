package com.umc.cmap.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {

    private final AwsS3Service awsS3Service;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/file")
    public List<URL> upload(@RequestPart List<MultipartFile> multipartFile) {
        return awsS3Service.uploadFile(multipartFile);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/file")
    public void delete(@RequestParam String fileName) {
        awsS3Service.deleteFile(fileName);
    }
}
