package com.demo.todolist.repository;

import com.demo.todolist.model.ToDoList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends CrudRepository<ToDoList, String> {
    ToDoList findFirstByOrderByCreationDateDesc();
    ToDoList findFirstByName(String name);
}
