package com.aditya.todotasks.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.aditya.todotasks.R;
import com.aditya.todotasks.adapters.TodoListAdapter;
import com.aditya.todotasks.fragments.EditDialogFragment;
import com.aditya.todotasks.models.ToDoItem;

public class MainActivity extends AppCompatActivity implements EditDialogFragment.EditDialogFragmentListener {

    private ListView listView;
    private TodoListAdapter todoListAdapter;
    private RelativeLayout noPendingTaskRelativeLayout;

    @Override
    public void onEditItem(int position, ToDoItem item) {
        todoListAdapter.replace(position, item);
        notifyAdapter();
        listView.smoothScrollToPosition(position);
    }

    @Override
    public void onAddItem(ToDoItem item) {
        todoListAdapter.addItem(item);
        notifyAdapter();
        listView.setSelection(todoListAdapter.getCount()-1);
    }

    @Override
    public void onRemoveItem(int position) {
        todoListAdapter.removeItem(position);
        notifyAdapter();
    }

    @Override
    public void onDismiss() {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_fab);
        noPendingTaskRelativeLayout = (RelativeLayout) findViewById(R.id.no_tasks);
        listView = (ListView) findViewById(R.id.todo_list);
        //todoListAdapter = new TodoListAdapter(this, getSampleToDoItems(100));
        todoListAdapter = new TodoListAdapter(this);
        listView.setAdapter(todoListAdapter);
        listView.setOnItemClickListener(getItemOnClickListener());
        listView.setOnItemLongClickListener(getItemOnLongClickListener());
        floatingActionButton.setOnClickListener(getFABOnClickListener());
        decideVisibilityForNoPendingTasks();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_done_all_white);
            // HACK
            getSupportActionBar().setTitle("  " + getString(R.string.app_name));
        }
    }

    private AdapterView.OnItemClickListener getItemOnClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openEditDialogFragment(todoListAdapter.getItem(i), i);
            }
        };
    }

    private AdapterView.OnItemLongClickListener getItemOnLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToDoItem item = todoListAdapter.getItem(i);
                if (item == null) {
                    return false;
                }
                if (item.isCompleted()) {
                    onRemoveItem(i);
                    return true;
                } else {
                    ToDoItem completedItem = new ToDoItem(item.getId(), item.getTaskName(),
                                                          item.getPriority().ordinal(), item.getDueDate(), true);
                    onEditItem(i, completedItem);
                    return true;
                }
            }
        };
    }

    private View.OnClickListener getFABOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditDialogFragment(null, -1);
            }
        };
    }

    private void openEditDialogFragment(ToDoItem toDoItem, int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditDialogFragment editDialogFragment = EditDialogFragment.newInstance(toDoItem, position);
        editDialogFragment.show(fm, "edit_dialog");
    }

    private void notifyAdapter() {
        todoListAdapter.notifyDataSetChanged();
        decideVisibilityForNoPendingTasks();
    }

    private void decideVisibilityForNoPendingTasks() {
        if (todoListAdapter.getCount() == 0) {
            noPendingTaskRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            noPendingTaskRelativeLayout.setVisibility(View.GONE);
        }
    }

}
