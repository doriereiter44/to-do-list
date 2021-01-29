package com.demo.todolist.repository;

import com.demo.todolist.model.ToDoItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoListItemRepository extends CrudRepository<ToDoItem, Integer> {
    List<ToDoItem> findAllByListName(String name);
}
