package com.app.relief.dto.task;

import com.app.relief.enums.TaskPriority;
import com.app.relief.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskRequest {

    @NotNull
    private String taskName;

    private String taskDescription;

    @NotNull
    private Long assignedUserId;

    @NotNull
    private TaskStatus taskStatus;

    @NotNull
    private TaskPriority taskPriority;

    private LocalDateTime taskDueDate;

}
