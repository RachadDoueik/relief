package com.app.relief.dto.comment;


import com.app.relief.entity.Task;
import com.app.relief.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {

    @NotNull
    private String content;

}
