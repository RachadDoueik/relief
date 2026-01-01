package com.app.relief.service;

import com.app.relief.dto.comment.CommentDto;
import com.app.relief.entity.Comment;
import com.app.relief.entity.User;
import com.app.relief.exception.CommentNotFoundException;
import com.app.relief.mapper.CommentMapper;
import com.app.relief.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository , CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        return commentMapper.commentToCommentDto(comment);

    }

    public void deleteCommentById(Long id , User user) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment not found");
        }

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }
}
