package razvan.gheorghe.taskmanagement.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import razvan.gheorghe.taskmanagement.R;
import razvan.gheorghe.taskmanagement.adapters.TaskAdapter;
import razvan.gheorghe.taskmanagement.objects.YTOTask;
import razvan.gheorghe.taskmanagement.utils.DBConnector;
import razvan.gheorghe.taskmanagement.utils.NoDefaultSpinner;

public class TasksFragment extends Fragment {

    Typeface tf;
    DBConnector dbConnector;
    TextView txtTasks;
    TextView txtAdd;
    NoDefaultSpinner spinnerPerioada;
    TextView viewAll, viewImportant, viewOptional;
    TextView txtZeroTaskuri;
    public static ListView task_list;
    public static int selectedOption = 1;// 1- All , 2- Important, 3- Optional
    ArrayList<YTOTask> tasks = new ArrayList<>();
    ArrayList<YTOTask> allTasks = new ArrayList<>();
    TaskAdapter adapter;
    public static int selectedPeriod = 0; //0 - toate , 1 - astazi, 2- maine, 3- saptamana , 4 - luna

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Marker Felt.ttf");
        dbConnector = new DBConnector(getActivity());
        allTasks = dbConnector.findAllTasks();
        adapter = new TaskAdapter(getActivity(), tasks);

