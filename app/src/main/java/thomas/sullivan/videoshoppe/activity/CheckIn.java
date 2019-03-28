package thomas.sullivan.videoshoppe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import thomas.sullivan.videoshoppe.activity.R;

public class CheckIn extends AppCompatActivity {

    Button btnscan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        btnscan = (Button)findViewById(R.id.button_scan);

        scanDVD();
    }

    public void scanDVD()
    {
        btnscan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        Toast.makeText(CheckIn.this,"Scanning...",Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
}

