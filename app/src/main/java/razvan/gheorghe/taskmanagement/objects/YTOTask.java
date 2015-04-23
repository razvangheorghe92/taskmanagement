package razvan.gheorghe.taskmanagement.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import razvan.gheorghe.taskmanagement.utils.DBDateConverter;

public class YTOTask implements Comparable<YTOTask>{
    String idTask;
    String date;
    String hour;
    String title;
    String type;
    String description;
    String priority;
    ArrayList<String> months;

    public YTOTask() {}

    public YTOTask(String date, String hour, String title, String description,
                   String type, String priority) {
        this.idTask = UUID.randomUUID().toString();
        this.date = date;
        this.hour = hour;
        this.type = type;
        this.title = title;
        this.description = description;
        this.priority = priority;

    }

    public YTOTask(String idTask, String date, String hour, String title, String description,
                   String type, String priority) {
        this.idTask = idTask;
        this.date = date;
        this.hour = hour;
        this.type = type;
        this.title = title;
        this.description = description;
        this.priority = priority;

    }


    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public Date getDate() {
        SimpleDateFormat  format = new SimpleDateFormat("dd.MM.yyyy");
        Date dateD = null;
        try {
            dateD = format.parse(date);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dateD;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String toJson() {
        JSONObject jsn = new JSONObject();

        try {
            jsn.put("date",date);
            jsn.put("hour", hour);
            jsn.put("type", type);
            jsn.put("title", title);
            jsn.put("description", description);
            jsn.put("priority", priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsn.toString();
    }

    public YTOTask fromJson(JSONObject jsn) {

        try {
            this.date = jsn.getString("date");
            this.hour = jsn.getString("hour");
            this.type = jsn.getString("type");
            this.title = jsn.getString("title");
            this.description = jsn.getString("description");
            this.priority = jsn.getString("priority");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public int compareTo(YTOTask anotherEvent) {
        String monthString1 = null;
        String monthString2 = null;

        DBDateConverter dbDateConverter = new DBDateConverter();

        //get month substring from date string
        // ex: date = 7-Februarie-2014 -> monthString = Februarie
        monthString1 = dbDateConverter.extractDateSubstring(date);
        monthString2 = dbDateConverter.extractDateSubstring(anotherEvent.date);

        //initialize month ArrayList
        months = dbDateConverter.addMonths();

        //month name to month number ex: Ianuarie becomes 01
        String month1 = dbDateConverter.monthNameToNumber(monthString1);
        String month2  = dbDateConverter.monthNameToNumber(monthString2);

        //get day from date String
        //ex: day = 01 from date = 1-Ianuarie-2014
        String day1 = dbDateConverter.getDayFromDate(date);
        String day2 = dbDateConverter.getDayFromDate(anotherEvent.date);

        SimpleDateFormat sdf  = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date dateString1 = null;
        try {
            dateString1 = sdf.parse(day1+"-"+month1+"-"+ date.substring(date.lastIndexOf("-")+1, date.length())
                    + " "+ hour.substring(0,2) + ":"+hour.substring(3,5));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeInMillisSinceEpoch1 = dateString1.getTime();

        Date dateString2 = null;
        try {
            dateString2 = sdf.parse(day2+"-"+month2+"-"+
                    anotherEvent.date.substring(anotherEvent.date.lastIndexOf("-")+1, anotherEvent.date.length())
                    + " "+ anotherEvent.hour.substring(0,2) + ":"+anotherEvent.hour.substring(3,5));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeInMillisSinceEpoch2 = dateString2.getTime();

        if(timeInMillisSinceEpoch1 > timeInMillisSinceEpoch2)
            return 1;
        if(timeInMillisSinceEpoch1 == timeInMillisSinceEpoch2)
            return 0;
        if(timeInMillisSinceEpoch1 < timeInMillisSinceEpoch2)
            return -1;
        return 0;
    }
}