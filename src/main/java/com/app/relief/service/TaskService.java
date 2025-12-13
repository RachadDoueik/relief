package com.app.relief.service;


import com.app.relief.dto.task.CreateTaskRequest;
import com.app.relief.dto.task.CreateTaskResponse;
import com.app.relief.entity.Project;
import com.app.relief.entity.Task;
import com.app.relief.entity.User;
import com.app.relief.repository.ProjectRepository;
import com.app.relief.repository.TaskRepository;
import com.app.relief.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public TaskService(UserRepository userRepository , ProjectRepository projectRepository , TaskRepository taskRepository){
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
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


}
