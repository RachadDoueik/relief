package com.app.relief.entity;

import com.app.relief.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // "user" is a reserved keyword in some SQL databases
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    //a user has a set of projects created in order
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Project> projects = new HashSet<>();

    //a user has a set of comments made
    @OneToMany(mappedBy = "author" , cascade =  CascadeType.ALL , orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    //a user has a set of tasks that he created
    @OneToMany(mappedBy = "createdBy" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Task> createdTasks = new HashSet<>();

    //a user has a set of tasks assigned to him
    @OneToMany(mappedBy = "assignedUser" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Task> assignedTasks = new HashSet<>();

    //a user has a set of attachments uploaded by him
    @OneToMany(mappedBy = "uploadedBy" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Attachment> attachments = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
}
