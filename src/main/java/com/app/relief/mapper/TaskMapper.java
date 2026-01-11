package com.app.relief.mapper;

import com.app.relief.dto.task.TaskDetailsDto;
import com.app.relief.dto.task.TaskSummaryDto;
import com.app.relief.entity.Task;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    //convert task to taskSummaryDto
    public TaskSummaryDto taskToTaskSummaryDto(Task task){

        if(task == null) return null;

        TaskSummaryDto tsd = new TaskSummaryDto();
        tsd.setId(task.getId());
        tsd.setName(task.getTaskName());
        tsd.setDescription(task.getTaskDescription());
        tsd.setTaskPriority(task.getPriority());
        tsd.setTaskStatus(task.getStatus());
        return tsd;
    }

    //convert task to taskDetailsDto
    public TaskDetailsDto taskToTaskDetailsDto(Task task){

        if(task == null) return null;

        TaskDetailsDto tdd = new TaskDetailsDto();
        AttachmentMapper am = new AttachmentMapper();
        CommentMapper cm = new CommentMapper();
        tdd.setId(task.getId());
        tdd.setTaskName(task.getTaskName());
        tdd.setDescription(task.getTaskDescription());
        tdd.setTaskStatus(task.getStatus());
        tdd.setAttachments(
                task.getAttachments().stream().map(am::attachmentToAttachmentDto).collect(Collectors.toSet())
        );
        tdd.setComments(
                task.getComments().stream().map(cm::commentToCommentDto).collect(Collectors.toSet())
        );

        return tdd;
    }
}
