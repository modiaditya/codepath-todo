package com.aditya.todotasks.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.aditya.todotasks.R;
import com.aditya.todotasks.database.ToDoDatabaseHandler;
import com.aditya.todotasks.models.ToDoItem;
import com.aditya.todotasks.utils.DateHelper;

import java.util.List;

public class TodoListAdapter extends ArrayAdapter<ToDoItem> {

    private Context context;
    private List<ToDoItem> toDoItems;
    private ToDoDatabaseHandler dbHandler;

    public TodoListAdapter(Context context) {
        super(context, R.layout.todo_item);
        this.context = context;
        this.dbHandler = new ToDoDatabaseHandler(context);
        this.toDoItems = dbHandler.getAllToDoItems();
    }

    @Override
    public int getCount() {
        return toDoItems.size();
    }

    @Nullable
    @Override
    public ToDoItem getItem(int position) {
        if (position < toDoItems.size()) {
            return toDoItems.get(position);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.todo_item, parent, false);
        TextView taskName = (TextView) rowView.findViewById(R.id.task_name_text);
        TextView dueDate = (TextView) rowView.findViewById(R.id.task_due_date_text);
        ImageView priority = (ImageView) rowView.findViewById(R.id.priority_image);
        ToDoItem item = toDoItems.get(position);
        taskName.setText(item.getTaskName());
        dueDate.setText(DateHelper.getMediumFormatDate(context, item.getDueDate()));
        if (item.getPriority().equals(ToDoItem.Priority.HIGH)) {
            priority.setImageResource(R.drawable.ic_flag_red);
        } else if (item.getPriority().equals(ToDoItem.Priority.MEDIUM)) {
            priority.setImageResource(R.drawable.ic_flag_orange);
        } else {
            priority.setImageResource(R.drawable.ic_flag_yellow);
        }

        if (item.isCompleted()) {
            dueDate.setTextColor(ContextCompat.getColor(context, R.color.grey));
            taskName.setTextColor(ContextCompat.getColor(context, R.color.grey));
            taskName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            priority.setImageResource(R.drawable.ic_flag_grey);
        }

        return rowView;
    }

    public void addItem(ToDoItem toDoItem) {
        dbHandler.addToDoItem(toDoItem);
        toDoItems.add(toDoItem);
    }

    public void removeItem(int position) {
        ToDoItem item = toDoItems.get(position);
        dbHandler.delete(item.getId());
        toDoItems.remove(position);
    }

    public void replace(int position, ToDoItem toDoItem) {
        ToDoItem item = toDoItems.get(position);
        dbHandler.update(item.getId(), toDoItem);
        toDoItems.set(position, toDoItem);
    }
}
