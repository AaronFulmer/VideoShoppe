package thomas.sullivan.videoshoppe.resources;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableList;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.ArrayList;
import java.util.List;

import thomas.sullivan.videoshoppe.activity.Employees;
import thomas.sullivan.videoshoppe.activity.R;

import static com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition.GROUP;


public class EmployeeListAdapter extends ExpandableRecyclerViewAdapter<EmployeeParentHolder, EmployeeChildHolder> {

    public EmployeeListAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public EmployeeParentHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_employee_layout, parent, false);
        return new EmployeeParentHolder(view);
    }

    @Override
    public EmployeeChildHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_employee_layout_expandable, parent, false);
        return new EmployeeChildHolder(view);
    }

    @Override
    public void onBindChildViewHolder(EmployeeChildHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final EmployeeChild employee = ((EmployeeItem) group).getItems().get(childIndex);
        holder.onBind(employee);
    }

    @Override
    public void onBindGroupViewHolder(EmployeeParentHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setName(group);
    }

    @Override
    public void onGroupExpanded(int positionStart, int itemCount) {
        super.onGroupExpanded(positionStart, itemCount);
        for (int i = getItemCount()-1; i >= 0; i--) {
            if(i <= getItemCount()) {
                if(i != positionStart-1) {
                    if (getItemViewType(i) == GROUP && isGroupExpanded(i)) {
                        toggleGroup(i);
                    }
                }
            }
        }
    }

}
