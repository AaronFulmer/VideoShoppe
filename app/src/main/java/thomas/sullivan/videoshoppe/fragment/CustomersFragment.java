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
import thomas.sullivan.videoshoppe.resources.UserDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomersFragment extends Fragment implements android.widget.SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    public class CustomerListAdapter extends BaseExpandableListAdapter {

        ArrayList<String> parents;
        HashMap<String, ArrayList<String>> children;
        Context context;

        public CustomerListAdapter(Context context, ArrayList<String> aparents,
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

            final String idText = (String) getChild(groupPosition, 0);
            final String cellText = (String) getChild(groupPosition, 1);
            final String emailText = (String) getChild(groupPosition, 2);
            final String cardText = (String) getChild(groupPosition, 3);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.row_customer_layout_expandable, null);
            }

            TextView id = (TextView) convertView.findViewById(R.id.customerListID);
            TextView cell = (TextView) convertView.findViewById(R.id.customerListCell);
            TextView email = (TextView) convertView.findViewById(R.id.customerListEmail);
            TextView card = (TextView) convertView.findViewById(R.id.customerListCard);

            id.setText(idText);
            cell.setText(cellText);
            email.setText(emailText);
            card.setText(cardText);

            final String tempCustomerID = customers.get(currentSelectedGroup).getiD();
            final String tempName = customers.get(currentSelectedGroup).getFirstName()+" "+customers.get(currentSelectedGroup).getLastName();

            ImageButton deleteMain = (ImageButton) convertView.findViewById(R.id.delete_customer_button);
            ImageButton editMain = (ImageButton) convertView.findViewById(R.id.edit_customer_button);

            editMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder editorCustomerDialog = new AlertDialog.Builder(getContext());
                    final View view3 = getLayoutInflater().inflate(R.layout.edit_customer_editor, null);
                    editorCustomerDialog.setView(view3);
                    final AlertDialog editor = editorCustomerDialog.create();
                    editor.show();

                    final EditText tempLastName = (EditText) view3.findViewById(R.id.edit_customer_lastName);
                    final EditText tempFirstName = (EditText) view3.findViewById(R.id.edit_customer_firstName);
                    final EditText tempEmail = (EditText) view3.findViewById(R.id.edit_customer_email);
                    final EditText tempPhoneNumber = (EditText) view3.findViewById(R.id.edit_customer_phoneNumber);
                    final EditText tempCard = (EditText) view3.findViewById(R.id.edit_customer_card);
                    final EditText tempSecurity = (EditText) view3.findViewById(R.id.edit_customer_code);
                    final EditText tempExpiration = (EditText) view3.findViewById(R.id.edit_customer_ExpirationDate);
                    final EditText tempType = (EditText) view3.findViewById(R.id.edit_customer_cardType);


                    Button editorConfirm = (Button) view3.findViewById(R.id.edit_customer_confirm);
                    Button editorCancel = (Button) view3.findViewById(R.id.edit_customer_cancel);

                    String[] customerTempInfo = db.customerRowReturn(tempCustomerID);
                    if (customerTempInfo[0] != null) {
                        tempLastName.setHint(customerTempInfo[0]);
                        tempFirstName.setHint(customerTempInfo[1]);
                        tempEmail.setHint(customerTempInfo[2]);
                        tempPhoneNumber.setHint(customerTempInfo[3]);
                        tempCard.setHint("************"+customerTempInfo[4].substring(customerTempInfo[4].length()-4));
                        tempSecurity.setHint("****");
                        tempExpiration.setHint("MMYY");
                        tempType.setHint(customerTempInfo[7]);
                    }

                    editorConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!tempFirstName.getText().toString().isEmpty() && !tempLastName.getText().toString().isEmpty() &&
                                    !tempEmail.getText().toString().isEmpty() && !tempPhoneNumber.getText().toString().isEmpty() && !tempCard.getText().toString().isEmpty() &&
                                    !tempSecurity.getText().toString().isEmpty()  && !tempExpiration.getText().toString().isEmpty()  && !tempType.getText().toString().isEmpty()) {
                                if (db.removeCustomer(tempCustomerID)) {
                                    if(tempPhoneNumber.getText().toString().length() != 10 || tempPhoneNumber.getText().toString().contains("[a-zA-Z]+"))
                                    {
                                        Toast.makeText(getContext(), "Invalid Phone Number.", Toast.LENGTH_SHORT).show();
                                    } else if(tempCard.getText().toString().length() != 16 || tempCard.getText().toString().contains("[a-zA-Z]+")) {
                                        Toast.makeText(getContext(), "Invalid Card Number.", Toast.LENGTH_SHORT).show();
                                    } else if(tempExpiration.getText().toString().length() != 4 || tempExpiration.getText().toString().contains("[a-zA-Z]+")) {
                                        Toast.makeText(getContext(), "Invalid Expiration Date.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        if (db.createCustomer(tempCustomerID, tempLastName.getText().toString(), tempFirstName.getText().toString(), tempEmail.getText().toString(),
                                                tempPhoneNumber.getText().toString(), tempCard.getText().toString(), tempExpiration.getText().toString(), tempSecurity.getText().toString(), tempType.getText().toString())) {
                                            Toast.makeText(getContext(), "Customer Updated.", Toast.LENGTH_SHORT).show();
                                            editor.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "Customer Update Failed.", Toast.LENGTH_SHORT).show();
                                            editor.dismiss();
                                        }
                                    }
                                    refreshList();
                                }
                            } else {
                                Toast.makeText(getContext(), "Text Fields CANNOT be empty.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    editorCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            editor.dismiss();
                        }
                    });
                }
            });

            deleteMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (db.isAdmin(db.getLoggedInUserID())) {
                        final AlertDialog.Builder editorEmployeeDialog = new AlertDialog.Builder(getContext());
                        final View view3 = getLayoutInflater().inflate(R.layout.delete_customer_confirm, null);
                        editorEmployeeDialog.setView(view3);
                        final AlertDialog editor = editorEmployeeDialog.create();
                        editor.show();

                        TextView deleteConfirmation = (TextView) view3.findViewById(R.id.delete_customer_title);
                        deleteConfirmation.setText("Delete "+tempName+"?");
                        Button deleteConfirm = (Button) view3.findViewById(R.id.delete_customer_confirm);
                        Button deleteCancel = (Button) view3.findViewById(R.id.delete_customer_cancel);

                        deleteConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.removeCustomer(tempCustomerID);
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
                convertView = infalInflater.inflate(R.layout.row_customer_layout, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.customerListHeader);
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

    // name = default/0, id = 1, cellphone = 2, email = 3. card = 4
    int spinnerSelection;

    private int lastExpandedPosition = -1;
    private int currentSelectedGroup;
    UserDatabase db;
    ExpandableListView customerList;
    View view;
    ArrayList<CustomerItem> customers;
    CustomerListAdapter adapter;
    HashMap<String, ArrayList<String>> customerInformation;

    private OnFragmentInteractionListener mListener;

    public CustomersFragment() {
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
    public static EmployeeFragment newInstance(String param1, String param2) {
        EmployeeFragment fragment = new EmployeeFragment();
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
        view = inflater.inflate(R.layout.fragment_customers, null);
        customerList = (ExpandableListView) view.findViewById(R.id.customers_listed);

        Spinner spinner = (Spinner) view.findViewById(R.id.customerFilter);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.customer_filters, R.layout.customer_filter);
        arrayAdapter.setDropDownViewResource(R.layout.customer_filter);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        android.widget.SearchView searchView = (android.widget.SearchView) view.findViewById(R.id.action_search_customers);
        searchView.setOnQueryTextListener(this);

        FloatingActionButton fab = view.findViewById(R.id.customerfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final AlertDialog.Builder addCustomerDialog = new AlertDialog.Builder(getContext());
                    final View view4 = getLayoutInflater().inflate(R.layout.add_customer_popup, null);
                    addCustomerDialog.setView(view4);
                    final AlertDialog add = addCustomerDialog.create();
                    add.show();

                    final EditText tempLastName = (EditText) view4.findViewById(R.id.add_customer_lastName);
                    final EditText tempFirstName = (EditText) view4.findViewById(R.id.add_customer_firstName);
                    final EditText tempEmail = (EditText) view4.findViewById(R.id.add_customer_email);
                    final EditText tempPhoneNumber = (EditText) view4.findViewById(R.id.add_customer_phoneNumber);
                    final EditText tempCard = (EditText) view4.findViewById(R.id.add_customer_card);
                    final EditText tempSecurity = (EditText) view4.findViewById(R.id.add_customer_code);
                    final EditText tempExpiration = (EditText) view4.findViewById(R.id.add_customer_ExpirationDate);
                    final EditText tempType = (EditText) view4.findViewById(R.id.add_customer_cardType);
                    final EditText tempCustomerID = (EditText) view4.findViewById(R.id.add_customer_ID);


                    Button addConfirm = (Button) view4.findViewById(R.id.add_customer_confirm);
                    Button addCancel = (Button) view4.findViewById(R.id.add_customer_cancel);

                    addConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!tempFirstName.getText().toString().isEmpty() && !tempLastName.getText().toString().isEmpty() &&
                                    !tempEmail.getText().toString().isEmpty() && !tempPhoneNumber.getText().toString().isEmpty() && !tempCard.getText().toString().isEmpty() &&
                                    !tempSecurity.getText().toString().isEmpty()  && !tempExpiration.getText().toString().isEmpty()  && !tempType.getText().toString().isEmpty()) {
                                if(tempPhoneNumber.getText().toString().length() != 10 || tempPhoneNumber.getText().toString().contains("[a-zA-Z]+"))
                                {
                                    Toast.makeText(getContext(), "Invalid Phone Number.", Toast.LENGTH_SHORT).show();
                                } else if(tempCard.getText().toString().length() != 16 || tempCard.getText().toString().contains("[a-zA-Z]+")) {
                                    Toast.makeText(getContext(), "Invalid Card Number.", Toast.LENGTH_SHORT).show();
                                } else if(tempExpiration.getText().toString().length() != 4 || tempExpiration.getText().toString().contains("[a-zA-Z]+")) {
                                    Toast.makeText(getContext(), "Invalid Expiration Date.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (db.createCustomer(tempCustomerID.getText().toString(), tempLastName.getText().toString(), tempFirstName.getText().toString(), tempEmail.getText().toString(),
                                            tempPhoneNumber.getText().toString(), tempCard.getText().toString(), tempExpiration.getText().toString(), tempSecurity.getText().toString(), tempType.getText().toString())) {
                                        Toast.makeText(getContext(), "Customer Registered.", Toast.LENGTH_SHORT).show();
                                        add.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Customer Registration Failed.", Toast.LENGTH_SHORT).show();
                                        add.dismiss();
                                    }
                                }
                                refreshList();
                            } else {
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

        customers = getCustomers();
        ArrayList<String> customerNames = getCustomerNames(customers);
        customerInformation = getCustomerInformation(customers);
        adapter = new CustomerListAdapter(getContext(),customerNames,customerInformation);
        customerList.setAdapter(adapter);

        customerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    customerList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                currentSelectedGroup = groupPosition;
            }
        });

        return view;
    }

    public HashMap<String, ArrayList<String>> getCustomerInformation(ArrayList<CustomerItem> a)
    {
        HashMap<String, ArrayList<String>> customerInfo = new HashMap<>();
        for(int i=0;i<a.size();i++)
        {
            ArrayList<String> tempList = new ArrayList<String>();
            CustomerItem temp = a.get(i);
            tempList.add("Customer ID: "+temp.getiD());
            tempList.add("Cell: "+temp.getPhoneNumber());
            tempList.add("Email: "+temp.getEmail());
            tempList.add("Card: ************"+temp.getCardNumber().substring(temp.getCardNumber().length()-4));
            tempList.add("ExpDate: "+temp.getExpirationDate());
            tempList.add("CSC Code: "+temp.getSecurityCode());
            tempList.add("Card Type: "+temp.getCardType());

            customerInfo.put(a.get(i).getName(), tempList);
        }
        return customerInfo;
    }

    public ArrayList<String> getCustomerNames(ArrayList<CustomerItem> a)
    {
        ArrayList<String> names = new ArrayList<>();
        for(int i = 0;i<a.size();i++)
        {
            names.add(a.get(i).getName());
        }
        return names;
    }


    public ArrayList<CustomerItem> getCustomers()
    {
        ArrayList<CustomerItem> customerTempList = new ArrayList<>();

        String tempID = "";
        String tempFirstName = "";
        String tempLastName = "";
        String tempCellPhone = "";
        String tempEmail = "";
        String tempCard = "";
        String tempExpiration = "";
        String tempSecurity = "";
        String tempType = "";

        Cursor res = db.getAllCustomerData();
        if(res.getCount() != 0)
        {
            while(res.moveToNext())
            {
                tempFirstName = res.getString(res.getColumnIndex("firstname"));
                tempLastName = res.getString(res.getColumnIndex("lastname"));
                tempID = res.getString(res.getColumnIndex("CustomerID"));
                tempCellPhone = res.getString(res.getColumnIndex("phoneNumber"));
                tempEmail = res.getString(res.getColumnIndex("email"));
                tempCard = res.getString(res.getColumnIndex("cardNumber"));
                tempExpiration = res.getString(res.getColumnIndex("expirationDate"));
                tempSecurity = res.getString(res.getColumnIndex("securityCode"));
                tempType = res.getString(res.getColumnIndex("cardType"));
                CustomerItem customerItem = new CustomerItem(tempFirstName,tempLastName,tempID,tempCellPhone,tempEmail,tempCard,tempExpiration,tempSecurity,tempType);
                customerTempList.add(customerItem);
            }
        }
        return customerTempList;
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
        ArrayList<CustomerItem> customersListFiltered = new ArrayList<>();

        //Customer ID
        if(spinnerSelection == 1)
        {
            for(CustomerItem customer : customers) {
                if (customer.getiD().toLowerCase().contains(userInput)) {
                    customersListFiltered.add(customer);
                }
            }
            customerList = (ExpandableListView) view.findViewById(R.id.customers_listed);
            ArrayList<String> customerNames = getCustomerNames(customersListFiltered);
            customerInformation = getCustomerInformation(customersListFiltered);
            adapter = new CustomerListAdapter(getContext(),customerNames,customerInformation);
            customerList.setAdapter(adapter);
        }
        //Cell Phone
        else if(spinnerSelection == 2)
        {
            for(CustomerItem customer : customers) {
                if (customer.getPhoneNumber().toLowerCase().contains(userInput)) {
                    customersListFiltered.add(customer);
                }
            }
            customerList = (ExpandableListView) view.findViewById(R.id.customers_listed);
            ArrayList<String> customerNames = getCustomerNames(customersListFiltered);
            customerInformation = getCustomerInformation(customersListFiltered);
            adapter = new CustomerListAdapter(getContext(),customerNames,customerInformation);
            customerList.setAdapter(adapter);
        }
        // email
        else if(spinnerSelection == 3)
        {
            for(CustomerItem customer : customers) {
                if (customer.getEmail().toLowerCase().contains(userInput)) {
                    customersListFiltered.add(customer);
                }
            }
            customerList = (ExpandableListView) view.findViewById(R.id.customers_listed);
            ArrayList<String> customerNames = getCustomerNames(customersListFiltered);
            customerInformation = getCustomerInformation(customersListFiltered);
            adapter = new CustomerListAdapter(getContext(),customerNames,customerInformation);
            customerList.setAdapter(adapter);
        }
        // card
        else if(spinnerSelection == 4)
        {
            for(CustomerItem customer : customers) {
                if (customer.getCardNumber().toLowerCase().contains(userInput)) {
                    customersListFiltered.add(customer);
                }
            }
            customerList = (ExpandableListView) view.findViewById(R.id.customers_listed);
            ArrayList<String> customerNames = getCustomerNames(customersListFiltered);
            customerInformation = getCustomerInformation(customersListFiltered);
            adapter = new CustomerListAdapter(getContext(),customerNames,customerInformation);
            customerList.setAdapter(adapter);
        }
        else
        {
            for(CustomerItem customer : customers) {
                if (customer.getName().toLowerCase().contains(userInput)) {
                    customersListFiltered.add(customer);
                }
            }
            customerList = (ExpandableListView) view.findViewById(R.id.customers_listed);
            ArrayList<String> customerNames = getCustomerNames(customersListFiltered);
            customerInformation = getCustomerInformation(customersListFiltered);
            adapter = new CustomerListAdapter(getContext(),customerNames,customerInformation);
            customerList.setAdapter(adapter);
        }
        return false;
    }

    public void refreshList()
    {
        customerList = (ExpandableListView) view.findViewById(R.id.customers_listed);
        customers = getCustomers();
        ArrayList<String> customerNames = getCustomerNames(customers);
        customerInformation = getCustomerInformation(customers);
        adapter = new CustomerListAdapter(getContext(),customerNames,customerInformation);
        customerList.setAdapter(adapter);
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
