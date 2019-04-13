package thomas.sullivan.videoshoppe.resources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void setCurrentUser(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {employeeFirstName, employeeLastName};
        String where = employeeID + " = ?";
        String[] args = {id};
        Cursor c = db.query(employeeTable, columns, where, args, null, null, null);

        if(c.moveToFirst()){
            currentEmployeeLastName = c.getString(0);
            currentEmployeeFirstName = c.getString(1);
        }
        else{
            currentEmployeeLastName = "";
            currentEmployeeFirstName = "ERROR";
        }
    }

    public String getLoggedInUserFirstName(){
        return currentEmployeeFirstName;
    }

    public String getLoggedInUserLastName(){
        return currentEmployeeLastName;
    }

    public String debugger(){
        return "temp";
    }



    /* This method is meant to search the database for some given terms that are passed in as arguments.
     *
     * Variables:
     *      a: acts as a counter for the number of rows in the cursor to make sure that all rows are checked for each test
     *      b: acts as a counter for the number of columns in the cursor to make sure that the columns are copied in their entirety
     *      d: acts as a counter for the number of words in the search terms to ensure every word is searched for
     *
     *  There are 3 tests that are run:
     *      1: Test if the current string from the cursor matches the search terms
     *      2: Test if the current string from the cursor contains an individual term
     *      3: Test if the current string from the cursor contains an individual term if all spaces are removed
     *
     *  Whenever a match is found, the row index is pushed to the Stack so that subsequent tests shouldn't return the same rows repeatedly.
     *  The tests are structured in such a way to prioritize better matches at the top of the list.
     *
     *  The tests are first run using the whole search terms string, then the string is broken up
     *  into its individual words and the tests are run again using each individual word to search with.
     */
    public ArrayList<ArrayList<String>> searchDvdv1(String terms, String filter) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + dvdTable, null);   //  Get a readable database
        String[] columns = c.getColumnNames();                                      //  Get the column names (mainly to get the number of columns)
        int target = c.getColumnIndex(filter);                                      //  Get the target column index from what the search is filtered by
        ArrayList<ArrayList<String>> list = new ArrayList<>();                      //  Results will be stored in a 2D ArrayList of Strings
        int rows = c.getCount();                                                    //  Get the number of rows in the cursor
        Stack used = new Stack();                                                   //  Make a stack to hold the used row indices

        //  Copy the column names into the first row of the ArrayList
        list.add(new ArrayList<String>());
        for(int a = 0; a < columns.length; a++){
            list.get(0).add(columns[a]);
        }

        // If the cursor has any results in it...
        if (c.moveToFirst()) {

            // Begin by testing that the datum is equal to the search terms
            for (int a = 0; a < rows; a++) {
                String data = c.getString(target);                                //  Get the new string from the cursor
                if (data.equals(terms) && !used.contains(a)) {
                    used.push(a);                                                 //  Push the row index into the stack to mark as used
                    list.add(new ArrayList<String>());                            //  Prep the list for new entry
                    for (int b = 0; b < columns.length; b++) {
                        list.get(list.size() - 1).add(c.getString(b));            //  Copy the contents of the current row of the cursor into the list
                    }
                }

                c.moveToNext();                                                   //  Move to the next row in the cursor
            }

            c.moveToFirst();                                                      //  Reset the cursor

            // Next, check if datum contains the search terms as a whole string
            for (int a = 0; a < rows; a++) {
                String data = c.getString(target);                                //  Get the new string from the cursor
                if (data.contains(terms) && !used.contains(a)) {
                    used.push(a);                                                 //  Push the row index into the stack to mark as used
                    list.add(new ArrayList<String>());                            //  Prep the list for new entry
                    for (int b = 0; b < columns.length; b++) {
                        list.get(list.size() - 1).add(c.getString(b));            //  Copy the contents of the current row of the cursor into the list
                    }
                }
                c.moveToNext();                                                   //  Move to the next row of the cursor
            }

            c.moveToFirst();                                                      //  Reset the cursor

            // Next, check if datum (excluding spaces) contains the terms as a whole string
            for (int a = 0; a < rows; a++) {
                String data = c.getString(target);                                //  Get the new string from the cursor
                data.replaceAll("\\s", "");                      //  Remove the spaces from the string
                if (data.contains(terms) && !used.contains(a)) {
                    used.push(a);                                                //  Push the row index into the stack to mark as used
                    list.add(new ArrayList<String>());                           //  Prep the list for new entry
                    for (int b = 0; b < columns.length; b++) {
                        list.get(list.size() - 1).add(c.getString(b));           //  Copy the contents of the current row of the cursor into the list
                    }
                }
                c.moveToNext();                                                  //  Move to the next row of the cursor
            }

            c.moveToFirst();                                                     //  Reset the cursor

            /*
             *  The following loops run the previous tests again using the individual words as search terms
             *  in and of themselves
             */

            String[] termList = terms.split(" ");                          //  Split the search term string into separate words

            // Begin with looking for perfectly matching strings
            // If any are found, push the index to the stack and copy all data into the list
            for (int d = 0; d < termList.length; d++) {
                for (int a = 0; a < rows; a++) {
                    String data = c.getString(target);
                    if (data.equals(termList[d]) && !used.contains(a)) {
                        used.push(a);
                        list.add(new ArrayList<String>());
                        for (int b = 0; b < columns.length; b++) {
                            list.get(list.size() - 1).add(c.getString(b));
                        }
                    }

                    c.moveToNext();
                }
            }

            c.moveToFirst();

            // Next, check if data has any of the terms as full words
            for (int d = 0; d < termList.length; d++) {
                for (int a = 0; a < rows; a++) {
                    String data = c.getString(target);
                    if (data.contains(termList[d]) && !used.contains(a)) {
                        used.push(a);
                        list.add(new ArrayList<String>());
                        for (int b = 0; b < columns.length; b++) {
                            list.get(list.size() - 1).add(c.getString(b));
                        }
                    }
                    c.moveToNext();
                }
            }

            c.moveToFirst();

            // Next, check if data (excluding spaces) contains the terms at all
            for (int d = 0; d < termList.length; d++) {
                for (int a = 0; a < rows; a++) {
                    String data = c.getString(target);
                    data.replaceAll("\\s", "");
                    if (data.contains(termList[d]) && !used.contains(a)) {
                        used.push(a);
                        list.add(new ArrayList<String>());
                        for (int b = 0; b < columns.length; b++) {
                            list.get(list.size() - 1).add(c.getString(b));
                        }
                    }
                    c.moveToNext();
                }

            c.moveToFirst();

            }

        }
        return list;
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
                        for (int d = 0; d < terms[e].length(); d++) {
                            if (test.charAt(d+b) == terms[e].charAt(d)) {
                                commonChars++;
                            }
                        }
                    }
                    if (commonChars > chars[1][a]) chars[1][a] = commonChars;
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
