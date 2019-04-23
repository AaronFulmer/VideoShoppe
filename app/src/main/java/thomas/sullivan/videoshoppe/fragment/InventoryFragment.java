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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import thomas.sullivan.videoshoppe.activity.R;
import thomas.sullivan.videoshoppe.resources.CustomerItem;
import thomas.sullivan.videoshoppe.resources.InventoryItem;
import thomas.sullivan.videoshoppe.resources.UserDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment implements android.widget.SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    public class InventoryListAdapter extends BaseExpandableListAdapter {

        ArrayList<String> parents;
        HashMap<String, ArrayList<String>> children;
        Context context;

        public InventoryListAdapter(Context context, ArrayList<String> aparents,
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

            final String director = (String) getChild(groupPosition, 2);
            final String releaseYear = (String) getChild(groupPosition, 3);
            final String condition = (String) getChild(groupPosition, 5);
            final String upcCode = (String) getChild(groupPosition, 0);
            final String genre = (String) getChild(groupPosition, 4);
            final String movieId = (String) getChild(groupPosition, 1);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.row_inventory_layout_expandable, null);
            }

            TextView txtDirector = (TextView) convertView.findViewById(R.id.lstDvdDirector);
            TextView txtReleaseYear = (TextView) convertView.findViewById(R.id.lstDvdReleaseYear);
            TextView txtCondition = (TextView) convertView.findViewById(R.id.lstDvdCondition);
            TextView txtGenre = (TextView) convertView.findViewById(R.id.lstDvdGenre);
            TextView txtUPCCode = (TextView) convertView.findViewById(R.id.lstDvdUPCCode);

            final String upc = inventory.get(currentSelectedGroup).getDvdUpc();
            final String movieName = inventory.get(currentSelectedGroup).getDvdName();
            txtDirector.setText(director);
            txtReleaseYear.setText(releaseYear);
            txtCondition.setText(condition);
            txtUPCCode.setText(upcCode);
            txtGenre.setText(genre);

//            final String tempCustomerID = customers.get(currentSelectedGroup).getiD();
//            final String tempName = customers.get(currentSelectedGroup).getFirstName()+" "+customers.get(currentSelectedGroup).getLastName();

            ImageButton deleteMain = (ImageButton) convertView.findViewById(R.id.btnDeleteDVD);
            ImageButton editMain = (ImageButton) convertView.findViewById(R.id.btnEditDVD);

            editMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder editorInventoryDialog = new AlertDialog.Builder(getContext());
                    final View view3 = getLayoutInflater().inflate(R.layout.edit_inventory_editor, null);
                    editorInventoryDialog.setView(view3);
                    final AlertDialog editor = editorInventoryDialog.create();
                    editor.show();

                    final TextView tempMovieName = view3.findViewById(R.id.txtMovieName);
                    final TextView tempUPCCode = view3.findViewById(R.id.txtUPCCode);
                    final TextView tempMovieID = view3.findViewById(R.id.txtMovieID);
                    final TextView tempReleaseDate =  view3.findViewById(R.id.txtReleaseDate);
                    final TextView tempGenre =  view3.findViewById(R.id.txtGenre);
                    final TextView tempDirector =  view3.findViewById(R.id.txtDirector);
