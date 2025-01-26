package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;


@Data
public class Booking {

    private Item item;
    private LocalDate start;
    private boolean isAcceptedByOwner;
}
