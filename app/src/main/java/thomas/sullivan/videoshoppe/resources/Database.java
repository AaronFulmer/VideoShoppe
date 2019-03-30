package thomas.sullivan.videoshoppe.resources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE = "database.db";
    public static final String USERS = "USERS";
    public static final String IDNUM = "ID";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    //Yes or No string values
    public static final String ADMIN = "ADMIN";
    public static final String[] COLUMNS = {IDNUM,LAST_NAME,FIRST_NAME,USERNAME,PASSWORD,ADMIN};
    public static String loggedInUserFirstName = "";
    public static String loggedInUserLastName = "";

    //Database Default Constructor
    public Database(Context context)
    {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+USERS+" (ID TEXT, LAST_NAME TEXT, FIRST_NAME TEXT, USERNAME TEXT, PASSWORD TEXT, ADMIN TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+USERS);
        onCreate(db);
    }


    public boolean createUser(String ID, String lastName, String firstName, String username, String password, String admin )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IDNUM, ID);
        contentValues.put(LAST_NAME, lastName);
        contentValues.put(FIRST_NAME, firstName);
        contentValues.put(USERNAME, username);
        contentValues.put(PASSWORD, password);
        contentValues.put(ADMIN, admin);
        long result = db.insert(USERS,null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public void wipeDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        onCreate(db);
        createUser("ADMIN","Doe","John","ADMIN","ADMIN","YES");
    }

    public String debugger()
    {
        String result = "";
        String query = "SELECT * FROM "+USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        while(cursor.moveToNext())
        {
            String id = cursor.getString(0);
            String lastname = cursor.getString(1);
            String firstname = cursor.getString(2);
            String username = cursor.getString(3);
            String password = cursor.getString(4);
            String admin = cursor.getString(5);

            result += id+" "+lastname+" "+firstname+" "+username+" "+password+" "+admin;
        }
        cursor.close();
        db.close();
        return result;
    }

    public String getLoggedInUserFirstName()
    {
        return loggedInUserFirstName;
    }

    public String getLoggedInUserLastName()
    {
        return loggedInUserLastName;
    }

    public boolean searchUsername(String user)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM  USERS WHERE USERNAME= ? COLLATE NOCASE",new String[] {user});
        if(res.moveToFirst())
        {
            String testUsername = res.getString(3);
            if(user.equalsIgnoreCase(testUsername))
            {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean searchPassword(String user, String pass)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM  USERS WHERE USERNAME= ? COLLATE NOCASE",new String[] {user});
        if(res.moveToFirst())
        {
            String testPassword = res.getString(4);
            if(testPassword.equals(pass))
            {
                loggedInUserFirstName = res.getString(2);
                loggedInUserLastName = res.getString(1);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public boolean isAdmin(String employeeID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ADMIN from "+USERS+" where ID="+employeeID,null);
        String adminCheck = res.getString(0);
        if(adminCheck.equalsIgnoreCase("Yes"))
        {
            return true;
        } else {
            return false;
        }
    }


    public Cursor getAllData() {
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = this.getReadableDatabase().rawQuery("select * from "+USERS,null);
        return res;
    }

    //Updates User's data in the database
    public boolean updateUser(String ID, String lastName, String firstName, String username, String password, String admin ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, ID);
        contentValues.put(LAST_NAME, lastName);
        contentValues.put(FIRST_NAME, firstName);
        contentValues.put(USERNAME, username);
        contentValues.put(PASSWORD, password);
        contentValues.put(ADMIN, admin);
        db.update(USERS, contentValues, "ID = ?",new String[] { ID });
        return true;
    }

    //returns true if the user is deleted; Returns false if user is non-existent.
    public boolean deleteUser (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int numberOfDeletedRows = db.delete(USERS, "ID = ?",new String[] {id});

        if(numberOfDeletedRows > 0) {
            return true;
        }
        else {
            return false;
        }

    }


}
