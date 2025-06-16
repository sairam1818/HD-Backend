package com.tmt.ticketsystem.ticket.service;

import com.tmt.ticketsystem.ticket.converters.attachment.AttachmentCompressAndDecompressor;
import com.tmt.ticketsystem.ticket.model.entity.Attachment;
import com.tmt.ticketsystem.ticket.repository.AttachmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AttachmentService {
    private AttachmentRepository attachmentRepository;

    public Attachment saveFile(MultipartFile attachment) throws IOException {
        Attachment insertFile = attachmentRepository.save(Attachment.builder()
                .name(attachment.getOriginalFilename())
                .type(attachment.getContentType())
                .file(AttachmentCompressAndDecompressor.compressImage(attachment.getBytes())).build());
        return insertFile;
    }

    public byte[] getFile(Long id) {
        Optional<Attachment> insertFile = attachmentRepository.findById(id);
        byte[] image = AttachmentCompressAndDecompressor.decompressImage(insertFile.get().getFile());
        return image;
    }
}
