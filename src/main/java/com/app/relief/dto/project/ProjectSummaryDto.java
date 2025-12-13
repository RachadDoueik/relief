package com.app.relief.dto.project;

import com.app.relief.enums.ProjectKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSummaryDto {
    private Long id;

    private String projectName;

    private ProjectKey projectKey;
}
