package com.demo.todolist.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ToDoList {
    @Id
    private String name;
    private String description;
    private Date creationDate;
}
