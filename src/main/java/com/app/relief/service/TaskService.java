package com.app.relief.service;

import com.app.relief.dto.comment.CommentDto;
import com.app.relief.dto.comment.CreateCommentRequest;
import com.app.relief.dto.comment.CreateCommentResponse;
import com.app.relief.dto.task.*;
import com.app.relief.entity.Comment;
import com.app.relief.entity.Project;
import com.app.relief.entity.Task;
import com.app.relief.entity.User;
import com.app.relief.exception.TaskNotFoundException;
import com.app.relief.exception.TaskOwnershipException;
import com.app.relief.mapper.CommentMapper;
import com.app.relief.mapper.TaskMapper;
import com.app.relief.repository.CommentRepository;
import com.app.relief.repository.ProjectRepository;
import com.app.relief.repository.TaskRepository;
import com.app.relief.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;

    public TaskService(UserRepository userRepository ,
                       ProjectRepository projectRepository ,
                       TaskRepository taskRepository,
                       TaskMapper taskMapper,
                       CommentRepository commentRepository,
                       CommentMapper commentMapper) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    //create new task for a specific project
    public CreateTaskResponse createTaskForProject(CreateTaskRequest request , Long projectId , User user){

        //get project using its id
        Project taskProject = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project Not Found !"));

        //check for project ownership before adding new task
        if(!taskProject.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to add tasks to this project !");
        }

        Task task = new Task();
        task.setTaskName(request.getTaskName());
        task.setTaskDescription(request.getTaskDescription());
        task.setStatus(request.getTaskStatus());
        task.setPriority(request.getTaskPriority());
        task.setCreatedBy(user);
        task.setProject(taskProject);

        //get assignedUser from request.assignedUserId
        User assignedUser = userRepository.findById(request.getAssignedUserId()).orElseThrow(() -> new RuntimeException("User not found !"));
        task.setAssignedUser(assignedUser);

        taskRepository.save(task);
        return new CreateTaskResponse("New Task created successfully for project " + projectId);
    }


    public TaskSummaryDto getTaskSummaryById(Long taskId) {

        Task task = taskRepository.findById(taskId).orElseThrow(() ->  new TaskNotFoundException("Task with id " + taskId + " not found !"));
        return taskMapper.taskToTaskSummaryDto(task);
    }

    public TaskDetailsDto getTaskDetailsById(Long taskId) {

        Task task = taskRepository.findById(taskId).orElseThrow(() ->  new TaskNotFoundException("Task with id " + taskId + " not found !"));
        return taskMapper.taskToTaskDetailsDto(task);
    }

    public UpdateTaskResponse updateTaskById(UpdateTaskRequest request , Long taskId , User owner){

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found !"));

        if(!task.getCreatedBy().getId().equals(owner.getId())){
            throw new TaskOwnershipException("You do not own this task to modify it !");
        }

        task.setTaskName(request.getTaskName());
        task.setTaskDescription(request.getTaskDescription());
        task.setStatus(request.getTaskStatus());
        task.setPriority(request.getTaskPriority());

        LocalDateTime dueDate = null;
        String dueDateStr = request.getTaskDueDate();
        if (dueDateStr != null && !dueDateStr.isBlank()) {
            try {
                dueDate = LocalDateTime.parse(dueDateStr, DateTimeFormatter.ISO_DATE_TIME);
            } catch (DateTimeParseException e) {
                DateTimeFormatter alt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                dueDate = LocalDateTime.parse(dueDateStr, alt);
            }
        }
        task.setTaskDueDate(dueDate);
        taskRepository.save(task);

        return new UpdateTaskResponse("Task with id " + taskId + " updated successfully !");
    }

    public void deleteTaskById(Long taskId , User owner){

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found !"));

        if(!task.getCreatedBy().getId().equals(owner.getId())){
            throw new TaskOwnershipException("You do not own this task to delete it !");
        }

        taskRepository.delete(task);
    }

    public CreateCommentResponse addCommentToTask(Long taskId, CreateCommentRequest request, User user) {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found !"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setAuthor(user);
        commentRepository.save(comment);

        return new CreateCommentResponse("Comment added successfully to task with id " + taskId);
    }

    public List<CommentDto> getCommentsForTask(Long taskId) {

        if(!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task with id " + taskId + " not found !");
        }

        List<Comment> comments = commentRepository.findByTaskId(taskId);

        return comments.stream().map(commentMapper::commentToCommentDto).toList();
    }
}
