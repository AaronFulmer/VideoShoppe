package thomas.sullivan.videoshoppe.resources;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import thomas.sullivan.videoshoppe.activity.R;

public class EmployeeChildHolder extends ChildViewHolder {

    public TextView userIDView;
    public TextView employeeCellphone;
    public TextView employeeEmail;

    public EmployeeChildHolder(View itemView) {
        super(itemView);

        employeeCellphone = (TextView) itemView.findViewById(R.id.list_editable_employeeCell);
        userIDView = (TextView) itemView.findViewById(R.id.list_editable_employeeID);
        employeeEmail = (TextView) itemView.findViewById(R.id.list_editable_employeeEmail);

    }

    public void onBind(EmployeeChild employee) {
        employeeEmail.setText("Email: "+employee.getEmail());
        employeeCellphone.setText("Cell: "+employee.getCellPhone());
        userIDView.setText("ID: "+employee.getUserID());
    }
}
