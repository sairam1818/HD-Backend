package com.tmt.ticketsystem.ticket.api;


import com.tmt.ticketsystem.ticket.model.entity.Attachment;
import com.tmt.ticketsystem.ticket.service.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/save-file")
    private Attachment uploadFile(@RequestParam("image") MultipartFile attachment) throws IOException {
        Attachment uploadImage = attachmentService.saveFile(attachment);
        return uploadImage;
    }

    @GetMapping("/get-file/{id}")
    private ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        byte[] insertFile = attachmentService.getFile(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(insertFile);
    }
}
