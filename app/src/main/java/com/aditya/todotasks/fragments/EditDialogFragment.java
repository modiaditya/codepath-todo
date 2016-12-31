package com.aditya.todotasks.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.aditya.todotasks.R;
import com.aditya.todotasks.models.ToDoItem;
import com.aditya.todotasks.utils.DateHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class EditDialogFragment extends DialogFragment {

    private static final String TODO_ITEM = "todo_item";
    private static final String POSITION = "position";

    private Date pickedDueDate;
    private ToDoItem toDoItem;
    private int position;
    private EditDialogFragmentListener listener;

    public interface EditDialogFragmentListener {
        void onEditItem(int position, ToDoItem item);
        void onAddItem(ToDoItem item);
        void onRemoveItem(int position);
        void onDismiss();
    }

    public EditDialogFragment() {
    }

    public static EditDialogFragment newInstance(ToDoItem toDoItem, int position) {
        Bundle args = new Bundle();
        args.putSerializable(TODO_ITEM, toDoItem);
        args.putInt(POSITION, position);
        EditDialogFragment editDialogFragment = new EditDialogFragment();
        editDialogFragment.setArguments(args);
        return editDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.toDoItem = (ToDoItem) getArguments().getSerializable(TODO_ITEM);
            this.position = getArguments().getInt(POSITION);
        }
        this.listener = (EditDialogFragmentListener) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get all UI components
        final Button save = (Button) view.findViewById(R.id.save_button);
        final Button dismiss = (Button) view.findViewById(R.id.dismiss_button);
        final Button delete = (Button) view.findViewById(R.id.delete_button);
        final EditText taskName = (EditText) view.findViewById(R.id.task_name_edit_text);
        final Switch isCompletedSwitch = (Switch) view.findViewById(R.id.completed_switch);
        final Spinner spinner = (Spinner) view.findViewById(R.id.priority_spinner);
        final TextView dueDate = (TextView) view.findViewById(R.id.due_date);
        final Calendar c = Calendar.getInstance();
        pickedDueDate = c.getTime();

        // setup items for the listview
        ArrayAdapter<CharSequence> simpleAdapter = ArrayAdapter.createFromResource(getContext(),
                                                                                   R.array.priority_array,
                                                                                   android.R.layout.simple_spinner_item);

        simpleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(simpleAdapter);

        // setup each item
        if (toDoItem != null) {
            taskName.setText(toDoItem.getTaskName());
            spinner.setSelection(toDoItem.getPriority().ordinal());
            dueDate.setText(DateHelper.getMediumFormatDate(getContext(), toDoItem.getDueDate()));
            getDialog().setTitle(getString(R.string.edit_task));
            isCompletedSwitch.setChecked(toDoItem.isCompleted());
            c.setTime(toDoItem.getDueDate());
            delete.setVisibility(View.VISIBLE);
        } else {
            dueDate.setText(DateHelper.getMediumFormatDate(getContext(), Calendar.getInstance().getTime()));
            getDialog().setTitle(getString(R.string.add_task));
            delete.setVisibility(View.GONE);
        }

        // setup click listeners
        dueDate.setOnClickListener(getDueDateOnClickLister(dueDate, c));
        delete.setOnClickListener(getDeleteOnClickListener());
        dismiss.setOnClickListener(getDismissOnClickListener());
        save.setOnClickListener(getSaveOnClickListener(taskName, spinner, isCompletedSwitch));

    }

    private View.OnClickListener getDismissOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDismiss();
                dismiss();
            }
        };
    }

    private View.OnClickListener getSaveOnClickListener(final TextView taskName,
                                                        final Spinner spinner,
                                                        final Switch isCompletedSwitch) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDoItem item = new ToDoItem(UUID.randomUUID().toString(),
                                             taskName.getText().toString(),
                                             spinner.getSelectedItemPosition(),
                                             pickedDueDate,
                                             isCompletedSwitch.isChecked());
                if (position != -1) {
                    listener.onEditItem(position, item);
                } else {
                    listener.onAddItem(item);
                }

                dismiss();
            }
        };
    }

    private View.OnClickListener getDeleteOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRemoveItem(position);
                dismiss();
            }
        };
    }

    private View.OnClickListener getDueDateOnClickLister(final TextView dueDate, final Calendar c) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                DatePickerDialog datePickerDialog =
                    new DatePickerDialog(getContext(),
                                         new DatePickerDialog.OnDateSetListener() {

                                             @Override
                                             public void onDateSet(DatePicker view, int yearOfDate,
                                                                   int monthOfYear, int dayOfMonth) {

                                                 Calendar c = Calendar.getInstance();
                                                 c.set(yearOfDate, monthOfYear, dayOfMonth);
                                                 pickedDueDate = c.getTime();
                                                 dueDate.setText(DateHelper.getMediumFormatDate(getContext(),
                                                                                                pickedDueDate));

                                             }
                                         }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                datePickerDialog.show();
            }
        };
    }
}
