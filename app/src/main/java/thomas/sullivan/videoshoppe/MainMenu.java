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


public class MainMenu extends AppCompatActivity{

    Button btnCheckIn;
    Button btnCheckOut;
    Button btnDebug;
    UserDatabase usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnCheckIn = (Button)findViewById(R.id.button_check_in);
        btnCheckOut = (Button)findViewById(R.id.button_check_out);
        btnDebug = (Button)findViewById(R.id.button_debug);

        mainMenu();

    }

    public void gotoCheckIn()
    {
        Intent intent = new Intent(MainMenu.this, CheckIn.class);
        startActivity(intent);
    }

    public void gotoCheckOut()
    {
        Intent intent = new Intent(MainMenu.this, MainMenu.class);
        startActivity(intent);
    }

    public void mainMenu()
    {

        //DEBUG
        btnDebug.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Cursor res = usersDatabase.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("ID :"+ res.getString(0)+"\n");
                            buffer.append("First :"+ res.getString(1)+"\n");
                            buffer.append("Last :"+ res.getString(2)+"\n");
                            buffer.append("USER :"+ res.getString(3)+"\n");
                            buffer.append("PASS :"+ res.getString(4)+"\n");
                            buffer.append("ADMIN :"+ res.getString(5)+"\n\n");
                        }

                        showMessage("Users",buffer.toString());
                    }
                }
        );

        //GOTO CHECK IN PAGE
        btnCheckIn.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        gotoCheckIn();
                    }
                }
        );

        //GOTO CHECK OUT PAGE
        btnCheckOut.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        gotoCheckOut();
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}