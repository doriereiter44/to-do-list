package com.demo.todolist.service;

import com.demo.todolist.exception.ToDoListException;
import com.demo.todolist.model.ToDoItem;
import com.demo.todolist.model.ToDoList;

import java.util.List;
import java.util.Map;

public interface ToDoListService {
    void createToDoList(ToDoList toDoList) throws ToDoListException;

    String addItemToMostRecentCreatedList(ToDoItem toDoItem) throws ToDoListException;

    void addItemToList(ToDoItem toDoItem, String listName) throws ToDoListException;

    List<Map<String, Object>> getAllItemsForList(String listName) throws ToDoListException;
}
