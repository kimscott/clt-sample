package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceService {

    @Autowired
    BudgetRepository budgetRepository;


    public List<Budget> checkBudget(String name){

        List<Budget> list = budgetRepository.findByName(name);

        return list;
    }
}
