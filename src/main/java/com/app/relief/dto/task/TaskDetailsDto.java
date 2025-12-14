package com.app.relief.dto.task;


import com.app.relief.dto.attachment.AttachmentDto;
import com.app.relief.dto.comment.CommentDto;
import com.app.relief.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDetailsDto {

    private Long id;

    private String taskName;

    private String description;

    private TaskStatus taskStatus;

    private Set<AttachmentDto> attachments;

    private Set<CommentDto> comments;

}
