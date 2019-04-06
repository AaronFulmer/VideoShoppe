package thomas.sullivan.videoshoppe.resources;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import thomas.sullivan.videoshoppe.activity.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;


public class EmployeeParentHolder extends GroupViewHolder {

    public TextView name;
    public ImageView arrow;

    public EmployeeParentHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.list_editable_employeeName);
        arrow = (ImageView) itemView.findViewById(R.id.list_employee_arrow);

    }

    public void setName(ExpandableGroup group) {
        name.setText(group.getTitle());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }


}
