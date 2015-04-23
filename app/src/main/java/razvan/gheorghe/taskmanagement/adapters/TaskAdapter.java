package razvan.gheorghe.taskmanagement.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import razvan.gheorghe.taskmanagement.R;
import razvan.gheorghe.taskmanagement.fragments.TasksFragment;
import razvan.gheorghe.taskmanagement.objects.YTOTask;
import razvan.gheorghe.taskmanagement.utils.DBConnector;
import razvan.gheorghe.taskmanagement.utils.NoDefaultSpinner;

public class TaskAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    ArrayList<YTOTask> tasks = new ArrayList<YTOTask>();

    public TaskAdapter(Context context, ArrayList<YTOTask> tasks) {
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int pos) {
        return tasks.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row, null);
        }

        final TextView txtEdit = (TextView) view.findViewById(R.id.txtEdit);
        final TextView txtErase = (TextView) view.findViewById(R.id.txtErase);

        if (txtEdit != null) {
            if (tasks.size() > 0) {
                txtEdit.setVisibility(View.VISIBLE);
                txtErase.setVisibility(View.VISIBLE);
            } else {
                txtEdit.setVisibility(View.GONE);
                txtErase.setVisibility(View.GONE);

            }
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Marker Felt.ttf");

            TextView listItemText = (TextView) view.findViewById(R.id.textView);
            listItemText.setText(tasks.get(position).getTitle());
            listItemText.setTypeface(Typeface.create(tf, Typeface.BOLD));

            txtEdit.setTypeface(Typeface.create(tf, Typeface.BOLD));
            txtErase.setTypeface(Typeface.create(tf, Typeface.BOLD));
            txtErase.setId(10000 + position);
            txtErase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YTOTask task = tasks.get(position);
                    showPopupDelete(task);

                }
            });
            txtEdit.setId(25000 + position);
            txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YTOTask task = tasks.get(position);
                    showPopup(task);

                }
            });

        }
        return view;
    }

    public void showPopupDelete(final YTOTask task) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_erroare);
        dialog.setCancelable(false);
        TextView textHeader;
        TextView textContent;

        textHeader = (TextView) dialog.findViewById(R.id.text_titlu);
        textContent = (TextView) dialog.findViewById(R.id.text_error);

        textHeader.setText("Atentie !");
        textContent.setText("Stergeti acest eveniment?");

        Button btn_nu = (Button) dialog.findViewById(R.id.button_nu);

        btn_nu.setText("NU");

        btn_nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btn_da = (Button) dialog.findViewById(R.id.button_da);
        btn_da.setText("DA");
        btn_da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DBConnector(context).deleteTask(task.getIdTask());
                tasks.remove(task);
                ((BaseAdapter) TasksFragment.task_list.getAdapter()).notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void showPopup(final YTOTask task) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCancelable(true);
        final TimePicker tp;
        final DatePicker dp;
        final NoDefaultSpinner spPrioritate;
        final NoDefaultSpinner spTip;
        final EditText edtTitlu, edtDescriere;
        spTip = (NoDefaultSpinner) dialog.findViewById(R.id.spTip);
        spPrioritate = (NoDefaultSpinner) dialog.findViewById(R.id.spPrioritate);
        edtTitlu = (EditText) dialog.findViewById(R.id.edtTitlu);
        edtDescriere = (EditText) dialog.findViewById(R.id.edtDescriere);
        tp = (TimePicker) dialog.findViewById(R.id.task_hour);
        dp = (DatePicker) dialog.findViewById(R.id.task_date);
        tp.setIs24HourView(true);
        tp.setEnabled(true);
        Button btn_da = (Button) dialog.findViewById(R.id.button_da);
        Button btn_nu = (Button) dialog.findViewById(R.id.button_nu);
        final DBConnector dbConnector = new DBConnector(context);
        edtDescriere.setText(task.getDescription());
        edtTitlu.setText(task.getTitle());
        spPrioritate.setSelection(getIndex(spPrioritate, task.getPriority()));
        spTip.setSelection(getIndex(spTip, task.getType()));

        btn_da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spPrioritate.getSelectedItem() != null && spTip.getSelectedItem() != null
                        && edtDescriere.getText().toString() != null &&
                        edtTitlu.getText().toString() != null) {
                    String day = checkDigit(dp.getDayOfMonth());
                    String month = checkDigit(dp.getMonth() + 1);
                    String year = "" + dp.getYear();
                    YTOTask newTask = new YTOTask(task.getIdTask(), day + "." + month + "." + year,
                            getCurrentTime(tp), edtTitlu.getText().toString(),
                            edtDescriere.getText().toString(), spTip.getSelectedItem().toString(),
                            spPrioritate.getSelectedItem().toString());
                    dbConnector.updateTask(newTask);
                    ((BaseAdapter) TasksFragment.task_list.getAdapter()).notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Trebuie sa completati toata campurile",
                            Toast.LENGTH_LONG).show();
//                    if(spMaterii.getSelectedItem() == null) {
//                        spMaterii.setBackgroundResource(R.drawable.edit_text_error);
//                    } else {
//                        spMaterii.setBackgroundResource(R.drawable.edit_text);
//                    }

                }
                dialog.dismiss();
            }
        });

        btn_nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public String getCurrentTime(TimePicker tp) {
        String hour_minutes = null;
        if (tp.getCurrentHour() < 10 && tp.getCurrentMinute() < 10)
            hour_minutes = "0" + tp.getCurrentHour() + ":0" + tp.getCurrentMinute();
        if (tp.getCurrentHour() < 10 && tp.getCurrentMinute() > 9)
            hour_minutes = "0" + tp.getCurrentHour() + ":" + tp.getCurrentMinute();
        if (tp.getCurrentHour() > 9 && tp.getCurrentMinute() < 10)
            hour_minutes = tp.getCurrentHour() + ":0" + tp.getCurrentMinute();
        if (tp.getCurrentHour() > 9 && tp.getCurrentMinute() > 9)
            hour_minutes = tp.getCurrentHour() + ":" + tp.getCurrentMinute();
        return hour_minutes;
    }

    //7 to 07
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    private int getIndex(NoDefaultSpinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }


}