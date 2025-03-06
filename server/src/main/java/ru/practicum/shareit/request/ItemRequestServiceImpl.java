package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        return ItemRequestMapper.mapRequestToDto(itemRequestRepository
                .save(ItemRequestMapper.mapDtoToRequest(itemRequestDto, user)));
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));

        List<ItemRequest> requests = itemRequestRepository.findAllByOwnerId(userId);
        return requests.stream().map(ItemRequestMapper::mapRequestToDto).sorted((r1, r2) -> {
            if (r1.getCreated().isBefore(r2.getCreated())) {
                return 1;
            } else if (r2.getCreated().isBefore(r1.getCreated())) {
                return -1;
            } else
                return 0;
        }).toList();
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        List<ItemRequest> requests = itemRequestRepository.findAllExceptOwnerId(userId);
        return requests.stream().map(ItemRequestMapper::mapRequestToDto).sorted((r1, r2) -> {
            if (r1.getCreated().isBefore(r2.getCreated())) {
                return 1;
            } else if (r2.getCreated().isBefore(r1.getCreated())) {
                return -1;
            } else
                return 0;
        }).toList();
    }

    @Override
    public ItemRequestDto getItemRequest(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findByIdWithItems(requestId)
                .orElseThrow(() -> new NotFoundException("Запроса с id " + requestId + " нет"));
        return ItemRequestMapper.mapRequestToDto(itemRequest);
    }
}
