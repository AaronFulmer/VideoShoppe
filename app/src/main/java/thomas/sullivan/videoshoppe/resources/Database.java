package thomas.sullivan.videoshoppe.resources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String database = "database.db";

    private static final String employeeTable = "employee";
    private static final String employeeID = "EmployeeID";            // Primary Key
    private static final String employeeFirstName = "firstname";
    private static final String employeeLastName = "lastname";
    private static final String employeeEmail = "email";
    private static final String employeePhoneNumber = "phonenumber";
    private static final String employeeUserName = "username";
    private static final String employeePassword = "password";
    private static final String employeeAdmin = "admin";

    private static final String customerTable = "customer";
    private static final String customerID = "CustomerID";           // Primary Key
    private static final String customerLastName = "lastname";
    private static final String customerFirstName = "firstname";
    private static final String customerEmail = "email";
    private static final String customerCardNumber = "cardNumber";   // Foreign Key
    private static final String customerPhoneNumber = "phoneNumber";

    private static final String cardTable = "card";
    private static final String cardNumber = "number";
    private static final String cardExpDate = "expirationDate";
    private static final String cardSecurityCode = "securityCode";
    private static final String cardType = "cardType";

    private static final String rentalTable = "rental";
    private static final String rentalID = "rentalID";
    private static final String rentalCustomerID = "rentalCustomerID";
    private static final String rentalUPCCode = "UPCCode";             // Foreign Key
    private static final String rentalReturnDate = "returnDate";
    private static final String rentalPrice = "price";

    private static final String dvdTable = "dvd";
    private static final String dvdUPCCode = "UPCCode";                // Primary Key
    private static final String dvdName = "name";
    private static final String dvdReleaseDate = "releaseDate";
    private static final String dvdDirector = "director";
    private static final String dvdGenre = "genre";
    private static final String dvdActors = "actors";
    private static final String dvdCondition = "condition";

    private static final String scheduleTable = "schedule";
    private static final String scheduleDateAndTime = "DateAndTime";   // Primary Key
    private static final String scheduleEmployeeId = "employeeID";     // Foreign Key
    private static final String scheduleHours = "Hours";
    //Yes or No string values



    //Database Default Constructor
    public Database(Context context)
    {
        super(context, database, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        sqLiteDB.execSQL("CREATE TABLE " + employeeTable + " ("
                + employeeID + " TEXT PRIMARY KEY, "
                + employeeLastName + " TEXT, "
                + employeeFirstName + " TEXT, "
                + employeeEmail + " TEXT, "
                + employeePhoneNumber + " TEXT, "
                + employeeUserName + " TEXT, "
                + employeePassword + " TEXT,"
                + employeeAdmin + " BOOLEAN);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+employeeTable);
        onCreate(db);
    }


    public boolean createUser(String ID, String lastName, String firstName, String username,
                              String password, Boolean admin )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(employeeID, ID);
        contentValues.put(employeeLastName, lastName);
        contentValues.put(employeeFirstName, firstName);
        contentValues.put(employeeUserName, username);
        contentValues.put(employeePassword, password);
        contentValues.put(employeeAdmin, admin);
        long result = db.insert(employeeTable,null,contentValues);
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
