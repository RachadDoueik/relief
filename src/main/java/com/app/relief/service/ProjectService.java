package com.app.relief.service;

import com.app.relief.dto.project.CreateProjectRequest;
import com.app.relief.dto.project.CreateProjectResponse;
import com.app.relief.dto.project.ProjectDetailsDto;
import com.app.relief.dto.project.ProjectSummaryDto;
import com.app.relief.entity.Project;
import com.app.relief.entity.User;
import com.app.relief.mapper.ProjectMapper;
import com.app.relief.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper){
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public List<ProjectSummaryDto> getProjectsSummaries() {

        List<Project> projects = projectRepository.findAll();

            return projects.stream()
                    .map(projectMapper::projectToProjectSummaryDto)
                    .toList();
    }

    public List<ProjectDetailsDto> getProjectsDetails() {

        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(projectMapper::projectToProjectDetailsDto).toList();
    }

    public CreateProjectResponse createNewProject(CreateProjectRequest request, User user) {

          Project project = new Project();
          project.setProjectName(request.getProjectName());
          project.setProjectKey(request.getProjectKey());
          project.setProjectDescription(request.getProjectDescription());
          project.setOwner(user);

          projectRepository.save(project);

          return new CreateProjectResponse("Project '" + project.getProjectName() + "' created successfully !");

    }
}
