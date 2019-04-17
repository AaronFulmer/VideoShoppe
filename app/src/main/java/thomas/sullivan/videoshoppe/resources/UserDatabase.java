package thomas.sullivan.videoshoppe.resources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

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
            createEmployee("ADMIN","Doe","John","ADMIN","ADMIN",true, "555-555-5555","administrator123@test.com");
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
                                  String password, boolean admin, String cellPhoneNumber, String email )

    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(employeeID, ID);
        contentValues.put(employeeLastName, lastName);
        contentValues.put(employeeFirstName, firstName);
        contentValues.put(employeeUserName, username);
        contentValues.put(employeePassword, password);
        contentValues.put(employeeEmail,email);
        contentValues.put(employeePhoneNumber,cellPhoneNumber);

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


    public boolean createCustomer(String ID, String lastName, String firstName, String email,
                                  String phone, String card, String expiration, String security, String type )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValuesCustomer = new ContentValues();
        contentValuesCustomer.put(customerID, ID);
        contentValuesCustomer.put(customerLastName, lastName);
        contentValuesCustomer.put(customerFirstName, firstName);
        contentValuesCustomer.put(customerEmail, email);
        contentValuesCustomer.put(customerPhoneNumber, phone);
        contentValuesCustomer.put(customerCardNumber,card);
        long result = db.insert(customerTable,null,contentValuesCustomer);
        if(result == -1)
        {
            return false;
        }
        else {
            ContentValues contentValuesCard = new ContentValues();
            contentValuesCard.put(cardNumber, card);
            contentValuesCard.put(cardExpDate, expiration);
            contentValuesCard.put(cardSecurityCode, security);
            contentValuesCard.put(cardType,type);
            long resultB = db.insert(cardTable,null,contentValuesCard);
            if(resultB == -1)
            {
                return false;
            } else {
                return true;
            }
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

    public Cursor getAllCustomerData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM customer a INNER JOIN card b ON a.cardNumber=b.number",null);
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

    public String[]customerRowReturn(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] strings = new String[8];
        String tempNumber = "";
        String[] columnsA = {customerLastName,customerFirstName,customerEmail,customerCardNumber,customerPhoneNumber};
        String[] columnsB = {cardExpDate,cardSecurityCode,cardType};
        String whereA = customerID + " = ?";
        String whereB = cardNumber + " = ?";
        String[] argsA = new String[]{id};
        String[] argsB = new String[]{tempNumber};

        Cursor resA = db.query(customerTable, columnsA, whereA, argsA, null, null, null);
        if(resA.moveToFirst())
        {
            strings[0] = resA.getString(resA.getColumnIndex(customerLastName));
            strings[1] = resA.getString(resA.getColumnIndex(customerFirstName));
            strings[2] = resA.getString(resA.getColumnIndex(customerEmail));
            strings[3] = resA.getString(resA.getColumnIndex(customerPhoneNumber));
            strings[4] = resA.getString(resA.getColumnIndex(customerCardNumber));
            tempNumber = resA.getString(resA.getColumnIndex(customerCardNumber));
        }

        Cursor resB = db.query(cardTable, columnsB, whereB, argsB, null, null, null);
        if(resB.moveToFirst())
        {
            strings[5] = resB.getString(resB.getColumnIndex(cardExpDate));
            strings[6] = resB.getString(resB.getColumnIndex(cardSecurityCode));
            strings[7] = resB.getString(resB.getColumnIndex(cardType));
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

    //returns true if the user is deleted; Returns false if user is non-existent.
    public boolean removeCustomer (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String tempCard = "";
        String[] columnsA = {customerID,customerCardNumber};
        String whereA = customerID + " = ?";
        String[] argsA = new String[]{id};
        String[] columnsB = {cardNumber};
        String whereB = cardNumber + " = ?";
        String[] argsB = new String[]{tempCard};
        Cursor resA = db.query(customerTable, columnsA, whereA, argsA, null, null, null);
        if(resA.moveToFirst())
        {
            tempCard = resA.getString(resA.getColumnIndex(customerCardNumber));
            db.execSQL("DELETE FROM "+customerTable+" WHERE "+customerID+"='"+id+"'");
            Cursor resB = db.query(cardTable, columnsB, whereB, argsB, null, null, null);
            if(resB.moveToFirst()) {
                db.execSQL("DELETE FROM "+cardTable+" WHERE "+cardNumber+"='"+tempCard+"'");
                return true;
            } else {
                return false;
            }
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

    public static String getFinanceTable(){ return financeTable; }

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

    /*
    *  Search Database Method
    *       The idea is that the function checks for perfect matches in the database first, then uses a
    *       brute force string matching algorithm to find the highest number of common characters between
    *       the search terms and the string from the database. It then adds them to an arraylist after
    *       sorting them based on number of common characters.
    *
    *  - Retrieve entire database in the form of a single cursor
    *  - Check each row of the targeted filter for a perfect match of the search terms
    *       - push matches to stack to mark row number as used
    *  - Reset cursor to top of table
    *  - Run a brute force string matching algorithm to determine the highest number of common characters in the search terms and the filtered column
    *  - Put the highest number of common characters for the search terms in a 2D array with row numbers for later sorting
    *  - After the common characters are counted and stored, use Arrays.sort to sort them by number of common characters
    *  - Using the stored row numbers, add all of the rows to the arraylist in order of match quality
    *  - Return arraylist
    *
    * */
    public ArrayList<ArrayList<String>> searchDatabase(String term, String filter){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + dvdTable, null);   //  Get a readable database
        String[] columns = c.getColumnNames();                                      //  Get the column names (mainly to get the number of columns)
        int target = c.getColumnIndex(filter);                                      //  Get the target column index from what the search is filtered by
        ArrayList<ArrayList<String>> list = new ArrayList<>();                      //  Results will be stored in a 2D ArrayList of Strings
        int rows = c.getCount();                                                    //  Get the number of rows in the cursor
        Stack used = new Stack();                                                   //  Make a stack to hold the used row indices

        if(!c.moveToFirst()){
            return list;
        }

        for(int a = 0; a < rows; a++){
            String test = c.getString(target);
            if(test.contains(term)){
                used.push(a);
                list.add(new ArrayList<String>());
                for(int b = 0; b < columns.length - 1; b++){
                    list.get(list.size() - 1).add(c.getString(b));
                }
            }
            c.moveToNext();
        }

        int commonChars = 0;
        c.moveToFirst();
        String[] terms = term.split(" ");
        int[][] chars = new int[2][rows];
        for(int a = 0; a < rows; a++){
            chars[0][a] = a;
            if(!used.contains(a)) {
                String test = c.getString(target);
                for (int e = 0; e < terms.length; e++) {
                    for (int b = 0; b < test.length() - terms[e].length(); b++) {
                        commonChars = 0;
                        for (int d = 0; d < terms[e].length(); d++) {
                            if (test.charAt(d+b) == terms[e].charAt(d)) {
                                commonChars++;
                            }
                        }
                        if (commonChars > chars[1][a]) chars[1][a] = commonChars;
                    }
                }
            }
            c.moveToNext();
        }
        c.moveToFirst();
        Arrays.sort(chars);
        c.move(chars[0][0]);
        list.add(new ArrayList<String>());
        for(int a = 0; a < columns.length; a++){
            list.get(list.size() - 1).add(c.getString(a));
        }

        for(int a = 1; a < rows; a++){
            int offset = chars[0][a] - chars[0][a-1];
            c.move(offset);
            list.add(new ArrayList<String>());
            for(int b = 0; b < columns.length; b++){
                list.get(list.size() - 1).add(c.getString(b));
            }
        }

        return list;
    }

}

