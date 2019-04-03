package thomas.sullivan.videoshoppe.resources;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import thomas.sullivan.videoshoppe.activity.R;

public class EmployeeListAdapter extends BaseAdapter {

    private ArrayList<EmployeeItem> employeeItems;
    private LayoutInflater layoutInflater;

    public EmployeeListAdapter(Context aContext, ArrayList<EmployeeItem> aEmployeeItems)
    {
        this.employeeItems = aEmployeeItems;
        layoutInflater = LayoutInflater.from(aContext);
    }


    @Override
    public int getCount() {
        return employeeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_employee_layout, null);
            holder = new ViewHolder();
            holder.firstNameView = (TextView) convertView.findViewById(R.id.firstName);
            holder.lastNameView = (TextView) convertView.findViewById(R.id.lastName);
            holder.usernameView = (TextView) convertView.findViewById(R.id.username);
            holder.adminView = (TextView) convertView.findViewById(R.id.admin);
            holder.userIDView = (TextView) convertView.findViewById(R.id.userID);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.firstNameView.setText(employeeItems.get(position).getFirstName());
        holder.lastNameView.setText(employeeItems.get(position).getLastName());
        holder.userIDView.setText("Employee ID: "+employeeItems.get(position).getUserID());
        holder.usernameView.setText("Username: "+employeeItems.get(position).getUsername());

        if(employeeItems.get(position).getAdmin().equalsIgnoreCase("yes"))
        {
            holder.adminView.setText("Admin: No");
        }else {
            holder.adminView.setText("Admin: Yes");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView firstNameView;
        TextView lastNameView;
        TextView usernameView;
        TextView adminView;
        TextView userIDView;
    }
}
