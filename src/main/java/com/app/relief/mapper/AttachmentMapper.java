package com.app.relief.mapper;


import com.app.relief.dto.attachment.AttachmentDto;
import com.app.relief.dto.user.UserSummaryDto;
import com.app.relief.entity.Attachment;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapper {

    //attachment to attachmentDto
    public AttachmentDto attachmentToAttachmentDto(Attachment attachment){

        if(attachment == null) return null;

        AttachmentDto ad = new AttachmentDto();
        ad.setId(attachment.getId());
        ad.setFileName(attachment.getFileName());
        ad.setFileSize(attachment.getFileSize());
        ad.setFileUrl(attachment.getFileUrl());
        ad.setCreatedAt(attachment.getCreatedAt());
        ad.setUploadedBy(new UserSummaryDto(attachment.getUploadedBy().getUsername() , attachment.getUploadedBy().getEmail()));

        return ad;
    }

}
