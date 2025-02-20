package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exceptions.NotAllowedException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto postItem(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        Item item = ItemMapper.mapDtoToItem(itemDto, user);
        return ItemMapper.mapItemToDto(itemRepository.save(item));

    }

    @Override
    public ItemDto patchItem(long userId, ItemDto itemDto, long itemId) {

        Item itemFromMemory = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещи с таким id нет"));
        if (itemFromMemory.getOwner().getId() != userId) {
            throw new NotAllowedException("Менять вещь может только ее владелец");
        }
        if (itemDto.getName() != null) {
            itemFromMemory.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemFromMemory.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemFromMemory.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.mapItemToDto(itemRepository.save(itemFromMemory));

    }

    @Override
    public ItemDtoWithBookingAndComments getItem(Long userId, long itemId) {
        Pageable pageable = PageRequest.of(0, 1);
        Optional<Item> item = itemRepository.findById(itemId);
        if (!userId.equals(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещи с таким id нет")).getId())) {
            return ItemMapper.mapItemToExtendDto(itemRepository.findById(itemId)
                            .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет")),
                    BookingMapper.mapBookingToDto(bookingRepository.findLast(userId, Status.APPROVED, pageable)
                            .stream().findFirst().orElse(null)),
                    BookingMapper.mapBookingToDto(bookingRepository.findNext(userId, Status.APPROVED, pageable)
                            .stream().findFirst().orElse(null)));
        } else {
            return ItemMapper.mapItemToExtendDto(itemRepository.findById(itemId)
                    .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет")), null, null);
        }
    }

    @Override
    public List<ItemDtoWithBookingAndComments> getItems(Long userId) {
        Pageable pageable = PageRequest.of(0, 1);
        return itemRepository.findByOwnerId(userId).stream().map(item -> {
            return ItemMapper.mapItemToExtendDto(item,
                    BookingMapper.mapBookingToDto(bookingRepository.findLast(userId, Status.APPROVED, pageable)
                            .stream().findFirst().orElse(null)),
                    BookingMapper.mapBookingToDto(bookingRepository.findNext(userId, Status.APPROVED, pageable)
                            .stream().findFirst().orElse(null)));
        }).toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        return itemRepository.search(text).stream().map(ItemMapper::mapItemToDto).toList();
    }

    @Transactional
    @Override
    public CommentDto postComment(Long userId, long itemId, CommentDto commentDto) {
        bookingRepository.checkComments(userId, itemId)
                .orElseThrow(() -> new ValidationException("Нельзя комментировать вещи, которые не брали в аренду"));
        User user = userRepository
                .findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        Item item = itemRepository
                .findById(itemId).orElseThrow(() -> new NotFoundException("Вещи с таким id нет"));
        return CommentMapper.mapCommentToDto(commentRepository.save(CommentMapper.mapDtoToComment(commentDto, user, item)));
    }
}
