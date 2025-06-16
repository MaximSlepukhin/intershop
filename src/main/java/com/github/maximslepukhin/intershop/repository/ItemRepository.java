package com.github.maximslepukhin.intershop.repository;

import com.github.maximslepukhin.intershop.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
    Page<Item> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            @Param("title") String title,
            @Param("description") String description,
            Pageable pageable);

    Optional<Item> findById(Long id);

    List<Item> findAllById(Iterable<Long> ids);
}
