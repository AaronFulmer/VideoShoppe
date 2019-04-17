package thomas.sullivan.videoshoppe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import thomas.sullivan.videoshoppe.resources.UserDatabase;

public class LoginScreen extends AppCompatActivity {

    UserDatabase database;
    EditText editUsername,editPassword;
    Button btnLogin;
    Button btnTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        database = new UserDatabase(this);

        editUsername = (EditText)findViewById(R.id.editText_Username);
        editPassword = (EditText)findViewById(R.id.editText_Password);
        btnLogin = (Button)findViewById(R.id.button_Login);

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

                        if(database.searchCredentials(usernameEntry,passwordEntry))
                        {
                            toastMessage("Logging in.");
                            database.setCurrentUser(usernameEntry);
                            openMainMenu();
                        }
                        else{
                            toastMessage("Invalid Credentials.");
                        }
                    }
                }
        );
    }

    public void openMainMenu()
    {
        Intent intent = new Intent(LoginScreen.this, Menu.class);
        startActivity(intent);
    }


    public void toastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
