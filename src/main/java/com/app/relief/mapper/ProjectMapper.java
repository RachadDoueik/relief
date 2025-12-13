package com.app.relief.mapper;

import com.app.relief.dto.project.ProjectDetailsDto;
import com.app.relief.dto.project.ProjectSummaryDto;
import com.app.relief.dto.task.TaskSummaryDto;
import com.app.relief.entity.Project;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    //converts a Project to a ProjectSummaryDto
    public ProjectSummaryDto projectToProjectSummaryDto(Project project){
        if(project == null){
            return null;
        }

        ProjectSummaryDto projectSummaryDto = new ProjectSummaryDto();
        projectSummaryDto.setId(project.getId());
        projectSummaryDto.setProjectName(project.getProjectName());
        projectSummaryDto.setProjectKey(project.getProjectKey());

        return projectSummaryDto;

    }

    //converts a Project to a ProjectDetailsDto
    public ProjectDetailsDto projectToProjectDetailsDto(Project project){

        if(project == null){
            return null;
        }

        ProjectDetailsDto projectDetailsDto = new ProjectDetailsDto();
        projectDetailsDto.setId(project.getId());
        projectDetailsDto.setName(project.getProjectName());
        projectDetailsDto.setProjectKey(project.getProjectKey());
        projectDetailsDto.setTasks(
                project.getTasks().stream()
                        .map(task -> new TaskSummaryDto(task.getId() , task.getTaskName() , task.getTaskDescription() , task.getStatus()))
                        .collect(Collectors.toSet())
        );

        return projectDetailsDto;
    }

}
