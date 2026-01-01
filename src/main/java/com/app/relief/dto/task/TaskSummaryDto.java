package com.app.relief.dto.task;


import com.app.relief.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSummaryDto {

    private Long id;

    private String name;

    private String description;

    private TaskStatus taskStatus;
}
