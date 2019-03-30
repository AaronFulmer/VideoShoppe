package thomas.sullivan.videoshoppe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import thomas.sullivan.videoshoppe.activity.R;
import thomas.sullivan.videoshoppe.resources.Database;

public class LoginScreen extends AppCompatActivity {

    Database database;
    EditText editUsername,editPassword;
    Button btnLogin;
    Button btnTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        database = new Database(this);

        editUsername = (EditText)findViewById(R.id.editText_Username);
        editPassword = (EditText)findViewById(R.id.editText_Password);
        btnLogin = (Button)findViewById(R.id.button_Login);
        btnTest2 = (Button)findViewById(R.id.button_test2);

        //Wipes database and adds admin user.
        //database.wipeDatabase();

        login();

    }

    public void login()
    {
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String usernameEntry = editUsername.getText().toString();
                        String passwordEntry = editPassword.getText().toString();

                        boolean correctUsername = database.searchUsername(usernameEntry);
                        boolean correctPassword = database.searchPassword(usernameEntry,passwordEntry);
                        if(correctUsername == true && correctPassword == true)
                        {
                            toastMessage("Logging in...");
                            openMainMenu();
                        } else {
                            toastMessage("Incorrect username/password, Try again!");
                        }

                    }
                }
        );

        btnTest2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        test2();
                    }
                }
        );
    }

    public void openMainMenu()
    {
        Intent intent = new Intent(LoginScreen.this, Menu.class);
        startActivity(intent);
    }

    //Views database
    public void test2()
    {
        String result = database.debugger();
        toastMessage(result);
    }


    public void toastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
