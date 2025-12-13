package com.app.relief.controller;

import com.app.relief.dto.project.CreateProjectRequest;
import com.app.relief.dto.project.CreateProjectResponse;
import com.app.relief.dto.project.ProjectDetailsDto;
import com.app.relief.dto.project.ProjectSummaryDto;
import com.app.relief.dto.task.CreateTaskRequest;
import com.app.relief.dto.task.CreateTaskResponse;
import com.app.relief.entity.User;
import com.app.relief.service.ProjectService;
import com.app.relief.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService , TaskService taskService){
         this.projectService = projectService;
         this.taskService = taskService;
    }

    @GetMapping("/summaries")
    @PreAuthorize("isAuthenticated()")
    public List<ProjectSummaryDto> getProjectsSummaries(){
        return projectService.getProjectsSummaries();
    }

    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    public List<ProjectDetailsDto> getProjectsDetails(){
        return projectService.getProjectsDetails();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateProjectResponse> createNewProject(@RequestBody CreateProjectRequest request, @AuthenticationPrincipal User user){
         try {
             CreateProjectResponse response =  projectService.createNewProject(request , user);
             return ResponseEntity.ok(response);
         }catch (Exception e){
             return ResponseEntity.badRequest().body(new CreateProjectResponse(e.getMessage()));
         }
    }

    @PostMapping("/{projectId}/tasks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateTaskResponse> createNewTaskForProject(@RequestBody CreateTaskRequest request, @PathVariable Long projectId , @AuthenticationPrincipal User user){
        try {
            CreateTaskResponse response = taskService.createTaskForProject(request , projectId , user);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new CreateTaskResponse(e.getMessage()));
        }
    }

}
