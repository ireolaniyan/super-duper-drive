package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping()
    public String getFilesPage(Authentication auth, Model model) {
        model.addAttribute("files", fileService.getFiles(auth));
        return "home";
    }

    @PostMapping("/upload")
    public String postFile(Authentication auth, MultipartFile fileUpload, Model model) throws IOException {

        if (!fileService.isValidFilename(auth, fileUpload)) {
            String fileUploadError = "File already exists. Rename file.";
            model.addAttribute("fileUploadError", fileUploadError);
        } else {
            fileService.addFile(auth, fileUpload);
        }

        model.addAttribute("files", fileService.getFiles(auth));
        return "home";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity downloadFile(@PathVariable("fileId") int fileId) throws IOException {
        File file = fileService.getFileById(fileId);

        String contentType = file.getContentType();
        String fileName = file.getFileName();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file.getFileData());

    }
}
