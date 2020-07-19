package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping()
    public ModelAndView getFilesPage(Authentication auth, ModelMap model) {
        model.addAttribute("files", fileService.getFiles(auth));
        return new ModelAndView("home", model);
    }

    @PostMapping("/upload")
    public ModelAndView postFile(Authentication auth, MultipartFile fileUpload, ModelMap model) throws IOException {
        String message;
        if (fileUpload.isEmpty()){
            message = "Please select a file. Can't upload null/empty file.";
            model.addAttribute("error", message);
            model.addAttribute("files", fileService.getFiles(auth));
            return new ModelAndView("result", model);
        } else if (!fileService.isValidFilename(auth, fileUpload)) {
            message = "File already exists. Rename file.";
            model.addAttribute("error", message);
            model.addAttribute("files", fileService.getFiles(auth));
            return new ModelAndView("result", model);
        }

        fileService.addFile(auth, fileUpload);
        message = "File successfully uploaded.";
        model.addAttribute("files", fileService.getFiles(auth));
        model.addAttribute("success", message);
        return new ModelAndView("result", model);

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

    @GetMapping("/delete/{fileId}")
    public ModelAndView deleteFile(@PathVariable("fileId") int fileId, Authentication auth, ModelMap model) {
        String message = "Successfully deleted file.";
        fileService.deleteFile(fileId);
        model.addAttribute("successfulDelete", message);
        model.addAttribute("files", fileService.getFiles(auth));
        return new ModelAndView("result", model);
    }
}
