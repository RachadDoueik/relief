package com.app.relief.dto.project;

import com.app.relief.enums.ProjectKey;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequest {

    @NotNull
    private String projectName;

    @NotNull
    private ProjectKey projectKey;

    @NotNull
    private String projectDescription;

}
