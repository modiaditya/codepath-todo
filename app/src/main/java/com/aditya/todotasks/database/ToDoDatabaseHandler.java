package com.aditya.todotasks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.aditya.todotasks.database.ToDoDatabaseContract.ToDoEntry;
import com.aditya.todotasks.models.ToDoItem;
import com.aditya.todotasks.utils.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDo.db";

    public ToDoDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void addToDoItem(ToDoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(ToDoEntry.TABLE_NAME, null, getContentValues(item));
        db.close();
    }

    public List<ToDoItem> getAllToDoItems() {
        List<ToDoItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + ToDoEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(
                    cursor.getColumnIndexOrThrow(ToDoEntry.COLUMN_NAME_TODO_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ToDoEntry.COLUMN_NAME_TODO_NAME));
                int priority = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoEntry.COLUMN_NAME_TODO_PRIORITY));
                Date dueDate = DateHelper.getDateFromLong(cursor.getLong(cursor.getColumnIndexOrThrow(ToDoEntry.COLUMN_NAME_TODO_DUE_DATE)));
                int isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoEntry.COLUMN_NAME_IS_COMPLETED));
                ToDoItem item = new ToDoItem(id, name, priority, dueDate, isCompleted == 1);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    public void update(String id, ToDoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ToDoEntry.TABLE_NAME, getContentValues(item), ToDoEntry.COLUMN_NAME_TODO_ID + " = ?",
                         new String[] { String.valueOf(id) });
        db.close();
    }

    public void delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ToDoEntry.TABLE_NAME, ToDoEntry.COLUMN_NAME_TODO_ID + " = ?",
                  new String[] { String.valueOf(id) });
        db.close();
    }

    private ContentValues getContentValues(ToDoItem item) {
        ContentValues values = new ContentValues();
        values.put(ToDoEntry.COLUMN_NAME_TODO_ID, item.getId());
        values.put(ToDoEntry.COLUMN_NAME_TODO_NAME, item.getTaskName());
        values.put(ToDoEntry.COLUMN_NAME_TODO_PRIORITY, item.getPriority().ordinal());
        values.put(ToDoEntry.COLUMN_NAME_TODO_DUE_DATE, item.getDueDate().getTime());
        values.put(ToDoEntry.COLUMN_NAME_IS_COMPLETED, item.isCompleted() ? 1 : 0);
        return values;
    }

    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + ToDoEntry.TABLE_NAME + " (" +
            ToDoEntry.COLUMN_NAME_TODO_ID + " TEXT PRIMARY KEY," +
            ToDoEntry.COLUMN_NAME_TODO_NAME + " TEXT," +
            ToDoEntry.COLUMN_NAME_TODO_DUE_DATE + " INTEGER," +
            ToDoEntry.COLUMN_NAME_TODO_PRIORITY + " INTEGER," +
            ToDoEntry.COLUMN_NAME_IS_COMPLETED + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + ToDoEntry.TABLE_NAME;
}
