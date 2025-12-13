package com.app.relief.entity;

import com.app.relief.enums.TaskPriority;
import com.app.relief.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String taskName;

    @Column(nullable = false)
    private String taskDescription;

    //a task belongs to a single project
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    //a task can be assigned to a single user
    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    //a task is created by a single user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    //a task can have multiple comments
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    //a task can have multiple attachments
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attachment> attachments = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
