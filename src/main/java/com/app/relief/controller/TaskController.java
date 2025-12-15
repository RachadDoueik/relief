package com.app.relief.controller;

import com.app.relief.dto.attachment.CreateAttachmentResponse;
import com.app.relief.dto.comment.CommentDto;
import com.app.relief.dto.comment.CreateCommentRequest;
import com.app.relief.dto.comment.CreateCommentResponse;
import com.app.relief.dto.task.TaskDetailsDto;
import com.app.relief.dto.task.TaskSummaryDto;
import com.app.relief.dto.task.UpdateTaskRequest;
import com.app.relief.dto.task.UpdateTaskResponse;
import com.app.relief.entity.User;
import com.app.relief.exception.TaskNotFoundException;
import com.app.relief.service.AttachmentService;
import com.app.relief.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;
    private final AttachmentService attachmentService;

    public TaskController(TaskService taskService, AttachmentService attachmentService){
        this.taskService = taskService;
        this.attachmentService = attachmentService;
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

    @DeleteMapping("/{taskId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long taskId , @AuthenticationPrincipal User user){

        try {
            taskService.deleteTaskById(taskId , user);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{taskId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateCommentResponse> addCommentToTask(@PathVariable Long taskId, @RequestBody CreateCommentRequest request , @AuthenticationPrincipal User user) {
        try {
            var response = taskService.addCommentToTask(taskId, request, user);
            return ResponseEntity.ok(response);
        } catch (TaskNotFoundException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{taskId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommentDto>> getCommentsForTask(@PathVariable Long taskId) {
        try {
            var response = taskService.getCommentsForTask(taskId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{taskId}/attachments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateAttachmentResponse> addAttachmentToTask (@PathVariable Long taskId, @RequestParam("file") MultipartFile file , @AuthenticationPrincipal User user) {
        try {
            CreateAttachmentResponse response = attachmentService.storeAttachment(taskId, file, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
