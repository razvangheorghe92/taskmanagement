package razvan.gheorghe.taskmanagement.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import razvan.gheorghe.taskmanagement.objects.Product;
import razvan.gheorghe.taskmanagement.objects.YTOTask;


public class DBConnector extends SQLiteOpenHelper {

    static final String TAG = "DBConnector";
    static final String DATABASE_NAME = "taskDB.sqlite";
    static final int DATABASE_VERSION = 1;
    public static String DATABASE_PATH = "/data/data/razvan.gheorghe.taskmanagement/databases/";

    private final Context myContext;
    public Cursor cursor;
    private SQLiteDatabase tasksDB;

    public DBConnector(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.myContext = context;
    }


    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[2048];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public synchronized void open() throws Exception {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            tasksDB = getWritableDatabase();
        } else {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            tasksDB = getWritableDatabase();
            copyDataBase();
            tasksDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
    }

    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    @Override
    public synchronized void close() {
        if (tasksDB != null)
            tasksDB.close();
        if (cursor != null)
            cursor.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + "to" + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }

    //taskurile sunt de tipul 1
    public synchronized void insertTask(YTOTask task) {
        ContentValues newCon = new ContentValues();
        newCon.put("Type", "1");
        newCon.put("IdTask", task.getIdTask());
        newCon.put("JSON", task.toJson());


        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tasksDB.insert("Task", null, newCon);
        close();
    }

    public void updateTask(YTOTask task) {
        ContentValues editCon = new ContentValues();
        editCon.put("Type", "1");
        editCon.put("IdTask", task.getIdTask());
        editCon.put("JSON", task.toJson());
        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tasksDB.update("Task", editCon, "IdTask=\"" + task.getIdTask()
                + "\"", null);
        close();
    }

    public void deleteTask(String idTask) {
        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tasksDB.delete("Task", "IdTask=\"" + idTask + "\"", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        close();
    }

    public ArrayList<YTOTask> findAllTasks () {
        ArrayList<YTOTask> tasks = new ArrayList<YTOTask>();
        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            cursor = tasksDB
                    .rawQuery(
                            "SELECT * FROM Task WHERE Type = 1 ;",
                            null);
            cursor.moveToFirst();
            do { String idTask = cursor.getString(1);
                String JSON = cursor.getString(2);
                JSONObject jsn = new JSONObject(JSON);
                YTOTask task = new YTOTask();
                task.setIdTask(idTask);
                task = task.fromJson(jsn);
                tasks.add(task);

            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        close();
        return tasks;
    }


    public ArrayList<YTOTask> findTasksByDate (String date) {
        ArrayList<YTOTask> tasks = new ArrayList<YTOTask>();
        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            cursor = tasksDB
                    .rawQuery(
                            "SELECT * FROM Task where Type = 1;",
                            null);
            cursor.moveToFirst();
            do {String idTask = cursor.getString(1);
                String JSON = cursor.getString(2);
                JSONObject jsn = new JSONObject(JSON);
                YTOTask task = new YTOTask();
                task.setIdTask(idTask);
                task = task.fromJson(jsn);
                if (task.getDate().equals(date))
                    tasks.add(task);

            }
            while (cursor.moveToNext());
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        close();
        return tasks;
    }


    //produsele din lista de cumparaturi sunt de tipul 2
    public synchronized void insertProduct(Product prod) {
        ContentValues newCon = new ContentValues();
        newCon.put("Type", "2");
        newCon.put("IdTask", prod.getIdProd());
        newCon.put("JSON", prod.toJson());


        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tasksDB.insert("Task", null, newCon);
        close();
    }

    public void updateProduct(Product prod) {
        ContentValues editCon = new ContentValues();
        editCon.put("Type", "2");
        editCon.put("IdTask", prod.getIdProd());
        editCon.put("JSON", prod.toJson());
        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tasksDB.update("Task", editCon, "IdTask=\"" + prod.getIdProd()
                + "\"", null);
        close();
    }

    public void deleteProduct(String idProd) {
        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tasksDB.delete("Task", "IdTask=\"" + idProd + "\"", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        close();
    }

    public ArrayList<Product> findAllProducts () {
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            cursor = tasksDB
                    .rawQuery(
                            "SELECT * FROM Task WHERE Type = 2 ;",
                            null);
            cursor.moveToFirst();
            do { String idProd = cursor.getString(1);
                String JSON = cursor.getString(2);
                JSONObject jsn = new JSONObject(JSON);
                Product prod = new Product();
                prod.setIdProd(idProd);
                prod = prod.fromJson(jsn);
                products.add(prod);

            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        close();
        return products;
    }



    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

