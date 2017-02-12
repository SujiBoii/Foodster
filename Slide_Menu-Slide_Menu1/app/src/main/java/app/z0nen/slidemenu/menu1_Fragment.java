package app.z0nen.slidemenu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;


/**
 * Created by Z0NEN on 10/22/2014.
 */
public class menu1_Fragment extends Fragment implements OnClickListener{
    View rootview;
    Button loginButton;
    private EditText user, pass;
    // Progress Dialog
    Button  mRegister;
    Button mSubmit;
    TextView signin;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu1_layout, container, false);
        //registerButton = (Button) rootview.findViewById(R.id.registerButton);
        user = (EditText)rootview.findViewById(R.id.userName);
        pass = (EditText)rootview.findViewById(R.id.userPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //Profile activity start
            Fragment newFragment = new menu4_Fragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack

            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

        progressDialog = new ProgressDialog(getActivity());

        signin = (TextView)rootview.findViewById(R.id.signinView);
        signin.setOnClickListener(this);


        mRegister = (Button)rootview.findViewById(R.id.register_button);
        mRegister.setOnClickListener(this);


        return rootview;
}

    private void registerUser(){
        String email = user.getText().toString().trim();
        String passwort = pass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email ist leer.
            Toast.makeText(getActivity(), "Bitte eine Email adresse eingeben", Toast.LENGTH_LONG).show();
            //wird dem User nicht erlauben weiter zu machen
            return;
        }

        if(TextUtils.isEmpty(passwort)){
        // passwort ist leer
            Toast.makeText(getActivity(), "Bitte ein Passwort eingeben", Toast.LENGTH_LONG).show();
            return;
        }

        //Wenn alles ok ist

        progressDialog.setMessage("Sie werden registriert");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,passwort)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            //User ist erfolgreich registriert
                            //Ab hier wird die Userprofile Activity gestartet

                            Fragment newFragment = new menu4_Fragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                            transaction.replace(R.id.container, newFragment);
                            transaction.addToBackStack(null);

                            transaction.commit();
                            Toast.makeText(getActivity(), "erfolgreich registriert", Toast.LENGTH_LONG).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Registrierung fehlgeschlagen", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


    @Override
    public void onClick(View v) {
        if(v == mRegister)
        {
            registerUser();
        }
        else if(v == signin)
        {
            //Command when user is already registered
            startActivity(new Intent(getActivity(), login.class));
        }


    }

}