        txtTasks = (TextView) rootView.findViewById(R.id.txtTasks);
        txtTasks.setTypeface(Typeface.create(tf, Typeface.BOLD));
        viewAll = (TextView) rootView.findViewById(R.id.txtView1);
        viewImportant = (TextView) rootView.findViewById(R.id.txtView2);
        viewOptional = (TextView) rootView.findViewById(R.id.txtView3);
        txtAdd = (TextView) rootView.findViewById(R.id.txtAdd);
        txtZeroTaskuri = (TextView) rootView.findViewById(R.id.txtZeroTaskuri);
        txtZeroTaskuri.setTypeface(Typeface.create(tf, Typeface.BOLD));
        task_list = (ListView) rootView.findViewById(R.id.task_list);
        spinnerPerioada = (NoDefaultSpinner) rootView.findViewById(R.id.spPerioada);
        spinnerPerioada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                selectedPeriod = position;
                selectType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        changeMenuColors(viewAll, viewImportant, viewOptional);
        tasks.addAll(allTasks);
        selectedOption = 1;
        showHide();
        TaskAdapter adapter = new TaskAdapter(getActivity(), tasks);
        task_list.setAdapter(adapter);

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMenuColors(viewAll, viewImportant, viewOptional);
                allTasks.clear();
                tasks.clear();
                tasks = new ArrayList<>();
                allTasks.addAll(dbConnector.findAllTasks());
                if (selectedPeriod == 0) {
                    tasks = allTasks;
                }
                if (selectedPeriod == 1) {//astazi events
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy");
                    String currentDay = df.format(c.getTime());
                    for (YTOTask task : allTasks)
                        if (df.format(task.getDate()).equals(currentDay))
                            tasks.add(task);
                }
                if (selectedPeriod == 2) {//maine events
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, 1);
                    SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy");
                    String currentDay = df.format(c.getTime());
                    for (YTOTask task : allTasks)
                        if (df.format(task.getDate()).equals(currentDay))
                            tasks.add(task);
                }
                if (selectedPeriod == 3) {//saptamana asta
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy");
                    String currentDay = df.format(c.getTime());
                    int cweekNo = c.get(Calendar.WEEK_OF_YEAR);
                    int cyear = c.get(Calendar.YEAR);
                    for (YTOTask task : allTasks) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(task.getDate());
                        int year = cal.get(Calendar.YEAR);
                        int weekNo = cal.get(Calendar.WEEK_OF_YEAR);
                        if (cyear == year && weekNo == cweekNo)
                            tasks.add(task);
                    }
                }
                if (selectedPeriod == 4) {
                    Calendar c = Calendar.getInstance();
                    int cmonth = c.get(Calendar.MONTH);
                    int cyear = c.get(Calendar.YEAR);
                    for (YTOTask task : allTasks) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(task.getDate());
                        int year = cal.get(Calendar.YEAR);
                        int intMonth = cal.get(Calendar.MONTH);
                        if (cmonth == intMonth && cyear == year)
                            tasks.add(task);
                    }
                }

                selectedOption = 1;
                showHide();
                TaskAdapter adapter = new TaskAdapter(getActivity(), tasks);
                task_list.setAdapter(adapter);
                ((BaseAdapter) TasksFragment.task_list.getAdapter()).notifyDataSetChanged();
            }
        });

        viewImportant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMenuColors(viewImportant, viewAll, viewOptional);
                selectedOption = 2;
                allTasks.clear();
                allTasks.addAll(dbConnector.findAllTasks());
                ArrayList<YTOTask> importantTasks = new ArrayList<>();
                for (YTOTask task : allTasks)
                    if (task.getType().equals("Important")) {
                        if (selectedPeriod == 0) {
                            importantTasks.add(task);
                        }
                        if (selectedPeriod == 1) {//astazi events
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy");
                            String currentDay = df.format(c.getTime());
                            if (df.format(task.getDate()).equals(currentDay))
                                importantTasks.add(task);
                        }
                        if (selectedPeriod == 2) {//maine events
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DATE, 1);
                            SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy");
                            String currentDay = df.format(c.getTime());
                            if (df.format(task.getDate()).equals(currentDay))
                                importantTasks.add(task);
                        }
                        if (selectedPeriod == 3) {//saptamana asta
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy");
                            String currentDay = df.format(c.getTime());
                            int cweekNo = c.get(Calendar.WEEK_OF_YEAR);
                            int cyear = c.get(Calendar.YEAR);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(task.getDate());
                            int year = cal.get(Calendar.YEAR);
                            int weekNo = cal.get(Calendar.WEEK_OF_YEAR);
                            if (cyear == year && weekNo == cweekNo)
                                importantTasks.add(task);
                        }
                        if (selectedPeriod == 4) {
                            Calendar c = Calendar.getInstance();
                            int cmonth = c.get(Calendar.MONTH);
                            int cyear = c.get(Calendar.YEAR);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(task.getDate());
                            int year = cal.get(Calendar.YEAR);
                            int intMonth = cal.get(Calendar.MONTH);
                            if (cmonth == intMonth && cyear == year)
                                importantTasks.add(task);
                        }
                    }
                tasks.clear();
                tasks.addAll(importantTasks);
                TaskAdapter adapter = new TaskAdapter(getActivity(), tasks);
                task_list.setAdapter(adapter);

            }
        });

        viewOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMenuColors(viewOptional, viewImportant, viewAll);
                selectedOption = 3;
                allTasks.clear();
                allTasks.addAll(dbConnector.findAllTasks());
                ArrayList<YTOTask> optionalTasks = new ArrayList<>();
                for (YTOTask task : allTasks)
                    if (task.getType().equals("Optional")) {
                        if (selectedPeriod == 0) {
                            optionalTasks.add(task);
                        }
                        if (selectedPeriod == 1) {//astazi events
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy");
                            String currentDay = df.format(c.getTime());
                            if (df.format(task.getDate()).equals(currentDay))
                                optionalTasks.add(task);
                        }
                        if (selectedPeriod == 2) {//maine events
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DATE, 1);
                            SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy");
                            String currentDay = df.format(c.getTime());
                            if (df.format(task.getDate()).equals(currentDay))
                                optionalTasks.add(task);
                        }
                        if (selectedPeriod == 3) {//saptamana asta
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy");
                            String currentDay = df.format(c.getTime());
                            int cweekNo = c.get(Calendar.WEEK_OF_YEAR);
                            int cyear = c.get(Calendar.YEAR);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(task.getDate());
                            int year = cal.get(Calendar.YEAR);
                            int weekNo = cal.get(Calendar.WEEK_OF_YEAR);
                            if (cyear == year && weekNo == cweekNo)
                                optionalTasks.add(task);
                        }
                        if (selectedPeriod == 4) {
                            Calendar c = Calendar.getInstance();
                            int cmonth = c.get(Calendar.MONTH);
                            int cyear = c.get(Calendar.YEAR);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(task.getDate());
                            int year = cal.get(Calendar.YEAR);
                            int intMonth = cal.get(Calendar.MONTH);
                            if (cmonth == intMonth && cyear == year)
                                optionalTasks.add(task);
                        }
                    }
                tasks.clear();
                tasks.addAll(optionalTasks);
                TaskAdapter adapter = new TaskAdapter(getActivity(), tasks);
                task_list.setAdapter(adapter);
            }
        });


        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });


        return rootView;
    }

    private void selectType() {
        if (selectedOption == 1)
            viewAll.performClick();
        else if (selectedOption == 2)
            viewImportant.performClick();
        else if (selectedOption == 3)
            viewOptional.performClick();
    }

    private void showPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCancelable(false);
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

        btn_da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spPrioritate.getSelectedItem() != null && spTip.getSelectedItem() != null
                        && edtDescriere.getText().toString() != null &&
                        edtTitlu.getText().toString() != null) {
                    String day = checkDigit(dp.getDayOfMonth());
                    String month = checkDigit(dp.getMonth() + 1);
                    String year = "" + dp.getYear();
                    YTOTask task = new YTOTask(day + "." + month + "." + year,
                            getCurrentTime(tp), edtTitlu.getText().toString(),
                            edtDescriere.getText().toString(), spTip.getSelectedItem().toString(),
                            spPrioritate.getSelectedItem().toString());
                    dbConnector.insertTask(task);
                    selectType();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Trebuie sa completati toata campurile",
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

    void changeMenuColors(TextView blue, TextView gray1, TextView gray2) {
        blue.setBackgroundResource(R.drawable.ordonare_selectat);
        gray1.setBackgroundResource(R.drawable.ordonare_neselectat);
        gray2.setBackgroundResource(R.drawable.ordonare_neselectat);
        blue.setTextColor(Color.WHITE);
        gray1.setTextColor(getResources().getColor(R.color.albastru));
        gray2.setTextColor(getResources().getColor(R.color.albastru));
        blue.setTypeface(null, Typeface.BOLD_ITALIC);
        gray1.setTypeface(null, Typeface.BOLD);
        gray2.setTypeface(null, Typeface.BOLD);
    }

    void showHide() {
        if (tasks.size() == 0) {
            txtZeroTaskuri.setVisibility(View.VISIBLE);
            task_list.setVisibility(View.GONE);
        } else {
            task_list.setAdapter(adapter);
            txtZeroTaskuri.setVisibility(View.GONE);
            task_list.setVisibility(View.VISIBLE);
        }
    }
}
