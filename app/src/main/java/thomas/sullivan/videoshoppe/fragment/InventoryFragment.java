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

            final String idText = (String) getChild(groupPosition, 0);
            final String releaseDateText = (String) getChild(groupPosition, 1);
            final String genreText = (String) getChild(groupPosition, 2);
            final String conditionText = (String) getChild(groupPosition, 3);
            final String directorText = (String) getChild(groupPosition, 4);
            final String actorsText = (String) getChild(groupPosition, 5);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.row_inventory_layout_expandable, null);
            }

            TextView id = (TextView) convertView.findViewById(R.id.inventoryListID);
            TextView releaseDate = (TextView) convertView.findViewById(R.id.inventoryListReleaseDate);
            TextView genre = (TextView) convertView.findViewById(R.id.inventoryListGenre);
            TextView condition = (TextView) convertView.findViewById(R.id.inventoryListCondition);
            TextView director = (TextView) convertView.findViewById(R.id.inventoryListDirector);
            TextView actors = (TextView) convertView.findViewById(R.id.inventoryListActors);

            id.setText(idText);
            releaseDate.setText(releaseDateText);
            director.setText(directorText);
            actors.setText(actorsText);
            genre.setText(genreText);
            condition.setText(conditionText);

            final String tempMovieUPC = movies.get(currentSelectedGroup).getUPCCode();
            final String tempTitle = movies.get(currentSelectedGroup).getTitle();

            ImageButton deleteMain = (ImageButton) convertView.findViewById(R.id.delete_inventory_button);
            ImageButton editMain = (ImageButton) convertView.findViewById(R.id.edit_inventory_button);

            editMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder editorCustomerDialog = new AlertDialog.Builder(getContext());
                    final View view3 = getLayoutInflater().inflate(R.layout.edit_inventory_editor, null);
                    editorCustomerDialog.setView(view3);
                    final AlertDialog editor = editorCustomerDialog.create();
                    editor.show();

                    final EditText tempID = (EditText) view3.findViewById(R.id.edit_movie_code);
                    final EditText tempReleaseDate = (EditText) view3.findViewById(R.id.edit_movie_releasedate);
                    final EditText tempDirector = (EditText) view3.findViewById(R.id.edit_movie_director);
                    final EditText tempActors = (EditText) view3.findViewById(R.id.edit_movie_actors);
                    final EditText tempGenre = (EditText) view3.findViewById(R.id.edit_movie_genre);
                    final EditText tempCondition = (EditText) view3.findViewById(R.id.edit_movie_condition);
                    final EditText tempTitle = (EditText) view3.findViewById(R.id.edit_movie_title);

                    Button editorConfirm = (Button) view3.findViewById(R.id.edit_movie_confirm);
                    Button editorCancel = (Button) view3.findViewById(R.id.edit_movie_cancel);

                    String[] movieTempInfo = db.inventoryRowReturn(tempMovieUPC);
                    if (movieTempInfo[0] != null) {
                        tempTitle.setHint(movieTempInfo[0]);
                        tempID.setHint(movieTempInfo[1]);
                        tempReleaseDate.setHint(movieTempInfo[2]);
                        tempDirector.setHint(movieTempInfo[3]);
                        tempActors.setHint(movieTempInfo[4]);
                        tempGenre.setHint(movieTempInfo[5]);
                        tempCondition.setHint(movieTempInfo[6]);
                    }

                    editorConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!tempID.getText().toString().isEmpty() && !tempReleaseDate.getText().toString().isEmpty() &&
                                    !tempDirector.getText().toString().isEmpty() && !tempActors.getText().toString().isEmpty() && !tempGenre.getText().toString().isEmpty() &&
                                    !tempCondition.getText().toString().isEmpty()  && !tempTitle.getText().toString().isEmpty()) {
                                if (db.removeDvd(tempMovieUPC)) {
                                    if (db.createDvd(tempMovieUPC, tempID.getText().toString(), tempTitle.getText().toString(),
                                            tempDirector.getText().toString(), tempCondition.getText().toString(), tempReleaseDate.getText().toString(), tempGenre.getText().toString(), tempActors.getText().toString())) {
                                        Toast.makeText(getContext(), "Movie Updated.", Toast.LENGTH_SHORT).show();
                                        editor.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Movie Update Failed.", Toast.LENGTH_SHORT).show();
                                        editor.dismiss();
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
                        final View view3 = getLayoutInflater().inflate(R.layout.delete_dvd_confirm, null);
                        editorEmployeeDialog.setView(view3);
                        final AlertDialog editor = editorEmployeeDialog.create();
                        editor.show();

                        TextView deleteConfirmation = (TextView) view3.findViewById(R.id.delete_movie_title);
                        deleteConfirmation.setText("Delete "+tempTitle+"?");
                        Button deleteConfirm = (Button) view3.findViewById(R.id.delete_movie_confirm);
                        Button deleteCancel = (Button) view3.findViewById(R.id.delete_movie_cancel);

                        deleteConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.removeDvd(tempMovieUPC);
                                editor.dismiss();
                                Toast.makeText(getContext(), "Movie Removed.", Toast.LENGTH_SHORT).show();
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

    // name = default/0, id = 1, cellphone = 2, email = 3. card = 4
    int spinnerSelection;

    private int lastExpandedPosition = -1;
    private int currentSelectedGroup;
    UserDatabase db;
    ExpandableListView movieList;
    View view;
    ArrayList<InventoryItem> movies;
    InventoryListAdapter adapter;
    HashMap<String, ArrayList<String>> movieInformation;

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
        view = inflater.inflate(R.layout.fragment_inventory, null);
        movieList = (ExpandableListView) view.findViewById(R.id.inventory_listed);

        Spinner spinner = (Spinner) view.findViewById(R.id.inventoryFilter);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.inventory_filters, R.layout.inventory_filter);
        arrayAdapter.setDropDownViewResource(R.layout.inventory_filter);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        android.widget.SearchView searchView = (android.widget.SearchView) view.findViewById(R.id.action_search_inventory);
        searchView.setOnQueryTextListener(this);

        FloatingActionButton fab = view.findViewById(R.id.inventoryfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder addCustomerDialog = new AlertDialog.Builder(getContext());
                final View view4 = getLayoutInflater().inflate(R.layout.add_dvd_popup, null);
                addCustomerDialog.setView(view4);
                final AlertDialog add = addCustomerDialog.create();
                add.show();

                final EditText tempID = (EditText) view4.findViewById(R.id.add_movie_id);
                final EditText tempUPC = (EditText) view4.findViewById(R.id.add_movie_code);
                final EditText tempTitle = (EditText) view4.findViewById(R.id.add_movie_title);
                final EditText tempReleaseDate = (EditText) view4.findViewById(R.id.add_movie_releasedate);
                final EditText tempDirector = (EditText) view4.findViewById(R.id.add_movie_director);
                final EditText tempActors = (EditText) view4.findViewById(R.id.add_movie_actors);
                final EditText tempGenre = (EditText) view4.findViewById(R.id.add_movie_genre);
                final EditText tempCondition = (EditText) view4.findViewById(R.id.add_movie_condition);


                Button addConfirm = (Button) view4.findViewById(R.id.add_movie_confirm);
                Button addCancel = (Button) view4.findViewById(R.id.add_movie_cancel);

                addConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!tempID.getText().toString().isEmpty() && !tempUPC.getText().toString().isEmpty() &&
                                !tempTitle.getText().toString().isEmpty() && !tempReleaseDate.getText().toString().isEmpty() && !tempDirector.getText().toString().isEmpty() &&
                                !tempActors.getText().toString().isEmpty()  && !tempGenre.getText().toString().isEmpty()  && !tempCondition.getText().toString().isEmpty()) {
                            if (db.createDvd(tempUPC.getText().toString(), tempID.getText().toString(), tempTitle.getText().toString(), tempDirector.getText().toString(),
                                    tempCondition.getText().toString(), tempReleaseDate.getText().toString(), tempGenre.getText().toString(), tempActors.getText().toString())) {
                                Toast.makeText(getContext(), "Movie Added.", Toast.LENGTH_SHORT).show();
                                add.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Movie Addition Failed.", Toast.LENGTH_SHORT).show();
                                add.dismiss();
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

        movies = getMovies();
        ArrayList<String> movieNames = getMovieNames(movies);
        movieInformation = getMovieInformation(movies);
        adapter = new InventoryListAdapter(getContext(),movieNames,movieInformation);
        movieList.setAdapter(adapter);

        movieList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    movieList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                currentSelectedGroup = groupPosition;
            }
        });

        return view;
    }

    public HashMap<String, ArrayList<String>> getMovieInformation(ArrayList<InventoryItem> a)
    {
        HashMap<String, ArrayList<String>> movieInfo = new HashMap<>();
        for(int i=0;i<a.size();i++)
        {
            ArrayList<String> tempList = new ArrayList<String>();
            InventoryItem temp = a.get(i);
            tempList.add("Movie ID: "+temp.getiD());
            tempList.add("Release Date: "+temp.getReleaseDate());
            tempList.add("Genre: "+temp.getGenre());
            tempList.add("Condition: "+temp.getCondition());
            tempList.add("Director: "+temp.getDirector());
            tempList.add("Actors: "+temp.getActors());

            movieInfo.put(a.get(i).getTitle(), tempList);
        }
        return movieInfo;
    }

    public ArrayList<String> getMovieNames(ArrayList<InventoryItem> a)
    {
        ArrayList<String> names = new ArrayList<>();
        for(int i = 0;i<a.size();i++)
        {
            names.add(a.get(i).getTitle());
        }
        return names;
    }


    public ArrayList<InventoryItem> getMovies()
    {
        ArrayList<InventoryItem> customerTempList = new ArrayList<>();

        String tempUPC = "";
        String tempTitle = "";
        String tempID = "";
        String tempReleaseDate = "";
        String tempDirector = "";
        String tempActors = "";
        String tempGenre = "";
        String tempCondition = "";

        Cursor res = db.getAllDVDs();
        if(res.getCount() != 0)
        {
            while(res.moveToNext())
            {
                tempUPC = res.getString(res.getColumnIndex("UPCCode"));
                tempTitle = res.getString(res.getColumnIndex("name"));
                tempID = res.getString(res.getColumnIndex("id"));
                tempReleaseDate = res.getString(res.getColumnIndex("releaseDate"));
                tempDirector = res.getString(res.getColumnIndex("director"));
                tempActors = res.getString(res.getColumnIndex("actors"));
                tempGenre = res.getString(res.getColumnIndex("genre"));
                tempCondition = res.getString(res.getColumnIndex("condition"));
                InventoryItem movieItem = new InventoryItem(tempUPC,tempTitle,tempID,tempReleaseDate,tempDirector,tempActors,tempGenre,tempCondition);
                customerTempList.add(movieItem);
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
        ArrayList<InventoryItem> movieListFiltered = new ArrayList<>();

        //Actors
        if(spinnerSelection == 1)
        {
            for(InventoryItem movie : movies) {
                if (movie.getActors().toLowerCase().contains(userInput)) {
                    movieListFiltered.add(movie);
                }
            }
            movieList = (ExpandableListView) view.findViewById(R.id.inventory_listed);
            ArrayList<String> movieNames = getMovieNames(movieListFiltered);
            movieInformation = getMovieInformation(movieListFiltered);
            adapter = new InventoryListAdapter(getContext(),movieNames,movieInformation);
            movieList.setAdapter(adapter);
        }
        //Director
        else if(spinnerSelection == 2)
        {
            for(InventoryItem movie : movies) {
                if (movie.getDirector().toLowerCase().contains(userInput)) {
                    movieListFiltered.add(movie);
                }
            }
            movieList = (ExpandableListView) view.findViewById(R.id.inventory_listed);
            ArrayList<String> movieNames = getMovieNames(movieListFiltered);
            movieInformation = getMovieInformation(movieListFiltered);
            adapter = new InventoryListAdapter(getContext(),movieNames,movieInformation);
            movieList.setAdapter(adapter);
        }
        // release Date
        else if(spinnerSelection == 3)
        {
            for(InventoryItem movie : movies) {
                if (movie.getReleaseDate().toLowerCase().contains(userInput)) {
                    movieListFiltered.add(movie);
                }
            }
            movieList = (ExpandableListView) view.findViewById(R.id.inventory_listed);
            ArrayList<String> movieNames = getMovieNames(movieListFiltered);
            movieInformation = getMovieInformation(movieListFiltered);
            adapter = new InventoryListAdapter(getContext(),movieNames,movieInformation);
            movieList.setAdapter(adapter);
        }
        // UPC Code
        else if(spinnerSelection == 4)
        {
            for(InventoryItem movie : movies) {
                if (movie.getUPCCode().toLowerCase().contains(userInput)) {
                    movieListFiltered.add(movie);
                }
            }
            movieList = (ExpandableListView) view.findViewById(R.id.inventory_listed);
            ArrayList<String> movieNames = getMovieNames(movieListFiltered);
            movieInformation = getMovieInformation(movieListFiltered);
            adapter = new InventoryListAdapter(getContext(),movieNames,movieInformation);
            movieList.setAdapter(adapter);
        }
        else
        {
            for(InventoryItem movie : movies) {
                if (movie.getTitle().toLowerCase().contains(userInput)) {
                    movieListFiltered.add(movie);
                }
            }
            movieList = (ExpandableListView) view.findViewById(R.id.inventory_listed);
            ArrayList<String> movieNames = getMovieNames(movieListFiltered);
            movieInformation = getMovieInformation(movieListFiltered);
            adapter = new InventoryListAdapter(getContext(),movieNames,movieInformation);
            movieList.setAdapter(adapter);
        }
        return false;
    }

    public void refreshList()
    {
        movieList = (ExpandableListView) view.findViewById(R.id.inventory_listed);
        movies = getMovies();
        ArrayList<String> movieNames = getMovieNames(movies);
        movieInformation = getMovieInformation(movies);
        adapter = new InventoryListAdapter(getContext(),movieNames,movieInformation);
        movieList.setAdapter(adapter);
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
