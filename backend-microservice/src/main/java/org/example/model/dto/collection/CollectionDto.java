package org.example.model.dto.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Long createdBy;
}
