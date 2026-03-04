package com.tmt.ticketsystem.ticket.service;

import com.tmt.ticketsystem.Factory.TestDataFactory;
import com.tmt.ticketsystem.ticket.converters.attachment.AttachmentCompressAndDecompressor;
import com.tmt.ticketsystem.ticket.model.entity.Attachment;
import com.tmt.ticketsystem.ticket.repository.AttachmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttachmentServiceTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private AttachmentService attachmentService;

    // ======================== saveFile ========================

    @Nested
    @DisplayName("saveFile Tests")
    class SaveFileTests {

        @Test
        @DisplayName("Positive: Should save file with correct name, type and compressed data")
        void saveFile_ShouldSaveAndReturnAttachment() throws IOException {
            // Arrange
            MultipartFile mockFile = mock(MultipartFile.class);
            byte[] originalBytes = "Hello, World!".getBytes();
            byte[] compressedBytes = AttachmentCompressAndDecompressor.compressImage(originalBytes);

            when(mockFile.getOriginalFilename()).thenReturn("test_image.png");
            when(mockFile.getContentType()).thenReturn("image/png");
            when(mockFile.getBytes()).thenReturn(originalBytes);

            Attachment savedAttachment = Attachment.builder()
                    .id(1L)
                    .name("test_image.png")
                    .type("image/png")
                    .file(compressedBytes)
                    .build();

            when(attachmentRepository.save(any(Attachment.class))).thenReturn(savedAttachment);

            // Act
            Attachment result = attachmentService.saveFile(mockFile);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("test_image.png", result.getName());
            assertEquals("image/png", result.getType());
            assertNotNull(result.getFile());
            verify(attachmentRepository, times(1)).save(any(Attachment.class));
        }

        @Test
        @DisplayName("Positive: Should handle PDF file type correctly")
        void saveFile_ShouldHandlePdfFileType() throws IOException {
            MultipartFile mockFile = mock(MultipartFile.class);
            byte[] pdfBytes = "PDF content".getBytes();
            byte[] compressedBytes = AttachmentCompressAndDecompressor.compressImage(pdfBytes);

            when(mockFile.getOriginalFilename()).thenReturn("document.pdf");
            when(mockFile.getContentType()).thenReturn("application/pdf");
            when(mockFile.getBytes()).thenReturn(pdfBytes);

            Attachment savedAttachment = Attachment.builder()
                    .id(2L).name("document.pdf").type("application/pdf").file(compressedBytes).build();

            when(attachmentRepository.save(any(Attachment.class))).thenReturn(savedAttachment);

            Attachment result = attachmentService.saveFile(mockFile);

            assertEquals("document.pdf", result.getName());
            assertEquals("application/pdf", result.getType());
        }

        @Test
        @DisplayName("Edge: Should handle file with null original filename")
        void saveFile_ShouldHandleNullFilename() throws IOException {
            MultipartFile mockFile = mock(MultipartFile.class);
            byte[] bytes = new byte[] { 1, 2, 3 };

            when(mockFile.getOriginalFilename()).thenReturn(null);
            when(mockFile.getContentType()).thenReturn(null);
            when(mockFile.getBytes()).thenReturn(bytes);

            Attachment savedAttachment = Attachment.builder()
                    .id(3L).name(null).type(null)
                    .file(AttachmentCompressAndDecompressor.compressImage(bytes)).build();

            when(attachmentRepository.save(any(Attachment.class))).thenReturn(savedAttachment);

            Attachment result = attachmentService.saveFile(mockFile);

            assertNotNull(result);
            assertNull(result.getName());
            assertNull(result.getType());
        }

        @Test
        @DisplayName("Negative: Should propagate IOException when getBytes() throws")
        void saveFile_ShouldThrowIOException_WhenGetBytesFails() throws IOException {
            MultipartFile mockFile = mock(MultipartFile.class);
            when(mockFile.getBytes()).thenThrow(new IOException("Read error"));

            assertThrows(IOException.class, () -> attachmentService.saveFile(mockFile));

            verify(attachmentRepository, never()).save(any());
        }

        @Test
        @DisplayName("Edge: Should save file with empty byte array")
        void saveFile_ShouldHandleEmptyByteArray() throws IOException {
            MultipartFile mockFile = mock(MultipartFile.class);
            byte[] emptyBytes = new byte[0];
            byte[] compressed = AttachmentCompressAndDecompressor.compressImage(emptyBytes);

            when(mockFile.getOriginalFilename()).thenReturn("empty.txt");
            when(mockFile.getContentType()).thenReturn("text/plain");
            when(mockFile.getBytes()).thenReturn(emptyBytes);

            Attachment savedAttachment = Attachment.builder()
                    .id(4L).name("empty.txt").type("text/plain").file(compressed).build();

            when(attachmentRepository.save(any(Attachment.class))).thenReturn(savedAttachment);

            Attachment result = attachmentService.saveFile(mockFile);

            assertNotNull(result);
            assertEquals("empty.txt", result.getName());
        }

        @Test
        @DisplayName("Edge: Should handle large file content")
        void saveFile_ShouldHandleLargeFile() throws IOException {
            MultipartFile mockFile = mock(MultipartFile.class);
            byte[] largeBytes = new byte[1024 * 1024]; // 1 MB of zeros
            byte[] compressed = AttachmentCompressAndDecompressor.compressImage(largeBytes);

            when(mockFile.getOriginalFilename()).thenReturn("large_file.bin");
            when(mockFile.getContentType()).thenReturn("application/octet-stream");
            when(mockFile.getBytes()).thenReturn(largeBytes);

            Attachment savedAttachment = Attachment.builder()
                    .id(5L).name("large_file.bin").type("application/octet-stream").file(compressed).build();

            when(attachmentRepository.save(any(Attachment.class))).thenReturn(savedAttachment);

            Attachment result = attachmentService.saveFile(mockFile);

            assertNotNull(result);
            assertEquals("large_file.bin", result.getName());
        }

        @Test
        @DisplayName("Positive: Saved file should have compressed data different from original")
        void saveFile_SavedDataShouldBeCompressed() throws IOException {
            MultipartFile mockFile = mock(MultipartFile.class);
            byte[] originalBytes = "This is test data for compression verification".getBytes();

            when(mockFile.getOriginalFilename()).thenReturn("test.txt");
            when(mockFile.getContentType()).thenReturn("text/plain");
            when(mockFile.getBytes()).thenReturn(originalBytes);

            // Capture the attachment that is saved to the repository
            when(attachmentRepository.save(any(Attachment.class))).thenAnswer(invocation -> {
                Attachment saved = invocation.getArgument(0);
                saved.setId(10L);
                return saved;
            });

            Attachment result = attachmentService.saveFile(mockFile);

            assertNotNull(result.getFile());
            // Compressed data can be verified by decompressing it
            byte[] decompressed = AttachmentCompressAndDecompressor.decompressImage(result.getFile());
            assertArrayEquals(originalBytes, decompressed);
        }
    }

    // ======================== getFile ========================

    @Nested
    @DisplayName("getFile Tests")
    class GetFileTests {

        @Test
        @DisplayName("Positive: Should return decompressed file data when attachment found")
        void getFile_ShouldReturnDecompressedData_WhenFound() {
            // Arrange: compress data first, then expect decompression on retrieval
            byte[] originalData = "Test file content".getBytes();
            byte[] compressedData = AttachmentCompressAndDecompressor.compressImage(originalData);

            Attachment attachment = TestDataFactory.buildTestAttachmentWithCompressedData(compressedData);
            when(attachmentRepository.findById(1L)).thenReturn(Optional.of(attachment));

            // Act
            byte[] result = attachmentService.getFile(1L);

            // Assert
            assertNotNull(result);
            assertArrayEquals(originalData, result);
            verify(attachmentRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Negative: Should throw NoSuchElementException when attachment not found")
        void getFile_ShouldThrowException_WhenAttachmentNotFound() {
            when(attachmentRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(NoSuchElementException.class, () -> attachmentService.getFile(999L));
        }

        @Test
        @DisplayName("Positive: Should handle attachment with empty compressed data")
        void getFile_ShouldHandleEmptyCompressedData() {
            byte[] emptyOriginal = new byte[0];
            byte[] compressed = AttachmentCompressAndDecompressor.compressImage(emptyOriginal);
            Attachment attachment = TestDataFactory.buildTestAttachmentWithCompressedData(compressed);

            when(attachmentRepository.findById(1L)).thenReturn(Optional.of(attachment));

            byte[] result = attachmentService.getFile(1L);

            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        @DisplayName("Positive: Should correctly decompress image data")
        void getFile_ShouldCorrectlyDecompressImageData() {
            byte[] imageData = new byte[] { (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A };
            byte[] compressed = AttachmentCompressAndDecompressor.compressImage(imageData);
            Attachment attachment = TestDataFactory.buildTestAttachmentWithCompressedData(compressed);

            when(attachmentRepository.findById(1L)).thenReturn(Optional.of(attachment));

            byte[] result = attachmentService.getFile(1L);

            assertArrayEquals(imageData, result);
        }

        @Test
        @DisplayName("Edge: Should work with large decompressed data")
        void getFile_ShouldHandleLargeData() {
            byte[] largeData = new byte[1024 * 100]; // 100 KB
            for (int i = 0; i < largeData.length; i++) {
                largeData[i] = (byte) (i % 256);
            }
            byte[] compressed = AttachmentCompressAndDecompressor.compressImage(largeData);
            Attachment attachment = TestDataFactory.buildTestAttachmentWithCompressedData(compressed);

            when(attachmentRepository.findById(1L)).thenReturn(Optional.of(attachment));

            byte[] result = attachmentService.getFile(1L);

            assertArrayEquals(largeData, result);
        }

        @Test
        @DisplayName("Negative: Should throw NoSuchElementException when id is null")
        void getFile_ShouldThrowException_WhenIdIsNull() {
            when(attachmentRepository.findById(null)).thenReturn(Optional.empty());

            assertThrows(NoSuchElementException.class, () -> attachmentService.getFile(null));
        }

        @Test
        @DisplayName("Positive: Should call findById exactly once")
        void getFile_ShouldCallFindByIdOnce() {
            byte[] data = "Verify call count".getBytes();
            byte[] compressed = AttachmentCompressAndDecompressor.compressImage(data);
            Attachment attachment = TestDataFactory.buildTestAttachmentWithCompressedData(compressed);

            when(attachmentRepository.findById(42L)).thenReturn(Optional.of(attachment));

            attachmentService.getFile(42L);

            verify(attachmentRepository, times(1)).findById(42L);
        }
    }
}
