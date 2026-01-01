package com.app.relief.mapper;

import com.app.relief.dto.comment.CommentDto;
import com.app.relief.dto.user.UserSummaryDto;
import com.app.relief.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    //comment to commentDto
    public CommentDto commentToCommentDto(Comment comment){

        if(comment == null) return null;

        CommentDto cd = new CommentDto();
        cd.setId(comment.getId());
        cd.setAuthor(new UserSummaryDto(comment.getAuthor().getUsername() , comment.getAuthor().getEmail()));
        cd.setContent(comment.getContent());
        cd.setCreatedAt(comment.getCreatedAt());

        return cd;

    }
}
