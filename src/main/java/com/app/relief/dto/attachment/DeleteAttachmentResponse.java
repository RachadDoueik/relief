package com.app.relief.dto.attachment;


import lombok.Data;

@Data
public class DeleteAttachmentResponse {

    private String message;

    public DeleteAttachmentResponse(String message) {
        this.message = message;
    }
}
