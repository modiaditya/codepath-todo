package com.aditya.todotasks.database;

import android.provider.BaseColumns;

class ToDoDatabaseContract {

    private ToDoDatabaseContract() {}

    public static class ToDoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_NAME_TODO_ID = "todo_id";
        public static final String COLUMN_NAME_TODO_NAME = "todo_name";
        public static final String COLUMN_NAME_TODO_DUE_DATE = "todo_due_date";
        public static final String COLUMN_NAME_TODO_PRIORITY = "todo_priority";
        public static final String COLUMN_NAME_IS_COMPLETED = "is_completed";
    }
}
