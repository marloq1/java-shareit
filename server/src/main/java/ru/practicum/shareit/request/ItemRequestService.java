package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getItemRequests(Long userId);

    List<ItemRequestDto> getAllItemRequests(Long userId);

    ItemRequestDto getItemRequest(Long requestId);
}
