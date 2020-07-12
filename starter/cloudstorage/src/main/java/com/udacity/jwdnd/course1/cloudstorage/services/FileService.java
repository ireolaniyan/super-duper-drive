package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;
    private UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("creating FileService bean");
    }

    public boolean isValidFilename(Authentication auth, MultipartFile uploadedFile) {
        User user = userMapper.getUser(auth.getName());
        return fileMapper.getFile(user.getUserId(), uploadedFile.getOriginalFilename()) == null;
    }

    public void addFile(Authentication auth, MultipartFile uploadedFile) throws IOException {
        User user = userMapper.getUser(auth.getName());

        File file = new File();
        file.setFileName(uploadedFile.getOriginalFilename());
        file.setContentType(uploadedFile.getContentType());
        file.setFileSize(Long.toString(uploadedFile.getSize()));
        file.setFileData(uploadedFile.getBytes());
        file.setUserId(user.getUserId());

        fileMapper.addFile(file);
    }

    public List<File> getFiles(Authentication auth) {
        User user = userMapper.getUser(auth.getName());
        return fileMapper.getUserFiles(user.getUserId());
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void deleteFile(int fileId) {
        fileMapper.deleteFile(fileId);
    }
}
