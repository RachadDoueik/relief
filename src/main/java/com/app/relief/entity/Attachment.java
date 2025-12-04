package com.app.relief.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private double fileSize;

    //an attachment belongs to a single task
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    //an attachment is uploaded by a single user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User uploadedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;


}
