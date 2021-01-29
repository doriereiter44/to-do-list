package com.demo.todolist.service.impl;

import com.demo.todolist.exception.ToDoListException;
import com.demo.todolist.model.ToDoItem;
import com.demo.todolist.model.ToDoList;
import com.demo.todolist.repository.ToDoListItemRepository;
import com.demo.todolist.repository.ToDoRepository;
import com.demo.todolist.service.ToDoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ToDoListServiceImpl implements ToDoListService {
    private final
    ToDoRepository toDoRepository;

    private final
    ToDoListItemRepository toDoListItemRepository;

    @Autowired
    public ToDoListServiceImpl(ToDoRepository toDoRepository, ToDoListItemRepository toDoListItemRepository) {
        this.toDoRepository = toDoRepository;
        this.toDoListItemRepository = toDoListItemRepository;
    }

    @Override
    public void createToDoList(ToDoList toDoList) throws ToDoListException {
        listNameIsValid(toDoList.getName());
        try {
            toDoList.setCreationDate(new Date());
            toDoRepository.save(toDoList);
        } catch (Exception e) {
            throw new ToDoListException("Error occurred during To Do List creation", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String addItemToMostRecentCreatedList(ToDoItem toDoItem) throws ToDoListException {
        ToDoList toDoList = toDoRepository.findFirstByOrderByCreationDateDesc();
        if (toDoList == null)
            throw new ToDoListException("No To Do Lists have been created. Cannot add item.", HttpStatus.EXPECTATION_FAILED);
        toDoItem.setListName(toDoList.getName());
        toDoListItemRepository.save(toDoItem);
        return toDoList.getName();
    }

    @Override
    public void addItemToList(ToDoItem toDoItem, String listName) throws ToDoListException {
        doesListExist(listName);
        toDoItem.setListName(listName);
        toDoListItemRepository.save(toDoItem);
    }

    @Override
    public List<Map<String, Object>> getAllItemsForList(String listName) throws ToDoListException {
        doesListExist(listName);
        return toDoListItemRepository.findAllByListName(listName)
                .parallelStream()
                .map(toDoItem -> {
                    Map<String, Object> results = new HashMap<>();
                    results.put("name", toDoItem.getName());
                    return results;
                }).collect(Collectors.toList());
    }

    private void doesListExist(String listName) throws ToDoListException {
        ToDoList list = toDoRepository.findFirstByName(listName);
        if (list == null)
            throw new ToDoListException(String.format("To Do List: [%s] does not exist", listName), HttpStatus.BAD_REQUEST);
    }

    private void listNameIsValid(String listName) throws ToDoListException {
        ToDoList list = toDoRepository.findFirstByName(listName);
        if (list != null)
            throw new ToDoListException(String.format("To Do List with name [%s] already exists", listName), HttpStatus.BAD_REQUEST);
    }
}
