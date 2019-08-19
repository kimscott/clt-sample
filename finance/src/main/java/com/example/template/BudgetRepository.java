package com.example.template;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BudgetRepository extends PagingAndSortingRepository<Budget, Long> {

    List<Budget> findByName(@Param("name") String name);
}
