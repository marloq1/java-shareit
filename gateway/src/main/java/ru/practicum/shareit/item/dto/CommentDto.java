package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Builder
public class CommentDto {


    private long id;
    private String authorName;
    private long itemId;
    private LocalDateTime created;
    @NotBlank
    private String text;

}
