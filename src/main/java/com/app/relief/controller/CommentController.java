package com.app.relief.controller;

import com.app.relief.dto.comment.CommentDto;
import com.app.relief.entity.User;
import com.app.relief.exception.CommentNotFoundException;
import com.app.relief.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;

    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        try {
            var response = commentService.getCommentById(id);
            return ResponseEntity.ok(response);
        }
        catch(CommentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id , @AuthenticationPrincipal User user) {
        try {
            commentService.deleteCommentById(id , user);
            return ResponseEntity.noContent().build();
        } catch (CommentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
