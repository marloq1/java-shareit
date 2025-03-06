package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static Comment mapDtoToComment(CommentDto commentDto, User user, Item item) {
        return Comment.builder()
                .text(commentDto.getText())
                .time(LocalDateTime.now())
                .user(user)
                .item(item)
                .build();

    }

    public static CommentDto mapCommentToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getUser().getName())
                .itemId(comment.getItem().getId())
                .text(comment.getText())
                .created(comment.getTime())
                .build();
    }

}
