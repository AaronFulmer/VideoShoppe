package thomas.sullivan.videoshoppe.resources;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class EmployeeItem extends ExpandableGroup<EmployeeChild> {

    public EmployeeItem(String title, List<EmployeeChild> items) {

        super(title, items);

    }

}
