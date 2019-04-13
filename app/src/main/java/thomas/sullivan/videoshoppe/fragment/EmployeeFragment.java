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
import thomas.sullivan.videoshoppe.resources.EmployeeItem;
import thomas.sullivan.videoshoppe.resources.UserDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeFragment extends Fragment implements android.widget.SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    public class EmployeeListAdapter extends BaseExpandableListAdapter {

        ArrayList<String> parents;
        HashMap<String, ArrayList<String>> children;
        Context context;

        public EmployeeListAdapter(Context context, ArrayList<String> aparents,
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
            final String usernameText = (String) getChild(groupPosition, 1);
            final String cellText = (String) getChild(groupPosition, 2);
            final String emailText = (String) getChild(groupPosition, 3);
            final String adminText = (String) getChild(groupPosition, 4);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.row_employee_layout_expandable, null);
            }

            TextView id = (TextView) convertView.findViewById(R.id.employeeListID);
            TextView username = (TextView) convertView.findViewById(R.id.employeeListUsername);
            TextView cell = (TextView) convertView.findViewById(R.id.employeeListCellphone);
            TextView email = (TextView) convertView.findViewById(R.id.employeeListEmail);
            TextView admin = (TextView) convertView.findViewById(R.id.employeeListAdmin);

            id.setText(idText);
            username.setText(usernameText);
            cell.setText(cellText);
            email.setText(emailText);
            admin.setText(adminText);

            final String tempEmployeeID = employees.get(currentSelectedGroup).getiD();
            final String tempName = employees.get(currentSelectedGroup).getFirstName()+" "+employees.get(currentSelectedGroup).getLastName();

            ImageButton deleteMain = (ImageButton) convertView.findViewById(R.id.delete_employe_Button);
            ImageButton editMain = (ImageButton) convertView.findViewById(R.id.edit_employee_Button);

            editMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (db.isAdmin(db.getLoggedInUserID())) {
                        final AlertDialog.Builder editorEmployeeDialog = new AlertDialog.Builder(getContext());
                        final View view3 = getLayoutInflater().inflate(R.layout.edit_employee_editor, null);
                        editorEmployeeDialog.setView(view3);
                        final AlertDialog editor = editorEmployeeDialog.create();
                        editor.show();

                        final EditText tempFirstName = (EditText) view3.findViewById(R.id.edit_employee_firstName);
                        final EditText tempLastName = (EditText) view3.findViewById(R.id.edit_employee_lastName);
                        final EditText tempEmail = (EditText) view3.findViewById(R.id.edit_employee_email);
                        final EditText tempPhoneNumber = (EditText) view3.findViewById(R.id.edit_employee_phoneNumber);
                        final EditText tempUsername = (EditText) view3.findViewById(R.id.edit_employee_username);
                        final EditText tempPassword = (EditText) view3.findViewById(R.id.edit_employee_password);
                        final CheckBox tempAdmin = (CheckBox) view3.findViewById(R.id.edit_employee_admin);

                        Button editorConfirm = (Button) view3.findViewById(R.id.edit_employee_confirm);
                        Button editorCancel = (Button) view3.findViewById(R.id.edit_employee_cancel);

                        String[] employeInformation = db.employeeRowReturn(tempEmployeeID);
                        if (employeInformation[0] != null) {
                            tempLastName.setHint(employeInformation[0]);
                            tempFirstName.setHint(employeInformation[1]);
                            tempUsername.setHint(employeInformation[2]);
                            tempPassword.setHint(employeInformation[3]);
                            if (employeInformation[4].equalsIgnoreCase("1")) {
                                tempAdmin.setChecked(true);
                            }
                            tempEmail.setHint(employeInformation[5]);
                            tempPhoneNumber.setHint(employeInformation[6]);
                        }

                        editorConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!tempFirstName.getText().toString().isEmpty() && !tempLastName.getText().toString().isEmpty() &&
                                        !tempEmail.getText().toString().isEmpty() && !tempPhoneNumber.getText().toString().isEmpty() && !tempUsername.getText().toString().isEmpty() &&
                                        !tempPassword.getText().toString().isEmpty()) {
                                    if (db.removeEmployee(tempEmployeeID)) {
                                        if (tempAdmin.isChecked()) {
                                            if (db.createEmployee(tempEmployeeID, tempLastName.getText().toString(), tempFirstName.getText().toString(), tempUsername.getText().toString(),
                                                    tempPassword.getText().toString(), 1, tempPhoneNumber.getText().toString(), tempEmail.getText().toString())) {
                                                Toast.makeText(getContext(), "Employee Updated.", Toast.LENGTH_SHORT).show();
                                                editor.dismiss();
                                            } else {
                                                Toast.makeText(getContext(), "Employee Update Failed.", Toast.LENGTH_SHORT).show();
                                                editor.dismiss();
                                            }
                                        } else {
                                            if (db.createEmployee(tempEmployeeID, tempLastName.getText().toString(), tempFirstName.getText().toString(), tempUsername.getText().toString(),
                                                    tempPassword.getText().toString(), 0, tempPhoneNumber.getText().toString(), tempEmail.getText().toString())) {
                                                Toast.makeText(getContext(), "Employee Updated.", Toast.LENGTH_SHORT).show();
                                                editor.dismiss();

                                            } else {
                                                Toast.makeText(getContext(), "Employee Update Failed.", Toast.LENGTH_SHORT).show();
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
                    } else {
                        Toast.makeText(getContext(), "Insufficient Permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            deleteMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (db.isAdmin(db.getLoggedInUserID())) {
                        final AlertDialog.Builder editorEmployeeDialog = new AlertDialog.Builder(getContext());
                        final View view3 = getLayoutInflater().inflate(R.layout.delete_employee_confirm, null);
                        editorEmployeeDialog.setView(view3);
                        final AlertDialog editor = editorEmployeeDialog.create();
                        editor.show();

                        TextView deleteConfirmation = (TextView) view3.findViewById(R.id.delete_employee_title);
                        deleteConfirmation.setText("Delete "+tempName+"?");
                        Button deleteConfirm = (Button) view3.findViewById(R.id.delete_employee_confirm);
                        Button deleteCancel = (Button) view3.findViewById(R.id.delete_employee_cancel);

                        deleteConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.removeEmployee(tempEmployeeID);
                                editor.dismiss();
                                Toast.makeText(getContext(), "Employee Removed.", Toast.LENGTH_SHORT).show();
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
                convertView = infalInflater.inflate(R.layout.row_employee_layout, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.employeeListHeader);
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

    // name = 0, id = 1, cellphone = 2, email = 3. Default = name
    int spinnerSelection;

    private int lastExpandedPosition = -1;
    private int currentSelectedGroup;
    UserDatabase db;
    ExpandableListView employeeList;
    View view;
    ArrayList<EmployeeItem> employees;
    EmployeeListAdapter adapter;
    HashMap<String, ArrayList<String>> employeeInformation;

    private OnFragmentInteractionListener mListener;

    public EmployeeFragment() {
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
        view = inflater.inflate(R.layout.fragment_employee, null);
        employeeList = (ExpandableListView) view.findViewById(R.id.employees_listed);

        Spinner spinner = (Spinner) view.findViewById(R.id.employeeFilter);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.employee_filters, R.layout.employee_filter);
        arrayAdapter.setDropDownViewResource(R.layout.employee_filter);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        android.widget.SearchView searchView = (android.widget.SearchView) view.findViewById(R.id.action_search);
        searchView.setOnQueryTextListener(this);

        FloatingActionButton fab = view.findViewById(R.id.employeefab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.isAdmin(db.getLoggedInUserID()))
                {
                    final AlertDialog.Builder addEmployeeDialog = new AlertDialog.Builder(getContext());
                    final View view2 = getLayoutInflater().inflate(R.layout.add_employee_popup, null);
                    addEmployeeDialog.setView(view2);
                    final AlertDialog dialog = addEmployeeDialog.create();
                    dialog.show();

                    final EditText tempID = (EditText) view2.findViewById(R.id.add_employee_id);
                    final EditText tempFirstName = (EditText) view2.findViewById(R.id.add_employee_firstName);
                    final EditText tempLastName = (EditText) view2.findViewById(R.id.add_employee_lastName);
                    final EditText tempEmail = (EditText) view2.findViewById(R.id.add_employee_email);
                    final EditText tempPhoneNumber = (EditText) view2.findViewById(R.id.add_employee_phoneNumber);
                    final EditText tempUsername = (EditText) view2.findViewById(R.id.add_employee_username);
                    final EditText tempPassword = (EditText) view2.findViewById(R.id.add_employee_password);
                    final CheckBox tempAdmin = (CheckBox) view2.findViewById(R.id.add_employee_admin);
                    Button confirm = (Button) view2.findViewById(R.id.add_employee_confirm);
                    Button cancel = (Button) view2.findViewById(R.id.add_employee_cancel);

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!tempID.getText().toString().isEmpty() && !tempFirstName.getText().toString().isEmpty() &&  !tempLastName.getText().toString().isEmpty() &&
                                    !tempEmail.getText().toString().isEmpty() && !tempPhoneNumber.getText().toString().isEmpty() && !tempUsername.getText().toString().isEmpty() &&
                                    !tempPassword.getText().toString().isEmpty())
                            {
                                if(tempAdmin.isChecked())
                                {
                                    if(db.createEmployee(tempID.getText().toString(),tempLastName.getText().toString(),tempFirstName.getText().toString(),tempUsername.getText().toString(),tempPassword.getText().toString(),1,tempPhoneNumber.getText().toString(),tempEmail.getText().toString()))
                                    {
                                        Toast.makeText(getContext(),"Employee had been added.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(),"ERROR: Employee has not been added.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if(db.createEmployee(tempID.getText().toString(),tempLastName.getText().toString(),tempFirstName.getText().toString(),tempUsername.getText().toString(),tempPassword.getText().toString(),0,tempPhoneNumber.getText().toString(),tempEmail.getText().toString()))
                                    {
                                        Toast.makeText(getContext(),"Employee had been added.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(),"ERROR: Employee has not been added.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                addEmployeeDialog.setView(view);
                                dialog.dismiss();
                                refreshList();
                            } else {
                                Toast.makeText(getContext(),"ERROR: Text Entries CANNOT be empty.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                            Toast.makeText(getContext(),"Canceled.", Toast.LENGTH_SHORT).show();
                            refreshList();
                        }
                    });

                } else {
                    Toast.makeText(getContext(),"Insufficient Permissions", Toast.LENGTH_SHORT).show();
                }
            }
        });

        employees = getEmployees();
        ArrayList<String> employeeNames = getEmployeeNames(employees);
        employeeInformation = getEmployeeInformation(employees);
        adapter = new EmployeeListAdapter(getContext(),employeeNames,employeeInformation);
        employeeList.setAdapter(adapter);

        employeeList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    employeeList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                currentSelectedGroup = groupPosition;
            }
        });

        return view;
    }

    public HashMap<String, ArrayList<String>> getEmployeeInformation(ArrayList<EmployeeItem> a)
    {
        HashMap<String, ArrayList<String>> employeeInfo = new HashMap<>();
        for(int i=0;i<a.size();i++)
        {
            ArrayList<String> tempList = new ArrayList<String>();
            EmployeeItem temp = a.get(i);
            tempList.add("Employee ID: "+temp.getiD());
            tempList.add("Username: "+temp.getUsername());
            tempList.add("Cell: "+temp.getPhoneNumber());
            tempList.add("Email: "+temp.getEmail());
            if(temp.getAdmin()==1)
            {
                tempList.add("Admin: true");
            } else {
                tempList.add("Admin: false");
            }

            employeeInfo.put(a.get(i).getName(), tempList);
        }
        return employeeInfo;
    }

    public ArrayList<String> getEmployeeNames(ArrayList<EmployeeItem> a)
    {
        ArrayList<String> names = new ArrayList<>();
        for(int i = 0;i<a.size();i++)
        {
            names.add(a.get(i).getName());
        }
        return names;
    }


    public ArrayList<EmployeeItem> getEmployees()
    {
        ArrayList<EmployeeItem> employeesList = new ArrayList<>();
        Cursor res = db.getAllEmployeeData();
        if(res.getCount() != 0)
        {
            while(res.moveToNext())
            {
                String tempID = res.getString(res.getColumnIndex("EmployeeID"));
                String tempFirstName = res.getString(res.getColumnIndex("firstname"));
                String tempLastName = res.getString(res.getColumnIndex("lastname"));
                String tempCellPhone = res.getString(res.getColumnIndex("phonenumber"));
                String tempEmail = res.getString(res.getColumnIndex("email"));
                String tempUsername = res.getString(res.getColumnIndex("username"));
                int tempAdmin = res.getInt(res.getColumnIndex("admin"));

                EmployeeItem tempEmployee = new EmployeeItem(tempFirstName,tempLastName,tempID,tempCellPhone,tempEmail,tempUsername,tempAdmin);
                employeesList.add(tempEmployee);
            }
        }
        return employeesList;
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
        ArrayList<EmployeeItem> employeeListFiltered = new ArrayList<>();

        //Employee ID
        if(spinnerSelection == 1)
        {
            for(EmployeeItem employee : employees) {
                if (employee.getiD().toLowerCase().contains(userInput)) {
                    employeeListFiltered.add(employee);
                }
            }
            employeeList = (ExpandableListView) view.findViewById(R.id.employees_listed);
            ArrayList<String> employeeNames = getEmployeeNames(employeeListFiltered);
            employeeInformation = getEmployeeInformation(employeeListFiltered);
            adapter = new EmployeeListAdapter(getContext(),employeeNames,employeeInformation);
            employeeList.setAdapter(adapter);
        }
        //Cell Phone
        else if(spinnerSelection == 2)
        {
            for(EmployeeItem employee : employees) {
                if (employee.getPhoneNumber().toLowerCase().contains(userInput)) {
                    employeeListFiltered.add(employee);
                }
            }
            employeeList = (ExpandableListView) view.findViewById(R.id.employees_listed);
            ArrayList<String> employeeNames = getEmployeeNames(employeeListFiltered);
            employeeInformation = getEmployeeInformation(employeeListFiltered);
            adapter = new EmployeeListAdapter(getContext(),employeeNames,employeeInformation);
            employeeList.setAdapter(adapter);
        }
        // email
        else if(spinnerSelection == 3)
        {
            for(EmployeeItem employee : employees) {
                if (employee.getEmail().toLowerCase().contains(userInput)) {
                    employeeListFiltered.add(employee);
                }
            }
            employeeList = (ExpandableListView) view.findViewById(R.id.employees_listed);
            ArrayList<String> employeeNames = getEmployeeNames(employeeListFiltered);
            employeeInformation = getEmployeeInformation(employeeListFiltered);
            adapter = new EmployeeListAdapter(getContext(),employeeNames,employeeInformation);
            employeeList.setAdapter(adapter);
        }
        else
            {
            for(EmployeeItem employee : employees) {
                if (employee.getName().toLowerCase().contains(userInput)) {
                    employeeListFiltered.add(employee);
                }
            }
            employeeList = (ExpandableListView) view.findViewById(R.id.employees_listed);
            ArrayList<String> employeeNames = getEmployeeNames(employeeListFiltered);
            employeeInformation = getEmployeeInformation(employeeListFiltered);
            adapter = new EmployeeListAdapter(getContext(),employeeNames,employeeInformation);
            employeeList.setAdapter(adapter);
        }
        return false;
    }

    public void refreshList()
    {
        employeeList = (ExpandableListView) view.findViewById(R.id.employees_listed);
        employees = getEmployees();
        ArrayList<String> employeeNames = getEmployeeNames(employees);
        employeeInformation = getEmployeeInformation(employees);
        adapter = new EmployeeListAdapter(getContext(),employeeNames,employeeInformation);
        employeeList.setAdapter(adapter);
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
