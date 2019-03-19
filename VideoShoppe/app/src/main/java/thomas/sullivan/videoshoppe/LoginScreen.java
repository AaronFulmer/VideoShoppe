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

public class LoginScreen extends AppCompatActivity {

    UserDatabase usersDatabase;
    EditText editUsername,editPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        usersDatabase = new UserDatabase(this);

        // DEBUGGING
        // Removes current database
       // usersDatabase.wipeDatabase();

        editUsername = (EditText)findViewById(R.id.editText_Username);
        editPassword = (EditText)findViewById(R.id.editText_Password);
        btnLogin = (Button)findViewById(R.id.button_Login);
        login();

        // DEBUGGING
        // ADD ADMIN TO DATABASE
        // boolean isInserted = usersDatabase.createUser("TEST ID", "TEST LAST NAME", "TEST FIRST NAME", "Admin", "Admin", "Yes");

    }

    public void openMainMenu()
    {
            Intent intent = new Intent(LoginScreen.this, MainMenu.class);
            startActivity(intent);
    }


    public void login()
    {
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String usernameEntry = editUsername.getText().toString();
                        String passwordEntry = editPassword.getText().toString();

                        //temp
                        if(usernameEntry.equalsIgnoreCase("ADMIN") && passwordEntry.equalsIgnoreCase("ADMIN"))
                        {
                            Toast.makeText(LoginScreen.this,"Logging In...",Toast.LENGTH_LONG).show();
                            openMainMenu();
                        }
                        else {
                            Toast.makeText(LoginScreen.this,"Invalid Username or Password.",Toast.LENGTH_LONG).show();
                        }


                        /*
                        if(usernameEntry.equalsIgnoreCase("")) {
                            usernameEntry = "null";
                        }

                        boolean userMatch = usersDatabase.searchUsername(usernameEntry);

                        if(userMatch == true)
                        {
                            boolean passMatch = usersDatabase.searchPassword(usernameEntry,passwordEntry);
                            if(passMatch == true)
                            {
                                Toast.makeText(LoginScreen.this,"Login Successful",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(LoginScreen.this,"Invalid Password",Toast.LENGTH_LONG).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(LoginScreen.this,"Invalid Username",Toast.LENGTH_LONG).show();
                        }
                        */
                    }
                }
        );
    }

}
