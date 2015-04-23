package razvan.gheorghe.taskmanagement.utils;

import android.content.Context;
import android.content.SharedPreferences;

import razvan.gheorghe.taskmanagement.objects.Prefs;

public class LogSharedPrefs {

    public void setPrefs(String email, String parola, Context ctx) {
        SharedPreferences sharedPrefs = ctx.getSharedPreferences("LoggingPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPrefs.edit();
        edit.putString("email", email);
        edit.putString("parola", parola);

        edit.commit();
    }

    //0 -> nu exista date inca; 1-> exista si userul poate fi logat
    public int getPrefs(Context ctx) {
        SharedPreferences sharedPrefs = ctx.getSharedPreferences("LoggingPrefs", Context.MODE_PRIVATE);
        Prefs.emailPref = sharedPrefs.getString("email", null);
        Prefs.parolaPref = sharedPrefs.getString("parola", null);

        if (Prefs.emailPref == null || Prefs.parolaPref == null)
            return 0;
        else
            return 1;
    }

    public void deletePrefs(Context ctx) {
        SharedPreferences sharedPrefs = ctx.getSharedPreferences("LoggingPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPrefs.edit();
        edit.clear();
        edit.commit();
    }
}
