package com.app.relief.controller;

import com.app.relief.dto.task.TaskDetailsDto;
import com.app.relief.dto.task.TaskSummaryDto;
import com.app.relief.dto.task.UpdateTaskRequest;
import com.app.relief.dto.task.UpdateTaskResponse;
import com.app.relief.entity.User;
import com.app.relief.exception.TaskNotFoundException;
import com.app.relief.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}/summary")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskSummaryDto> getTaskSummaryById(@PathVariable Long taskId) {
        try {
            TaskSummaryDto response = taskService.getTaskSummaryById(taskId);
            return ResponseEntity.ok(response);
        } catch (TaskNotFoundException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{taskId}/details")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskDetailsDto> getTaskDetailsById(@PathVariable Long taskId){
        try {
            TaskDetailsDto response = taskService.getTaskDetailsById(taskId);
            return ResponseEntity.ok(response);
        }
        catch (TaskNotFoundException e){
            logger.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UpdateTaskResponse> updateTaskById(@RequestBody UpdateTaskRequest request , @PathVariable Long taskId , @AuthenticationPrincipal User user){
        try {
            UpdateTaskResponse response = taskService.updateTaskById(request , taskId, user);
            return ResponseEntity.ok(response);
        } catch (TaskNotFoundException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
