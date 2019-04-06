package thomas.sullivan.videoshoppe.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableList;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.ArrayList;
import java.util.List;

import thomas.sullivan.videoshoppe.activity.R;
import thomas.sullivan.videoshoppe.resources.EmployeeChild;
import thomas.sullivan.videoshoppe.resources.EmployeeItem;
import thomas.sullivan.videoshoppe.resources.EmployeeListAdapter;
import thomas.sullivan.videoshoppe.resources.UserDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeFragment extends Fragment implements SearchView.OnQueryTextListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchView.OnQueryTextListener queryTextListener;

    UserDatabase db;
    RecyclerView employeeList;
    View view;
    EmployeeListAdapter adapter;
    List<EmployeeItem> employees;
    LinearLayoutManager layoutManager;

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
        // Define the view
        view = inflater.inflate(R.layout.main_employees, container, false);

        employees = getEmployees();
        employeeList = (RecyclerView) view.findViewById(R.id.employee_list);
        layoutManager = new LinearLayoutManager(getContext());

        //instantiate your adapter with the list of employees
        adapter = new EmployeeListAdapter(employees);
        employeeList.setLayoutManager(layoutManager);
        employeeList.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }

    public List<EmployeeItem> getEmployees()
    {
        List<EmployeeItem> employeesList = new ArrayList<>();
        employeesList.clear();
        Cursor res = db.getAllEmployeeData();
        if(res.getCount() != 0)
        {
            while(res.moveToNext())
            {
                List<EmployeeChild> tempChildren = new ArrayList<EmployeeChild>();

                String tempID = res.getString(res.getColumnIndex("EmployeeID"));
                String tempFirstName = res.getString(res.getColumnIndex("firstname"));
                String tempLastName = res.getString(res.getColumnIndex("lastname"));
                String tempCellPhone = res.getString(res.getColumnIndex("phonenumber"));
                String tempEmail = res.getString(res.getColumnIndex("email"));

                EmployeeChild tempChild = new EmployeeChild(tempID, tempCellPhone, tempEmail);
                String name = tempLastName+", "+tempFirstName;
                tempChildren.add(tempChild);
                EmployeeItem tempEmployee = new EmployeeItem(name,tempChildren);
                employeesList.add(tempEmployee);
            }
        }
        return employeesList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
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
        inflater.inflate(R.menu.employee, menu);

        MenuItem searchEmployees = menu.findItem(R.id.action_search);
        MenuItem addEmployee = menu.findItem(R.id.action_add_employee);
        MenuItem editEmployee = menu.findItem(R.id.action_edit_employee);

        SearchView searchView = (SearchView) searchEmployees.getActionView();
        searchView.setOnQueryTextListener(this);

        addEmployee.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
                    Toast.makeText(getContext(),"This Action Requires Administraive Rights", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        editEmployee.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(db.isAdmin(db.getLoggedInUserID()))
                {
                    final AlertDialog.Builder editEmployeeDialog = new AlertDialog.Builder(getContext());
                    final View view3 = getLayoutInflater().inflate(R.layout.edit_employee_menu, null);
                    editEmployeeDialog.setView(view3);
                    final AlertDialog dialog = editEmployeeDialog.create();
                    dialog.show();

                    final EditText tempID2 = (EditText) view3.findViewById(R.id.edit_employee_id);

                    Button edit = (Button) view3.findViewById(R.id.edit_employee_edit);
                    Button delete = (Button) view3.findViewById(R.id.edit_employee_delete);
                    Button cancel = (Button) view3.findViewById(R.id.edit_employee_menu_cancel);

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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

                            String[] employeInformation = db.employeeRowReturn(tempID2.getText().toString());
                            tempLastName.setHint(employeInformation[0]);
                            tempFirstName.setHint(employeInformation[1]);
                            tempUsername.setHint(employeInformation[2]);
                            tempPassword.setHint(employeInformation[3]);
                            if(employeInformation[4].equalsIgnoreCase("1"))
                            {
                                tempAdmin.setChecked(true);
                            }
                            tempEmail.setHint(employeInformation[5]);
                            tempPhoneNumber.setHint(employeInformation[6]);

                            Button editorConfirm = (Button) view3.findViewById(R.id.edit_employee_confirm);
                            Button editorCancel = (Button) view3.findViewById(R.id.edit_employee_cancel);

                            editorConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!tempFirstName.getText().toString().isEmpty() &&  !tempLastName.getText().toString().isEmpty() &&
                                            !tempEmail.getText().toString().isEmpty() && !tempPhoneNumber.getText().toString().isEmpty() && !tempUsername.getText().toString().isEmpty() &&
                                            !tempPassword.getText().toString().isEmpty())
                                    {
                                        if(db.removeEmployee(tempID2.getText().toString()))
                                        {
                                            if(tempAdmin.isChecked())
                                            {
                                                if(db.createEmployee(tempID2.getText().toString(),tempLastName.getText().toString(),tempFirstName.getText().toString(),tempUsername.getText().toString(),
                                                        tempPassword.getText().toString(),1,tempPhoneNumber.getText().toString(),tempEmail.getText().toString()))
                                                {
                                                    Toast.makeText(getContext(),"Employee Updated.",Toast.LENGTH_SHORT).show();
                                                    editor.dismiss();
                                                    dialog.dismiss();
                                                    refreshList();
                                                } else {
                                                    Toast.makeText(getContext(),"Employee Update Failed.",Toast.LENGTH_SHORT).show();
                                                    editor.dismiss();
                                                    dialog.dismiss();
                                                    refreshList();
                                                }
                                            } else {
                                                if(db.createEmployee(tempID2.getText().toString(),tempLastName.getText().toString(),tempFirstName.getText().toString(),tempUsername.getText().toString(),
                                                        tempPassword.getText().toString(),0,tempPhoneNumber.getText().toString(),tempEmail.getText().toString()))
                                                {
                                                    Toast.makeText(getContext(),"Employee Updated.",Toast.LENGTH_SHORT).show();
                                                    editor.dismiss();
                                                    dialog.dismiss();
                                                    refreshList();
                                                } else {
                                                    Toast.makeText(getContext(),"Employee Update Failed.",Toast.LENGTH_SHORT).show();
                                                    editor.dismiss();
                                                    dialog.dismiss();
                                                    refreshList();
                                                }
                                            }
                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(getContext(),"ERROR: Invalid Employee ID", Toast.LENGTH_SHORT).show();
                                        }
                                     }else {
                                        Toast.makeText(getContext(),"Text Fields CANNOT be empty.",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                            editorCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    editor.dismiss();
                                    dialog.dismiss();
                                    Toast.makeText(getContext(),"Canceled.", Toast.LENGTH_SHORT).show();
                                    refreshList();
                                }
                            });
                        }
                    });


                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(db.removeEmployee(tempID2.getText().toString()))
                            {
                                editEmployeeDialog.setView(view);
                                dialog.dismiss();
                                refreshList();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getContext(),"ERROR: Invalid Employee ID", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(),"This Action Requires Administraive Rights", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String userInput = newText.toLowerCase();
        List<EmployeeItem> employeeListFiltered = new ArrayList<>();

        for(EmployeeItem employee : employees) {
            if (employee.getTitle().toLowerCase().contains(userInput)) {
                employeeListFiltered.add(employee);
            }
        }
        adapter = new EmployeeListAdapter(employeeListFiltered);
        employeeList.setLayoutManager(layoutManager);
        employeeList.setAdapter(adapter);
        return false;
    }

    public void refreshList()
    {
        employees = getEmployees();
        employeeList = (RecyclerView) view.findViewById(R.id.employee_list);
        layoutManager = new LinearLayoutManager(getContext());

        //instantiate your adapter with the list of employees
        adapter = new EmployeeListAdapter(employees);
        employeeList.setLayoutManager(layoutManager);
        employeeList.setAdapter(adapter);
    }
}
