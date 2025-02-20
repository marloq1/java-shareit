package ru.practicum.shareit.item;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    @Query("SELECT i FROM Item i " +
            "JOIN FETCH i.owner o " +
            "LEFT JOIN FETCH i.comments " +
            "WHERE o.id =:ownerId")
    List<Item> findByOwnerId(@Param("ownerId")Long ownerId);


    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.comments " +
            "WHERE i.id =:id")
    Optional<Item> findById(@Param("id") Long id);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND i.available=true")
    List<Item> search(String text);

}
