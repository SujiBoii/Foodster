package app.z0nen.slidemenu;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends Activity implements View.OnClickListener {

    private Button buttonLogin;
    private EditText emailET;
    private EditText passwortET;
    private TextView registerTW;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = (Button) findViewById(R.id.login_Button);
        emailET = (EditText) findViewById(R.id.userName);
        passwortET = (EditText) findViewById(R.id.userPassword);
        registerTW = (TextView) findViewById(R.id.registerView);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //Profile activity start
            finish();
           // startActivity(new Intent(getApplicationContext(),menu1_Fragment.class));
            Fragment newFragment = new menu4_Fragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
        progressDialog = new ProgressDialog(this);



        buttonLogin.setOnClickListener(this);
        registerTW.setOnClickListener(this);

    }

    private void userLogin(){
        String email = emailET.getText().toString().trim();
        String passwort = passwortET.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email ist leer.
            Toast.makeText(this, "Bitte eine Email adresse eingeben", Toast.LENGTH_LONG).show();
            //wird dem User nicht erlauben weiter zu machen
            return;
        }

        if(TextUtils.isEmpty(passwort)){
            // passwort ist leer
            Toast.makeText(this, "Bitte ein Passwort eingeben", Toast.LENGTH_LONG).show();
            return;
        }

        //Wenn alles ok ist

        progressDialog.setMessage("Login...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,passwort)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //start the profile acitivity
                            finish();
                           // startActivity(new Intent(getApplicationContext(),menu1_Fragment.class));
                            Fragment newFragment = new menu4_Fragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                            transaction.replace(R.id.menuLogin, newFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){
            userLogin();
        }
        if(view == registerTW){
            finish();

        }

    }
}
