package thomas.sullivan.videoshoppe.resources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class UserDatabase extends SQLiteOpenHelper {

    private static final String DB_Name = "videoshoppe.db";

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
    private static final String dvdMovieID = "id";
    private static final String dvdReleaseDate = "releaseDate";
    private static final String dvdDirector = "director";
    private static final String dvdGenre = "genre";
    private static final String dvdCondition = "condition";

    private static final String scheduleTable = "schedule";
    private static final String scheduleDateAndTime = "DateAndTime";   // Primary Key
    private static final String scheduleEmployeeId = "employeeID";     // Foreign Key
    private static final String scheduleHours = "Hours";

    private static final String actorsTable = "actors";
    private static final String actorsName = "name";
    private static final String actorsMovieID = "movie_id";

    private static final String financeTable = "finances";
    private static final String financeExpenditures = "expenditures";
    private static final String financeRevenue = "revenue";
    private static final String financeProfit = "profit";
    private static final String financeTransactionId = "transaction_number";
    private static final String financeTransactionDate = "date";

    private static String currentEmployeeFirstName = "";
    private static String currentEmployeeLastName = "";
    private static String currentEmployeeUserID = "";

    //Database Default Constructor
    public UserDatabase(Context context)
    {
        super(context, DB_Name, null, 1);
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
                + employeePassword + " TEXT, "
                + employeeAdmin + " INTEGER);");

        sqLiteDB.execSQL("CREATE TABLE " + customerTable + " ("
                + customerID + " TEXT PRIMARY KEY, "
                + customerLastName + " TEXT, "
                + customerFirstName + " TEXT, "
                + customerPhoneNumber + " TEXT, "
                + customerEmail + " TEXT, "
                + customerCardNumber + " TEXT);");

        sqLiteDB.execSQL("CREATE TABLE " + dvdTable + " ("
                + dvdUPCCode + " TEXT PRIMARY KEY, "
                + dvdMovieID + " TEXT, "
                + dvdName + " TEXT, "
                + dvdDirector + " TEXT, "
                + dvdCondition + " TEXT, "
                + dvdReleaseDate + " TEXT, "
                + dvdGenre + " TEXT);");

        sqLiteDB.execSQL("CREATE TABLE " + cardTable + " ("
                + cardNumber + " TEXT PRIMARY KEY, "
                + cardSecurityCode + " TEXT, "
                + cardExpDate + " TEXT, "
                + cardType + " TEXT);");

        sqLiteDB.execSQL("CREATE TABLE " + rentalTable + " ("
                + rentalID + " TEXT PRIMARY KEY, "
                + rentalCustomerID + " TEXT, "
                + rentalUPCCode + " TEXT, "
                + rentalReturnDate + " TEXT, "
                + rentalPrice + " TEXT);");

        sqLiteDB.execSQL("CREATE TABLE " + scheduleTable + " ("
                + scheduleDateAndTime + " DATE PRIMARY KEY, "
                + scheduleEmployeeId + " TEXT, "
                + scheduleHours + " TEXT);");

        sqLiteDB.execSQL("CREATE TABLE " + actorsTable + " ("
                + actorsMovieID + " TEXT, "
                + actorsName + " TEXT);");

        sqLiteDB.execSQL("CREATE TABLE " + financeTable + " ("
                + financeTransactionId + " TEXT PRIMARY KEY, "
                + financeTransactionDate + " DATE, "
                + financeRevenue + " DOUBLE, "
                + financeExpenditures + " DOUBLE, "
                + financeProfit + " DOUBLE);");

        if(!searchCredentials("ADMIN","ADMIN"))
        {
            createEmployee("ADMIN","Doe","John","ADMIN","ADMIN",1, "555-555-5555","administrator123@test.com");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+employeeTable);
        db.execSQL("DROP TABLE IF EXISTS "+customerTable);
        db.execSQL("DROP TABLE IF EXISTS "+actorsTable);
        db.execSQL("DROP TABLE IF EXISTS "+dvdTable);
        db.execSQL("DROP TABLE IF EXISTS "+rentalTable);
        db.execSQL("DROP TABLE IF EXISTS "+scheduleTable);
        db.execSQL("DROP TABLE IF EXISTS "+cardTable);
        db.execSQL("DROP TABLE IF EXISTS "+financeTable);

        onCreate(db);
    }


    public boolean createEmployee(String ID, String lastName, String firstName, String username,
                                  String password, int admin, String cellPhoneNumber, String email )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(employeeID, ID);
        contentValues.put(employeeLastName, lastName);
        contentValues.put(employeeFirstName, firstName);
        contentValues.put(employeeUserName, username);
        contentValues.put(employeePassword, password);
        if(admin == 0 || admin == 1)
        {
            contentValues.put(employeeAdmin, admin);
        }
        contentValues.put(employeeEmail,email);
        contentValues.put(employeePhoneNumber,cellPhoneNumber);
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
        onUpgrade(this.getWritableDatabase(), 1, 1);
    }

    public Boolean searchCredentials(String user, String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {employeeID};
        //"COLLATE NOCASE" ignores case sensitivity, in this case ignores username case sensitivity
        String where = employeePassword + " = ?  and " + employeeUserName + " = ? COLLATE NOCASE";
        String[] args = new String[]{pass, user};
        Cursor c = db.query(employeeTable, columns, where, args, null, null, null);

        if(c.moveToFirst()){
            return true;
        } else {
            return false;
        }
    }


    public boolean isAdmin(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {employeeAdmin};
        String where = employeeID + " = ?";
        String[] args = new String[]{id};
        Cursor res = db.query(employeeTable, columns, where, args, null, null, null);
        int adminTest;
        if(res.moveToFirst())
        {
           adminTest = res.getInt(res.getColumnIndex(employeeAdmin));
           if(adminTest == 1) {
               return true;
           } else {
               return false;
           }
        } else {
            return false;
        }
    }


    public Cursor getAllEmployeeData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+employeeTable,null);
        return res;
    }

    public Boolean insertIntoTable(String table, String[] columns, String[] values){
        ContentValues c = new ContentValues();
        for(int a = 0; a < values.length; a++){
            c.put(columns[a], values[a]);
        }
        long result = this.getWritableDatabase().insert(table, null, c);
        if(result == -1){
            return false;
        }
        return true;
    }

    //Updates employee's data in the database
    public boolean updateEmployee(String ID, String lastName, String firstName,
                                  String username, String password, int admin ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(employeeLastName, lastName);
        contentValues.put(employeeFirstName, firstName);
        contentValues.put(employeeUserName, username);
        contentValues.put(employeePassword, password);
        contentValues.put(employeeAdmin, admin);
        db.update(employeeTable, contentValues, employeeID + " = ?",new String[] { ID });
        return true;
    }

    public String[] employeeRowReturn(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] strings = new String[7];
        String[] columns = {employeeLastName,employeeFirstName,employeeUserName,employeePassword,employeeAdmin,employeeEmail,employeePhoneNumber};
        String where = employeeID + " = ?";
        String[] args = new String[]{id};
        Cursor res = db.query(employeeTable, columns, where, args, null, null, null);
        if(res.moveToFirst())
        {
            strings[0] = res.getString(res.getColumnIndex(employeeLastName));
            strings[1] = res.getString(res.getColumnIndex(employeeFirstName));
            strings[2] = res.getString(res.getColumnIndex(employeeUserName));
            strings[3] = res.getString(res.getColumnIndex(employeePassword));
            strings[4] = ""+res.getInt(res.getColumnIndex(employeeAdmin));
            strings[5] = res.getString(res.getColumnIndex(employeeEmail));
            strings[6] = res.getString(res.getColumnIndex(employeePhoneNumber));
        }
        return strings;
    }

    //returns true if the user is deleted; Returns false if user is non-existent.
    public boolean removeEmployee (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {employeeID};
        String where = employeeID + " = ?";
        String[] args = new String[]{id};
        Cursor res = db.query(employeeTable, columns, where, args, null, null, null);
        if(res.moveToFirst())
        {
            db.execSQL("DELETE FROM "+employeeTable+" WHERE "+employeeID+"='"+id+"'");
            return true;
        } else {
            return false;
        }

    }

    public static String getEmployeeTable() {
        return employeeTable;
    }

    public static String getCustomerTable() {
        return customerTable;
    }

    public static String getCardTable() {
        return cardTable;
    }

    public static String getRentalTable() {
        return rentalTable;
    }

    public static String getDvdTable() {
        return dvdTable;
    }

    public static String getScheduleTable() {
        return scheduleTable;
    }

    public static String getActorsTable() {
        return actorsTable;
    }

    public static String[] getEmployeeAttributes(){
        return new String[]{employeeID, employeeLastName, employeeFirstName, employeeEmail,
                employeePhoneNumber, employeeUserName, employeePassword, employeeAdmin};
    }

    public static String[] getCustomerAttributes(){
        return new String[]{customerID, customerLastName, customerFirstName, customerPhoneNumber,
                customerEmail, customerCardNumber};
    }

    public static String[] getDvdAttributes(){
        return new String[]{dvdUPCCode, dvdMovieID, dvdName, dvdDirector, dvdCondition, dvdReleaseDate,
                dvdGenre};
    }

    public static String[] getCardAttributes(){
        return new String[]{cardNumber, cardSecurityCode, cardExpDate, cardType};
    }

    public static String[] getRentalAttributes(){
        return new String[]{scheduleDateAndTime, scheduleEmployeeId, scheduleHours};
    }

    public static String[] getActorsAttributes(){
        return new String[]{actorsMovieID, actorsName};
    }

    public static String[] getFinanceAttributes(){
        return new String[]{financeTransactionId, financeTransactionDate, financeRevenue,
                financeExpenditures, financeProfit};
    }

    public void setCurrentUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {employeeFirstName, employeeLastName,employeeID};
        String where = employeeUserName + " = ? COLLATE NOCASE";
        String[] args = {username};
        Cursor c = db.query(employeeTable, columns, where, args, null, null, null);

        if(c.moveToFirst()){
            currentEmployeeLastName = c.getString(c.getColumnIndex(employeeLastName));
            currentEmployeeFirstName = c.getString(c.getColumnIndex(employeeFirstName));
            currentEmployeeUserID = c.getString(c.getColumnIndex(employeeID));

        }
        else{
            currentEmployeeLastName = "";
            currentEmployeeFirstName = "ERROR";
            currentEmployeeUserID = "";
        }
    }

    public String getLoggedInUserFirstName(){
        return currentEmployeeFirstName;
    }

    public String getLoggedInUserLastName(){
        return currentEmployeeLastName;
    }




    public String getLoggedInUserID(){
        return currentEmployeeUserID;
    }

    public String debugger(){
        return "temp";
    }
}