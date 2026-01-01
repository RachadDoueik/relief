package com.app.relief.dto.project;

import com.app.relief.dto.task.TaskSummaryDto;
import com.app.relief.enums.ProjectKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetailsDto {

    private Long id;

    private String name;

    private ProjectKey projectKey;

    private Set<TaskSummaryDto> tasks;
}
