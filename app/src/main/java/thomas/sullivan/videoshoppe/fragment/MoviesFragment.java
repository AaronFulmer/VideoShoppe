package thomas.sullivan.videoshoppe.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import thomas.sullivan.videoshoppe.activity.R;
import thomas.sullivan.videoshoppe.resources.CustomerItem;
import thomas.sullivan.videoshoppe.resources.InventoryItem;
import thomas.sullivan.videoshoppe.resources.RentalItem;
import thomas.sullivan.videoshoppe.resources.UserDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment implements android.widget.SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    public class MovieListAdapter extends BaseExpandableListAdapter {

        ArrayList<String> parents;
        HashMap<String, ArrayList<String>> children;
        Context context;

        public MovieListAdapter(Context context, ArrayList<String> aparents,
                                    HashMap<String, ArrayList<String>> achildren) {
            this.context = context;
            this.parents = aparents;
            this.children = achildren;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this.children.get(this.parents.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String upc = (String) getChild(groupPosition, 0);
            final String checkIn = (String) getChild(groupPosition, 1);
            final String custId = (String) getChild(groupPosition, 2);
            final String price = (String) getChild(groupPosition, 3);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.row_movie_layout_expandable, null);
            }

            TextView txtUPC = (TextView) convertView.findViewById(R.id.txtUpc);
            TextView txtCheckIn = (TextView) convertView.findViewById(R.id.txtCheckIn);
            TextView txtCustId = (TextView) convertView.findViewById(R.id.txtCustId);
            TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);


            txtUPC.setText(upc);
            txtCheckIn.setText(checkIn);
            txtCustId.setText(custId);
            txtPrice.setText(price);

