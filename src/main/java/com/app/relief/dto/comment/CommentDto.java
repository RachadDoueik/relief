package com.app.relief.dto.comment;

import com.app.relief.dto.user.UserSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String content;

    private UserSummaryDto author;

    private LocalDateTime createdAt;
}
