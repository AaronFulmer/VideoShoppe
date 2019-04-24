package thomas.sullivan.videoshoppe.fragment;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import thomas.sullivan.videoshoppe.activity.R;
import thomas.sullivan.videoshoppe.resources.IntValueFormatter;
import thomas.sullivan.videoshoppe.resources.UserDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    UserDatabase db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    BarChart chart;
    Calendar calendar;

    // Calender information
//    private String currentDayString;
//    private int currentDayInt;
//    private int sundayCount;
//    private int mondayCount;
//    private int tuesdayCount;
//    private int wednesdayCount;
//    private int thursdayCount;
//    private int fridayCount;
//    private int saturdayCount;
//    private int thirtyDayCounter;
//    private int thirtyDayDisplay;
//    private int rentalsInDay;



    private String currentMonthString;
    private int currentMonth;
    private int currentYear;
    private int january;
    private int february;
    private int march;
    private int april;
    private int may;
    private int june;
    private int july;
    private int august;
    private int septempber;
    private int october;
    private int november;
    private int december;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new UserDatabase(this.getContext());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Calendar
        final DateFormat d = new SimpleDateFormat("yyyy");
        calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Now use today date.
        //currentDayInt = calendar.get(Calendar.DAY_OF_WEEK);
        currentMonth = calendar.get(Calendar.MONTH);

        currentYear = Integer.parseInt(d.format(calendar.getTime()));
    }

    public void createChart()
    {
        calendarRun();
        setData(7);
        chart.setFitBars(true);
        chart.setScaleEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);

    }

    public void calendarRun()
    {
        //Sets currentDay to the day of the week
        switch(currentMonth)
        {
            case 1:
                currentMonthString = "February";
                february = 0;
                february += db.getRentals(currentMonth, currentYear);
                break;
            case 2:
                currentMonthString = "March";
                march = 0;
                march += db.getRentals(currentMonth, currentYear);
                break;
            case 3:
                currentMonthString = "April";
                april = 0;
                april += db.getRentals(currentMonth, currentYear);
                break;
            case 4:
                currentMonthString = "May";
                may = 0;
                may += db.getRentals(currentMonth, currentYear);
                break;
            case 5:
                currentMonthString = "June";
                june = 0;
                june += db.getRentals(currentMonth, currentYear);
                break;
            case 6:
                currentMonthString = "July";
                july = 0;
                july += db.getRentals(currentMonth, currentYear);
                break;
            case 7:
                currentMonthString = "August";
                august = 0;
                august += db.getRentals(currentMonth, currentYear);
                break;
            case 8:
                currentMonthString = "September";
                septempber = 0;
                septempber += db.getRentals(currentMonth, currentYear);
                break;
            case 9:
                currentMonthString = "October";
                october = 0;
                october += db.getRentals(currentMonth, currentYear);
                break;
            case 10:
                currentMonthString = "November";
                november = 0;
                november += db.getRentals(currentMonth, currentYear);
                break;
            case 11:
                currentMonthString = "December";
                december = 0;
                december += db.getRentals(currentMonth, currentYear);
                break;
            default:
                currentMonthString = "January";
                january = 0;
                january += db.getRentals(currentMonth, currentYear);
                break;
        }
    }

    private void setData(int count)
    {
        int[] intArray = new int[7];
        String[] dayNames = new String[7];
        ArrayList<BarEntry> yVals = new ArrayList<>();

        int tempMonth = currentMonth;
        int tempYear = currentYear;

        String tempDay = "";
        int arrayCounter = 0;
        boolean isFinished = false;

        while(isFinished == false)
        {
            int numberToAdd = -1;
            switch(tempMonth)
            {
                case 0:
                    tempDay = "Jan";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 1:
                    tempDay = "Feb";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 2:
                    tempDay = "Mar";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 3:
                    tempDay = "Apr";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 4:
                    tempDay = "May";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 5:
                    tempDay = "Jun";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 6:
                    tempDay = "Jul";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 7:
                    tempDay = "Aug";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 8:
                    tempDay = "Sep";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                break;
                case 9:
                    tempDay = "Oct";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 10:
                    tempDay = "Nov";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
                case 11:
                    tempDay = "Dec";
                    numberToAdd = db.getRentals(tempMonth, tempYear);
                    break;
            }


            intArray[6 - arrayCounter] = numberToAdd;
            dayNames[6 - arrayCounter] = tempDay;

            dayNames[6] = currentMonthString;

            // controls day #s
            if(tempMonth == 0)
            {
                tempMonth = 11;
                tempYear -= 1;
            } else {
                tempMonth--;
            }

            arrayCounter++;

            if(arrayCounter == 7)
            {
                isFinished = true;
            }
        }

        for(int i=0;i<count; i++)
        {
            yVals.add(new BarEntry(i, intArray[i]));
        }

        BarDataSet set = new BarDataSet(yVals, "Data Set");

        //  FORMATTING

        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setDrawValues(true);

        set.setLabel("Movie Rentals");

        //   END FORMATTING

        BarData data = new BarData(set);

        //  FORMATTING
        set.setHighlightEnabled(false);
        data.setValueTextSize(16);
        data.setValueFormatter(new IntValueFormatter());
        set.setValueFormatter(new IntValueFormatter());

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(14);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dayNames));
        xAxis.setDrawGridLines(false);

        YAxis rightYAxis = chart.getAxisRight();
        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setTextSize(14);
        rightYAxis.setEnabled(false);

        chart.setExtraOffsets(10, 10, 10, 10);
        chart.getLegend().setEnabled(false);

        //   END FORMATTING

        chart.setData(data);
        chart.invalidate();
        chart.animateY(1200);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_home, container, false);
        chart = (BarChart) view.findViewById(R.id.chart);
        //Creates Bar Graph
        createChart();
        // Inflate the layout for this fragment
        return view;
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
}
