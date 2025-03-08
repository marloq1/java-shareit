package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@ExtendWith(MockitoExtension.class)
public class ItemRequestImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemRequestService itemRequestService;



    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(userRepository,itemRequestRepository);
    }

    @Test
    void testGetRequestOrder() {
        LocalDateTime now = LocalDateTime.now();
        Mockito.when(userRepository.findById(1L))
                        .thenReturn(Optional.of(User.builder().build()));
        Mockito.when(itemRequestRepository.findAllByOwnerId(1L))
                .thenReturn(List.of(makeRequest(1L,"request1",now,20L),
                        makeRequest(2L,"request2",now,10L),
                        makeRequest(3L,"request3",now,20L)));
        List<ItemRequestDto> itemRequests = itemRequestService.getItemRequests(1L);

        assertThat(itemRequests.getLast().getId(),equalTo(2L));


    }

    @Test
    void testGetAllRequestOrder() {
        LocalDateTime now = LocalDateTime.now();
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito.when(itemRequestRepository.findAllExceptOwnerId(1L))
                .thenReturn(List.of(makeRequest(1L,"request1",now,20L),
                        makeRequest(2L,"request2",now,10L),
                        makeRequest(3L,"request3",now,20L)));
        List<ItemRequestDto> itemRequestsAll = itemRequestService.getAllItemRequests(1L);
        assertThat(itemRequestsAll.getLast().getId(),equalTo(2L));

    }

    private ItemRequest makeRequest(Long id, String description,LocalDateTime now,long plusSeconds) {
        return ItemRequest.builder()
                .id(id)
                .owner(User.builder().id(1L).build())
                .description(description)
                .created(now.plusSeconds(plusSeconds))
                .build();
    }
}
