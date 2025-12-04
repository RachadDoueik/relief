package com.app.relief.entity;

import com.app.relief.enums.ProjectKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "projects")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //a project is created by a single user
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    //a project contains multiple tasks
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();

    @Column(nullable = false , unique = true)
    private String projectName;

    @Enumerated(EnumType.STRING)
    private ProjectKey projectKey;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    private LocalDateTime creationDate;
}
