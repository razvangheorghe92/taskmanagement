package razvan.gheorghe.taskmanagement.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBDateConverter {

    ArrayList<String> monthsList = new ArrayList<String>();

    public String monthNameToNumber(String monthString) {
        String monthNo;
        if ((monthsList.indexOf(monthString) + 1) < 10)
            monthNo = "0" + (monthsList.indexOf(monthString) + 1);
        else
            monthNo = "" + (monthsList.indexOf(monthString) + 1);
        return monthNo;
    }

    //extract Februarie from 14-Februarie-2014
    public String extractDateSubstring (String date) {
        String monthString = null;
        Pattern p = Pattern.compile("\\-(.*?)\\-");
        Matcher m = p.matcher(date);
        while (m.find()) {
            monthString = m.group(1);
        }
        return monthString;
    }

    public String getDayFromDate (String date) {
        String day = date.substring(0, date.indexOf("-"));
        if(Integer.parseInt(day) < 10) {
            day = "0"+day;
        }
        return day;
    }

    public ArrayList<String> addMonths() {
        monthsList.add("Ianuarie");
        monthsList.add("Februarie");
        monthsList.add("Martie");
        monthsList.add("Aprilie");
        monthsList.add("Mai");
        monthsList.add("Iunie");
        monthsList.add("Iulie");
        monthsList.add("August");
        monthsList.add("Septembrie");
        monthsList.add("Octombrie");
        monthsList.add("Noiembrie");
        monthsList.add("Decembrie");

        return monthsList;
    }
}
