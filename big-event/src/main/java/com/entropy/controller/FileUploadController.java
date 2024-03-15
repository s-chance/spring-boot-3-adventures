package com.entropy.controller;

import com.entropy.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        // 把文件的内容存储到本地磁盘上
        String originalFilename = file.getOriginalFilename();
        // 保证文件名唯一，防止覆盖
        assert originalFilename != null;
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        file.transferTo(new File("/home/entropy/" + filename));
        return Result.success("url 地址...");
    }
}
