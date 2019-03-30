package thomas.sullivan.videoshoppe.resources;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class IntValueFormatter extends com.github.mikephil.charting.formatter.ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value);
    }
}
