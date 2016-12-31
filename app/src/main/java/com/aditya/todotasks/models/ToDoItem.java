package com.aditya.todotasks.models;

import java.io.Serializable;
import java.util.Date;

public class ToDoItem implements Serializable {

    public enum Priority {
        HIGH,
        MEDIUM,
        LOW
    }

    private final String id;
    private final String taskName;
    private final Priority priority;
    private final Date dueDate;
    private final boolean isCompleted;

    public ToDoItem(String id, String taskName, int priority, Date dueDate, boolean isCompleted) {
        this.id = id;
        this.taskName = taskName;
        this.priority = Priority.values()[priority];
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public String getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public Priority getPriority() {
        return priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
