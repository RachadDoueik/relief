package com.app.relief.controller;

import com.app.relief.dto.attachment.DeleteAttachmentResponse;
import com.app.relief.entity.User;
import com.app.relief.exception.AttachmentOwnershipException;
import com.app.relief.service.AttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {


    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService){
        this.attachmentService = attachmentService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteAttachmentResponse> deleteAttachmentById(@PathVariable Long id , @AuthenticationPrincipal User user) {
        try {
            DeleteAttachmentResponse response =  attachmentService.deleteAttachmentById(id , user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new DeleteAttachmentResponse(e.getMessage()));
        }
    }


}
