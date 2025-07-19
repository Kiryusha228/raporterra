package org.example.model.dto.report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportDto {
    private String name;
    private String description;
    private String sqlQuery;
    private Long connection;

}
