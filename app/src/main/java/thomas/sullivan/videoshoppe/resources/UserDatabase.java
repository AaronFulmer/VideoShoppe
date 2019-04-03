package thomas.sullivan.videoshoppe.resources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                                  String password, boolean admin )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(employeeID, ID);
        contentValues.put(employeeLastName, lastName);
        contentValues.put(employeeFirstName, firstName);
        contentValues.put(employeeUserName, username);
        contentValues.put(employeePassword, password);
        contentValues.put(employeeAdmin, (admin)? 1 : 0);
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

    public String searchCredentials(String user, String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {employeeID};
        String where = employeePassword + " = ? and " + employeeUserName + " = ?";
        String[] args = new String[]{pass, user};
        Cursor c = db.query(employeeTable, columns, where, args, null, null, null);

        if(!c.moveToFirst()){
            return "invalid";
        }
        return c.getString(0);
    }


    public boolean isAdmin(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {employeeAdmin};
        String where = employeeID + " = ?";
        String[] args = new String[]{id};
        Cursor res = db.query(employeeTable, columns, where, args, null, null, null);
        Boolean adminCheck = res.getInt(0) > 0;
        return adminCheck;
    }


    public Cursor getAllEmployeeData() {
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = this.getReadableDatabase().rawQuery("select * from "+employeeTable,null);
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
    public boolean updateEmployee(String table, String ID, String lastName, String firstName,
                                  String username, String password, String admin ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(employeeLastName, lastName);
        contentValues.put(employeeFirstName, firstName);
        contentValues.put(employeeUserName, username);
        contentValues.put(employeePassword, password);
        contentValues.put(employeeAdmin, admin);
        db.update(table, contentValues, employeeID + " = ?",new String[] { ID });
        return true;
    }

    //returns true if the user is deleted; Returns false if user is non-existent.
    public boolean removeEmployee (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int numberOfDeletedRows = db.delete(employeeTable, employeeID + " = ?",new String[] {id});

        if(numberOfDeletedRows > 0) {
            return true;
        }
        else {
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

    public String getLoggedInUserFirstName(){
        return employeeFirstName;
    }

    public String getLoggedInUserLastName(){
        return employeeLastName;
    }

    public String debugger(){
        return "temp";
    }
}
