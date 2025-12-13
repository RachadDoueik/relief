package com.app.relief.dto.attachment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {

    private Long id;

    private String fileName;

    private String fileUrl;

    private double fileSize;

    private LocalDateTime createdAt;
}
