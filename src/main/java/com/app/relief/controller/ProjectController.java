package com.app.relief.controller;

import com.app.relief.dto.project.CreateProjectRequest;
import com.app.relief.dto.project.CreateProjectResponse;
import com.app.relief.dto.project.ProjectDetailsDto;
import com.app.relief.dto.project.ProjectSummaryDto;
import com.app.relief.entity.User;
import com.app.relief.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
         this.projectService = projectService;
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

    @PostMapping("/newProject")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateProjectResponse> createNewProject(@RequestBody CreateProjectRequest request, @AuthenticationPrincipal User user){
         try {
             CreateProjectResponse response =  projectService.createNewProject(request , user);
             return ResponseEntity.ok(response);
         }catch (Exception e){
             return ResponseEntity.badRequest().body(new CreateProjectResponse(e.getMessage()));
         }
    }
}
