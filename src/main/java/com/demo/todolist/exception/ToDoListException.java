package com.demo.todolist.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ToDoListException extends Exception {
    private HttpStatus code;
    public ToDoListException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }
}
