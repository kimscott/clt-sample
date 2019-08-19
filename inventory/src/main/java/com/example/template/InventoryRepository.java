package com.example.template;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface InventoryRepository  extends PagingAndSortingRepository<Inventory, Long> {
}