//                    final EditText tempExpiration = (EditText) view3.findViewById(R.id.edit_customer_ExpirationDate);
//                    final EditText tempType = (EditText) view3.findViewById(R.id.edit_customer_cardType);
                    final CheckBox tempCondition = view3.findViewById(R.id.chkGoodCondition);


                    Button editorConfirm = (Button) view3.findViewById(R.id.btnInvEditConfirm);
                    Button editorCancel = (Button) view3.findViewById(R.id.btnInvEditCancel);

                    String[] customerTempInfo = db.inventoryRowReturn(upc);
                    if (customerTempInfo[0] != null) {
                        tempUPCCode.setText(customerTempInfo[0]);
                        tempMovieID.setText(customerTempInfo[1]);
                        tempMovieName.setText(customerTempInfo[2]);
                        tempDirector.setText(customerTempInfo[3]);
                        tempCondition.setChecked(customerTempInfo[4].equals("good"));
                        tempReleaseDate.setText(customerTempInfo[5]);
                        tempGenre.setText(customerTempInfo[6]);
//                        tempCard.setHint("************"+customerTempInfo[4].substring(customerTempInfo[4].length()-4));
//                        tempSecurity.setHint("****");
//                        tempExpiration.setHint("MMYY");
//                        tempType.setHint(customerTempInfo[7]);
                    }

                    editorConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!tempMovieID.getText().toString().isEmpty() && !tempMovieName.getText().toString().isEmpty() &&
                                    !tempDirector.getText().toString().isEmpty() && !tempReleaseDate.getText().toString().isEmpty() &&
                                    !tempGenre.getText().toString().isEmpty()) {
                                if (db.removeDvd(tempUPCCode.getText().toString())) {
                                    if (db.createDvd(tempUPCCode.getText().toString(), tempMovieID.getText().toString(), tempMovieName.getText().toString(), tempDirector.getText().toString(),
                                            (tempCondition.isChecked())? true : false, tempReleaseDate.getText().toString(), tempGenre.getText().toString())) {
                                        Toast.makeText(getContext(), "Customer Updated.", Toast.LENGTH_SHORT).show();
                                        editor.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Customer Update Failed.", Toast.LENGTH_SHORT).show();
                                        editor.dismiss();
                                        }
                                    }
                                    refreshList();
                            }
                            else {
                                Toast.makeText(getContext(), "Text Fields CANNOT be empty.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    editorCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            editor.dismiss();
                            refreshList();
                        }
                    });
                }
            });

            deleteMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (db.isAdmin(db.getLoggedInUserID())) {
                        final AlertDialog.Builder editorEmployeeDialog = new AlertDialog.Builder(getContext());
                        final View view3 = getLayoutInflater().inflate(R.layout.delete_dvd_confirm, null);
                        editorEmployeeDialog.setView(view3);
                        final AlertDialog editor = editorEmployeeDialog.create();
                        editor.show();
                        EditText tempMovieName = (EditText) view3.findViewById(R.id.txtMovieName);

                        TextView deleteConfirmation = (TextView) view3.findViewById(R.id.delete_dvd_title);
                        deleteConfirmation.setText("Delete "+movieName+"?");
                        Button deleteConfirm = (Button) view3.findViewById(R.id.btnDeleteDvdConfirm);
                        Button deleteCancel = (Button) view3.findViewById(R.id.btnDeleteDvdCancel);

                        deleteConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.removeDvd(upc);
                                editor.dismiss();
                                Toast.makeText(getContext(), "Customer Removed.", Toast.LENGTH_SHORT).show();
                                refreshList();
                            }
                        });

                        deleteCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                editor.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Insufficient Permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            });

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

    // name = default/0, id = 1, director = 2, genre = 3 release year = 4
    int spinnerSelection;

    private int lastExpandedPosition = -1;
    private int currentSelectedGroup;
    UserDatabase db;
    ExpandableListView inventoryList;
    View view;
    ArrayList<InventoryItem> inventory;
    InventoryListAdapter adapter;
    HashMap<String, ArrayList<String>> dvdInformation;

    private OnFragmentInteractionListener mListener;

    public InventoryFragment() {
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
    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
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
        view = inflater.inflate(R.layout.fragment_inventory, null);
        inventoryList = (ExpandableListView) view.findViewById(R.id.lstInventory);

        Spinner spinner = (Spinner) view.findViewById(R.id.inventoryFilter);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.inventory_filters, R.layout.inventory_filter);
        arrayAdapter.setDropDownViewResource(R.layout.inventory_filter);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        android.widget.SearchView searchView = (android.widget.SearchView) view.findViewById(R.id.action_search_inventory);
        searchView.setOnQueryTextListener(this);

        FloatingActionButton fab = view.findViewById(R.id.dvdFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder addDvdDialog = new AlertDialog.Builder(getContext());
                final View view4 = getLayoutInflater().inflate(R.layout.add_dvd_popup, null);
                addDvdDialog.setView(view4);
                final AlertDialog add = addDvdDialog.create();
                add.show();

                final EditText tempMovieName = (EditText) view4.findViewById(R.id.txtMovieName);
                final EditText tempUPCCode = (EditText) view4.findViewById(R.id.txtUPCCode);
                final EditText tempMovieID = (EditText) view4.findViewById(R.id.txtMovieID);
                final EditText tempReleaseDate = (EditText) view4.findViewById(R.id.txtReleaseDate);
                final EditText tempGenre = (EditText) view4.findViewById(R.id.txtGenre);
                final EditText tempDirector = (EditText) view4.findViewById(R.id.txtDirector);
                final CheckBox tempCondition = view4.findViewById(R.id.chkGoodCondition);
//                final EditText tempExpiration = (EditText) view4.findViewById(R.id.add_customer_ExpirationDate);
//                final EditText tempType = (EditText) view4.findViewById(R.id.add_customer_cardType);
//                final EditText tempCustomerID = (EditText) view4.findViewById(R.id.add_customer_ID);


                Button addConfirm = (Button) view4.findViewById(R.id.btnInvAddConfirm);
                Button addCancel = (Button) view4.findViewById(R.id.btnInvAddCancel);

                addConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!tempMovieID.getText().toString().isEmpty() && !tempMovieName.getText().toString().isEmpty() &&
                                !tempDirector.getText().toString().isEmpty() && !tempReleaseDate.getText().toString().isEmpty() &&
                                !tempGenre.getText().toString().isEmpty()) {

                                if (db.createDvd(tempUPCCode.getText().toString(), tempMovieID.getText().toString(), tempMovieName.getText().toString(), tempDirector.getText().toString(),
                                        (tempCondition.isChecked())? true : false, tempReleaseDate.getText().toString(), tempGenre.getText().toString())) {
                                    Toast.makeText(getContext(), "DVD Created.", Toast.LENGTH_SHORT).show();
                                    add.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "DVD Creation Failed.", Toast.LENGTH_SHORT).show();
                                    add.dismiss();
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

        inventory = getInventory();
        ArrayList<String> movieNames = getDVDNames(inventory);
        dvdInformation = getDvdInformation(inventory);
        adapter = new InventoryListAdapter(getContext(),movieNames,dvdInformation);
        inventoryList.setAdapter(adapter);

        inventoryList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    inventoryList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                currentSelectedGroup = groupPosition;
            }
        });

        return view;
    }

    public HashMap<String, ArrayList<String>> getDvdInformation(ArrayList<InventoryItem> a)
    {
        HashMap<String, ArrayList<String>> dvdInfo = new HashMap<>();

        for(int i=0;i<a.size();i++)
        {
            ArrayList<String> tempList = new ArrayList<>();
            InventoryItem temp = a.get(i);
            tempList.add("DVD UPC Code: "+temp.getDvdUpc());
            tempList.add("Movie ID: "+temp.getDvdId());
            tempList.add("Director: "+temp.getDvdDirector());
            tempList.add("Release Date: "+temp.getDvdReleaseDate());
            tempList.add("Genre: "+temp.getDvdGenre());
            tempList.add("Condition: " + temp.getDvdCondition());

            dvdInfo.put(a.get(i).getDvdName(), tempList);
        }
        return dvdInfo;
    }

    public ArrayList<String> getDVDNames(ArrayList<InventoryItem> a)
    {
        ArrayList<String> names = new ArrayList<>();
        for(int i = 0;i<a.size();i++)
        {
            names.add(a.get(i).getDvdName());
        }
        return names;
    }


    public ArrayList<InventoryItem> getInventory()
    {
        ArrayList<InventoryItem> inventoryTempList = new ArrayList<>();

        String tempID = "";
        String tempName = "";
        String tempUPC = "";
        String tempReleaseDate = "";
        String tempDirector = "";
        String tempGenre = "";
        String tempCondition = "";

        Cursor res = db.getAllDVDs();
        if(res.getCount() != 0)
        {
            String[] columns = db.getDvdAttributes();
            while(res.moveToNext())
            {
                tempUPC = res.getString(res.getColumnIndex(columns[0]));
                tempID = res.getString(res.getColumnIndex(columns[1]));
                tempName = res.getString(res.getColumnIndex(columns[2]));
                tempDirector = res.getString(res.getColumnIndex(columns[3]));
                tempCondition = res.getString(res.getColumnIndex(columns[4]));
                tempReleaseDate = res.getString(res.getColumnIndex(columns[5]));
                tempGenre = res.getString(res.getColumnIndex(columns[6]));
                InventoryItem item = new InventoryItem(tempUPC, tempName, tempID, tempReleaseDate, tempDirector, tempGenre, tempCondition);
                inventoryTempList.add(item);
            }
        }
        return inventoryTempList;
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
        ArrayList<InventoryItem> dvdListFiltered = new ArrayList<>();

        //Customer ID
        if(spinnerSelection == 1)
        {
            for(InventoryItem dvd : inventory) {
                if (dvd.getDvdUpc().toLowerCase().contains(userInput)) {
                    dvdListFiltered.add(dvd);
                }
            }

        }
        //Cell Phone
        else if(spinnerSelection == 2)
        {
            for(InventoryItem dvd : inventory) {
                if (dvd.getDvdDirector().toLowerCase().contains(userInput)) {
                    dvdListFiltered.add(dvd);
                }
            }
        }
        // email
        else if(spinnerSelection == 3)
        {
            for(InventoryItem dvd : inventory) {
                if (dvd.getDvdGenre().toLowerCase().contains(userInput)) {
                    dvdListFiltered.add(dvd);
                }
            }
        }
        // card
        else if(spinnerSelection == 4)
        {
            for(InventoryItem dvd : inventory) {
                if (dvd.getDvdReleaseDate().toLowerCase().contains(userInput)) {
                    dvdListFiltered.add(dvd);
                }
            }
        }
        else
        {
            for(InventoryItem dvd : inventory) {
                if (dvd.getDvdName().toLowerCase().contains(userInput)) {
                    dvdListFiltered.add(dvd);
                }
            }
        }
        inventoryList = (ExpandableListView) view.findViewById(R.id.lstInventory);
        ArrayList<String> customerNames = getDVDNames(dvdListFiltered);
        dvdInformation = getDvdInformation(dvdListFiltered);
        adapter = new InventoryListAdapter(getContext(),customerNames,dvdInformation);
        inventoryList.setAdapter(adapter);

        return false;
    }

    public void refreshList()
    {
        inventoryList = (ExpandableListView) view.findViewById(R.id.lstInventory);
        inventory = getInventory();
        ArrayList<String> customerNames = getDVDNames(inventory);
        dvdInformation = getDvdInformation(inventory);
        adapter = new InventoryListAdapter(getContext(),customerNames,dvdInformation);
        inventoryList.setAdapter(adapter);
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
