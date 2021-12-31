package com.sustechstore.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class FileTransferUtils {
    public static String upload(MultipartFile file) throws IOException {
        String name = "";
        Path tmp = Paths.get("src/main/resources/static");
        File target = tmp.toFile();
        if(!target.exists()){
            target.mkdirs();
        }
        name = file.getOriginalFilename();
        name = name.substring(name.lastIndexOf("."),name.length());
        name = HashUtils.SHA256((new Date().toString())+file.getOriginalFilename())+name;
        target = new File(target.getPath()+"/" +name);
        if (!target.exists()) {
            try {
                target.createNewFile();
            } catch (IOException e) {
                throw e;
            }
        }
        System.out.println(target.getAbsolutePath());
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),target);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return name;
    }
}
