package com.app.relief.service;


import com.app.relief.dto.attachment.CreateAttachmentResponse;
import com.app.relief.dto.attachment.DeleteAttachmentResponse;
import com.app.relief.entity.Attachment;
import com.app.relief.entity.Task;
import com.app.relief.entity.User;
import com.app.relief.exception.TaskNotFoundException;
import com.app.relief.exception.TaskOwnershipException;
import com.app.relief.repository.AttachmentRepository;
import com.app.relief.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final Path uploadPath;

    private static final List<String> ALLOWED_TYPES = List.of(
            "application/pdf",
            "image/png",
            "image/jpeg",
            "image/gif",
            "text/plain",
            "application/msword", // .doc
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
            "application/vnd.ms-powerpoint", // .ppt
            "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .pptx
            "application/rtf",
            "application/vnd.oasis.opendocument.text", // .odt
            "application/vnd.ms-excel", // .xls
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xlsx
    );


    // Constructor injection for repositories and upload dir
    public AttachmentService(AttachmentRepository attachmentRepository,
                             TaskRepository taskRepository,
                             @Value("${app.upload-dir}") String uploadDir) {
        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;

        // Initialize the Path safely
        this.uploadPath = Paths.get(uploadDir);
        System.out.println("Upload path: " + uploadPath.toAbsolutePath());
        System.out.println("Writable? " + Files.isWritable(uploadPath));

        // Ensure the directory exists
        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + uploadDir, e);
        }
    }

    public CreateAttachmentResponse storeAttachment(Long taskId, MultipartFile file , User user) {

        validateFile(file);

        String originalFilename = Paths.get(Objects.requireNonNull(file.getOriginalFilename())).getFileName().toString();
        String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;

        try {
            Path filePath = uploadPath.resolve(uniqueFilename);
            System.out.println("Copying file to: " + filePath.toAbsolutePath());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully");

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found"));

            if(!task.getCreatedBy().getId().equals(user.getId())){
                throw new TaskOwnershipException("Unauthorized to add attachment to this task");
            }

            Attachment attachment = new Attachment();
            attachment.setFileName(originalFilename);
            attachment.setFileUrl(filePath.toString());
            attachment.setFileType(file.getContentType());
            attachment.setFileSize(file.getSize());
            attachment.setTask(task);
            attachment.setUploadedBy(user);

            attachmentRepository.save(attachment);
            System.out.println("Attachment saved with id: " + attachment.getId());

            return new CreateAttachmentResponse("File with id " + attachment.getId() + " uploaded successfully");

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public DeleteAttachmentResponse deleteAttachmentById(Long attachmentId, User user){

        Attachment attachmentToDelete = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        if(!attachmentToDelete.getUploadedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this attachment");
        }

        try {
            Path filePath = Paths.get(attachmentToDelete.getFileUrl());
            Files.deleteIfExists(filePath);
            attachmentRepository.delete(attachmentToDelete);
            return new DeleteAttachmentResponse("Attachment deleted successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from storage", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // 5MB
        long MAX_FILE_SIZE = 10 * 1024 * 1024;

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File exceeds max size of 5MB");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new RuntimeException("Unsupported file type");
        }
    }
}
