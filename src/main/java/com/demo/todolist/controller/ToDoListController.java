package com.demo.todolist.controller;

import com.demo.todolist.exception.ToDoListException;
import com.demo.todolist.model.ToDoItem;
import com.demo.todolist.model.ToDoList;
import com.demo.todolist.service.ToDoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("rest/api/todolist")
public class ToDoListController {

    private final ToDoListService toDoListService;

    @Autowired
    public ToDoListController(ToDoListService toDoListService) {
        this.toDoListService = toDoListService;
    }

    /**
     * Create a new To Do List
     * @param toDoList with parameters name and description
     * @return message and status
     */
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody ToDoList toDoList) {
        if (!isParamValid(toDoList)) {
            return new ResponseEntity<>("To Do List creation requires [name] and [description]", HttpStatus.BAD_REQUEST);
        }
        try {
            toDoListService.createToDoList(toDoList);
        } catch (ToDoListException e) {
            return handleException(e);
        }
        return new ResponseEntity<>(String.format("Successfully created To Do List: %s",
                toDoList.getName()), HttpStatus.OK);
    }

    /**
     * Add a new item to the To Do List
     * if no To Do List name is specified, item will get added to most recently created list
     * @param item item with parameter namme
     * @param list list name to add the item to (Optional)
     * @return message and status
     */
    @PostMapping(value = "/item/add")
    public ResponseEntity<?> addItem(@RequestBody ToDoItem item, @RequestParam Optional<String> list) {
        if (!isParamValid(item)) {
            return new ResponseEntity<>(
                    "To Do List creation requires [name]",
                    HttpStatus.BAD_REQUEST);
        }
        String listName;
        try {
            if (list.isPresent()) {
                listName = list.get();
                toDoListService.addItemToList(item, listName);
            } else {
                listName = toDoListService.addItemToMostRecentCreatedList(item);
            }
        } catch (ToDoListException tdnfe) {
            return handleException(tdnfe);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Error occurred during To Do List creation",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return new ResponseEntity<>(String.format("Successfully added item %s to To Do List: %s",
                item.getName(), listName), HttpStatus.OK);
    }

    /**
     * List all items in specified To Do List
     * @param list To Do List name to add item to
     * @return message and status
     */
    @GetMapping(value = "/all")
    public ResponseEntity<?> listAll(@RequestParam String list) {
        List<Map<String, Object>> itemList;
        try {
            itemList = toDoListService.getAllItemsForList(list);
        } catch (ToDoListException e) {
            return handleException(e);
        }
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    private boolean isParamValid(ToDoList toDoList) {
        return toDoList.getName() != null && !toDoList.getName().isEmpty() &&
                toDoList.getDescription() != null && !toDoList.getDescription().isEmpty();
    }

    private boolean isParamValid(ToDoItem toDoItem) {
        return toDoItem.getName() != null && !toDoItem.getName().isEmpty();
    }

    private ResponseEntity<?> handleException(ToDoListException e) {
        return new ResponseEntity<>(e.getMessage(), e.getCode());
    }
}