//            final String tempCustomerID = customers.get(currentSelectedGroup).getiD();
//            final String tempName = customers.get(currentSelectedGroup).getFirstName()+" "+customers.get(currentSelectedGroup).getLastName();



            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.parents.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.parents.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.row_inventory_layout, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.inventoryListHeader);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchView.OnQueryTextListener queryTextListener;

    // upc = default/0, check out = 1, check in = 2, movie name = 3 cust id = 4
    int spinnerSelection;

    private int lastExpandedPosition = -1;
    private int currentSelectedGroup;
    UserDatabase db;
    ExpandableListView rentalList;
    View view;
    ArrayList<RentalItem> rentals;
    MovieListAdapter adapter;
    HashMap<String, ArrayList<String>> rentalInformation;

    private OnFragmentInteractionListener mListener;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesFragment newInstance(String param1, String param2) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        db = new UserDatabase(getContext());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rentals, null);
        rentalList = (ExpandableListView) view.findViewById(R.id.lstRentals);

        Spinner spinner = (Spinner) view.findViewById(R.id.rentalsFilter);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.rental_filters, R.layout.rental_filter);
        arrayAdapter.setDropDownViewResource(R.layout.rental_filter);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        android.widget.SearchView searchView = (android.widget.SearchView) view.findViewById(R.id.action_search_rentals);
        searchView.setOnQueryTextListener(this);

        FloatingActionButton fab = view.findViewById(R.id.moviesfab);
        FloatingActionButton fab2 = view.findViewById(R.id.checkIn);

        fab2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final AlertDialog.Builder addRentalDialog = new AlertDialog.Builder(getContext());
                final View view4 = getLayoutInflater().inflate(R.layout.add_rental_popup, null);
                addRentalDialog.setView(view4);
                final AlertDialog add = addRentalDialog.create();
                add.show();

                final EditText txtUPC = view4.findViewById(R.id.txtUPC);
                Button addSearch = (Button) view4.findViewById(R.id.btnRentalSearch);
                Button addCancel = (Button) view4.findViewById(R.id.btnRentalCancel);

                addCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.dismiss();
                    }
                });
                addSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!txtUPC.getText().toString().isEmpty()) {
                            String[] customerTempInfo = db.inventoryRowReturn(txtUPC.getText().toString());
                            if (customerTempInfo[0] != null) {

                                final AlertDialog.Builder verifyDVDDialog = new AlertDialog.Builder(getContext());
                                final View view4 = getLayoutInflater().inflate(R.layout.verify_dvd_rental, null);
                                verifyDVDDialog.setView(view4);
                                final AlertDialog editor = verifyDVDDialog.create();
                                editor.show();

                                final TextView tempMovieName = view4.findViewById(R.id.txtMovieName);
                                final TextView tempUPCCode = view4.findViewById(R.id.txtUPCCode);
                                final TextView tempMovieID = view4.findViewById(R.id.txtMovieID);
                                final TextView tempReleaseDate = view4.findViewById(R.id.txtReleaseDate);
                                final TextView tempGenre = view4.findViewById(R.id.txtGenre);
                                final TextView tempDirector = view4.findViewById(R.id.txtDirector);
                                final CheckBox tempCondition = view4.findViewById(R.id.chkGoodCondition);

                                Button btnContinue = (Button) view4.findViewById(R.id.btnRentalVerifyContinue);
                                Button btnCancel = (Button) view4.findViewById(R.id.btnRentalVerifyCancel);


                                if (customerTempInfo[0] != null) {
                                    tempUPCCode.setText(customerTempInfo[0]);
                                    tempMovieID.setText(customerTempInfo[1]);
                                    tempMovieName.setText(customerTempInfo[2]);
                                    tempDirector.setText(customerTempInfo[3]);
                                    tempCondition.setChecked(customerTempInfo[4].equals("good"));
                                    tempReleaseDate.setText(customerTempInfo[5]);
                                    tempGenre.setText(customerTempInfo[6]);
                                }

                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editor.dismiss();
                                    }
                                });

                                btnContinue.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String[] rentalColumns = db.rentalRowReturn(tempUPCCode.getText().toString());
                                        db.decrementRentals(rentalColumns[1]);
                                        db.changeStatus(tempUPCCode.getText().toString(), true);

                                        String[] custColumns = db.customerRowReturn(rentalColumns[1]);
                                        String cardNum = custColumns[4].substring(11);

                                        final DateFormat d = new SimpleDateFormat("MM/dd/yyyy");
                                        final Calendar cal = Calendar.getInstance();
                                        cal.setTime(new Date()); // Now use today date.

                                        db.addTransaction(rentalColumns[0], rentalColumns[4], rentalColumns[1], d.format(cal.getTime()));
                                        Toast.makeText(getContext(), "Card ************" + cardNum + " charged $" + rentalColumns[4], Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Invalid UPC", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder addRentalDialog = new AlertDialog.Builder(getContext());
                final View view4 = getLayoutInflater().inflate(R.layout.add_rental_popup, null);
                addRentalDialog.setView(view4);
                final AlertDialog add = addRentalDialog.create();
                add.show();

                final EditText txtUPC = view4.findViewById(R.id.txtUPC);
//                final EditText tempExpiration = (EditText) view4.findViewById(R.id.add_customer_ExpirationDate);
//                final EditText tempType = (EditText) view4.findViewById(R.id.add_customer_cardType);
//                final EditText tempCustomerID = (EditText) view4.findViewById(R.id.add_customer_ID);


                Button addSearch = (Button) view4.findViewById(R.id.btnRentalSearch);
                Button addCancel = (Button) view4.findViewById(R.id.btnRentalCancel);

                addSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!txtUPC.getText().toString().isEmpty()) {
                            String[] customerTempInfo = db.inventoryRowReturn(txtUPC.getText().toString());
                            if (Integer.parseInt(customerTempInfo[7]) == 0) {
                                Toast.makeText(getContext(), "Movie out of stock", Toast.LENGTH_SHORT).show();
                            }
                            else if (customerTempInfo[0] != null) {

                                final AlertDialog.Builder verifyDVDDialog = new AlertDialog.Builder(getContext());
                                final View view4 = getLayoutInflater().inflate(R.layout.verify_dvd_rental, null);
                                verifyDVDDialog.setView(view4);
                                final AlertDialog editor = verifyDVDDialog.create();
                                editor.show();

                                final TextView tempMovieName = view4.findViewById(R.id.txtMovieName);
                                final TextView tempUPCCode = view4.findViewById(R.id.txtUPCCode);
                                final TextView tempMovieID = view4.findViewById(R.id.txtMovieID);
                                final TextView tempReleaseDate = view4.findViewById(R.id.txtReleaseDate);
                                final TextView tempGenre = view4.findViewById(R.id.txtGenre);
                                final TextView tempDirector = view4.findViewById(R.id.txtDirector);
                                final CheckBox tempCondition = view4.findViewById(R.id.chkGoodCondition);

                                Button btnContinue = (Button) view4.findViewById(R.id.btnRentalVerifyContinue);
                                Button btnCancel = (Button) view4.findViewById(R.id.btnRentalVerifyCancel);


                                if (customerTempInfo[0] != null) {
                                    tempUPCCode.setText(customerTempInfo[0]);
                                    tempMovieID.setText(customerTempInfo[1]);
                                    tempMovieName.setText(customerTempInfo[2]);
                                    tempDirector.setText(customerTempInfo[3]);
                                    tempCondition.setChecked(customerTempInfo[4].equals("good"));
                                    tempReleaseDate.setText(customerTempInfo[5]);
                                    tempGenre.setText(customerTempInfo[6]);
                                }

                                btnContinue.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        final AlertDialog.Builder verifyCustDialog = new AlertDialog.Builder(getContext());
                                        final View view6 = getLayoutInflater().inflate(R.layout.check_customer, null);
                                        verifyCustDialog.setView(view6);
                                        final AlertDialog editor = verifyCustDialog.create();
                                        editor.show();

                                        final TextView txtCustId = view6.findViewById(R.id.txtCustID);

                                        Button search = view6.findViewById(R.id.btnCustSearch);
                                        Button cancel = view6.findViewById(R.id.btnCancel);

                                        search.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Cursor c = db.getNumOfRentals(txtCustId.getText().toString());

                                                if (!c.moveToFirst()) {
                                                    Toast.makeText(getContext(), "Invalid ID", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    int num = c.getInt(0);
                                                    if (num >= 2) {
                                                        Toast.makeText(getContext(), "Customer has maximum amount of DVDs rented already", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        final AlertDialog.Builder verifyCustDialog = new AlertDialog.Builder(getContext());
                                                        final View view7 = getLayoutInflater().inflate(R.layout.verify_dates, null);
                                                        verifyCustDialog.setView(view7);
                                                        final AlertDialog editor = verifyCustDialog.create();
                                                        editor.show();

                                                        final TextView txtCheckOut = view7.findViewById(R.id.txtCheckOut);
                                                        final TextView txtCheckIn = view7.findViewById(R.id.txtCheckIn);

                                                        Button confirm = view7.findViewById(R.id.btnRentalConfirm);
                                                        Button cancel = view7.findViewById(R.id.btnCancel);
                                                        final DateFormat d = new SimpleDateFormat("MM/dd/yyyy");
                                                        final Calendar cal = Calendar.getInstance();
                                                        cal.setTime(new Date()); // Now use today date.

                                                        txtCheckOut.setText(d.format(cal.getTime()));
                                                        cal.add(Calendar.DATE, 7); // Adds 7 days
                                                        txtCheckIn.setText(d.format(cal.getTime()));

                                                        confirm.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (db.addRental(txtUPC.getText().toString(), txtCustId.getText().toString(),
                                                                        d.format(cal.getTime()), txtCheckOut.getText().toString())) {
                                                                    db.incrementRentals(txtCustId.getText().toString());
                                                                    db.changeStatus(txtUPC.getText().toString(), false);
                                                                    Toast.makeText(getContext(), "Rental Created", Toast.LENGTH_SHORT).show();
                                                                    editor.dismiss();
                                                                    refreshList();
                                                                } else {
                                                                    Toast.makeText(getContext(), "Rental Creation Failed", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        cancel.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                editor.dismiss();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });
                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                editor.dismiss();
                                            }
                                        });
                                    }
                                });

                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        editor.dismiss();
                                    }
                                });
                                add.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Invalid UPC Code", Toast.LENGTH_SHORT).show();
                            }

                            refreshList();
                        }
                        else {
                            Toast.makeText(getContext(), "Text Fields CANNOT be empty.", Toast.LENGTH_SHORT).show();

                        }
                        refreshList();
                    }
                });
                addCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        add.dismiss();
                    }
                });
            }
        });

        rentals = getRentals();
        ArrayList<String> rentalIds = getRentalIds(rentals);
        rentalInformation = getRentalInformation(rentals);
        adapter = new MovieListAdapter(getContext(),rentalIds,rentalInformation);
        rentalList.setAdapter(adapter);

        rentalList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    rentalList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                currentSelectedGroup = groupPosition;
            }
        });

        return view;
    }

    public HashMap<String, ArrayList<String>> getRentalInformation(ArrayList<RentalItem> a)
    {
        HashMap<String, ArrayList<String>> rentalInfo = new HashMap<>();

        for(int i=0;i<a.size();i++)
        {
            ArrayList<String> tempList = new ArrayList<>();
            RentalItem temp = a.get(i);
            tempList.add("DVD UPC Code: "+temp.getUpc());
            tempList.add("Check In Date: "+temp.getCheckInDate());
            tempList.add("Customer ID: "+temp.getCust());
            tempList.add("Price: $"+temp.getCost());

            rentalInfo.put(a.get(i).getId()+"", tempList);
        }
        return rentalInfo;
    }

    public ArrayList<String> getRentalIds(ArrayList<RentalItem> a)
    {
        ArrayList<String> names = new ArrayList<>();
        for(int i = 0;i<a.size();i++)
        {
            names.add(a.get(i).getId() + "");
        }
        return names;
    }


    public ArrayList<RentalItem> getRentals()
    {
        ArrayList<RentalItem> rentalTempList = new ArrayList<>();

        int tempID;
        String tempCustId = "";
        String tempUPC = "";
        String tempReturnDate = "";
        double tempPrice;
        int tempIsLate;


        Cursor res = db.getAllRentals();
        if(res.getCount() != 0)
        {
            String[] columns = db.getRentalAttributes();
            while(res.moveToNext())
            {

                tempID = Integer.parseInt(res.getString(res.getColumnIndex(columns[0])));
                tempCustId = res.getString(res.getColumnIndex(columns[1]));
                tempUPC = res.getString(res.getColumnIndex(columns[2]));
                tempReturnDate = res.getString(res.getColumnIndex(columns[3]));
                tempPrice = Double.parseDouble(res.getString(res.getColumnIndex(columns[4])));
                tempIsLate = res.getInt(res.getColumnIndex(columns[5]));

                RentalItem item = new RentalItem(tempReturnDate, tempIsLate == 1, tempPrice, tempID, tempUPC, tempCustId);
                rentalTempList.add(item);
            }
        }
        return rentalTempList;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String userInput = newText.toLowerCase();
        ArrayList<RentalItem> rentalListFiltered = new ArrayList<>();

        //Customer ID
        if(spinnerSelection == 1)
        {
            for(RentalItem dvd : rentals) {
                if (dvd.getCheckInDate().toLowerCase().contains(userInput)) {
                    rentalListFiltered.add(dvd);
                }
            }

        }
        //Cell Phone
        else if(spinnerSelection == 2)
        {
            for(RentalItem dvd : rentals) {
                if (dvd.getUpc().toLowerCase().contains(userInput)) {
                    rentalListFiltered.add(dvd);
                }
            }
        }
        // email
        else if(spinnerSelection == 3)
        {
            for(RentalItem dvd : rentals) {
                if (dvd.getCust().toLowerCase().contains(userInput)) {
                    rentalListFiltered.add(dvd);
                }
            }
        }
        // card
        else if(spinnerSelection == 4)
        {
            for(RentalItem dvd : rentals) {
                if ((dvd.getCost() + "").toLowerCase().contains(userInput)) {
                    rentalListFiltered.add(dvd);
                }
            }
        }
        else
        {
            for(RentalItem dvd : rentals) {
                if ((dvd.getId() + "").toLowerCase().contains(userInput)) {
                    rentalListFiltered.add(dvd);
                }
            }
        }
        rentalList = (ExpandableListView) view.findViewById(R.id.lstRentals);
        ArrayList<String> rentalIds = getRentalIds(rentalListFiltered);
        rentalInformation = getRentalInformation(rentalListFiltered);
        adapter = new MovieListAdapter(getContext(),rentalIds,rentalInformation);
        rentalList.setAdapter(adapter);

        return false;
    }

    public void refreshList()
    {
        rentalList = (ExpandableListView) view.findViewById(R.id.lstRentals);
        rentals = getRentals();
        ArrayList<String> customerNames = getRentalIds(rentals);
        rentalInformation = getRentalInformation(rentals);
        adapter = new MovieListAdapter(getContext(),customerNames,rentalInformation);
        rentalList.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerSelection = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spinnerSelection = 0;
    }
}
