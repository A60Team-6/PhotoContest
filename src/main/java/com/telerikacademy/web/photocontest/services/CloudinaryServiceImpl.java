package com.telerikacademy.web.photocontest.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.telerikacademy.web.photocontest.services.contracts.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/*ToDo you do not need service if you will have only one method which will just upload photo... */
@Service
@AllArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
/*This method has a lot of cons...First you are using raw Map which is not okay, if you want to use raw Map
* better to use var if you do not know what is that check, then you are printing with System.err.println? WHy??*/
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        System.out.println("Uploading file: " + file.getOriginalFilename());
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            System.out.println("Upload result: " + uploadResult);
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            System.err.println("Failed to upload file: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to upload photo", e);
        }
    }

    /* Check this method and determine if you understand it and if its a better choice*/

//    public String uploadFile(MultipartFile file) throws IOException {
//        log.info("Uploading file: {}", file.getOriginalFilename());
//        try {
//            var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//            log.debug("Upload result: {}", uploadResult);
//
//            return Optional.ofNullable((String) uploadResult.get("url"))
//                    .orElseThrow(() -> new FileUploadException("URL not found in upload result"));
//        } catch (IOException e) {
//            log.error("Failed to upload file: {}", e.getMessage(), e);
//            throw new FileUploadException("Failed to upload photo", e);
//        }
//    }



}
