package thomas.sullivan.videoshoppe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.InputType;

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

