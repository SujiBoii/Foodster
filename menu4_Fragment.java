package app.z0nen.slidemenu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

/**
 * Created by Z0NEN on 10/22/2014.
 */
public class menu4_Fragment extends Fragment implements View.OnClickListener {

    private Button speichernButton;
    private Button logoutButton;
    private TextView helloTW;
    private EditText nameET;
    private EditText geburtsdatumET;
    private EditText infoET;

    private TextView testName,testGeb;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;



    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu4_layout, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            getActivity().finish();
            // startActivity(new Intent(getActivity(), menu5_Fragment.class));
        }

        FirebaseUser userr = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userr.getUid());


        speichernButton = (Button)rootview.findViewById(R.id.speichernButton);
        logoutButton = (Button)rootview.findViewById(R.id.buttonLogout);
        nameET = (EditText)rootview.findViewById(R.id.nameET);
        geburtsdatumET =(EditText)rootview.findViewById(R.id.geburtsdatumET);
       // helloTW = (TextView)rootview.findViewById(R.id.begruessungTW);
        //helloTW.setText("Welcome"+ " " + userr.getEmail());
        infoET = (EditText)rootview.findViewById(R.id.infoET);

        testName = (TextView)rootview.findViewById(R.id.testName);
        testGeb = (TextView)rootview.findViewById(R.id.testGeb);

        logoutButton.setOnClickListener(this);
        speichernButton.setOnClickListener(this);


        ladeInformation();


        return rootview;
    }

    private void ladeInformation(){


      databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    UserInformation user = dataSnapshot.getValue(UserInformation.class);

                    if(user != null) {
                        testName.setText(user.name);
                        testGeb.setText(user.geburtsdatum);
                    }else{
                        testName.setText("");
                        testGeb.setText("");
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void speicherInformation(){
        String name = nameET.getText().toString().trim();
        String geburtsdatum = geburtsdatumET.getText().toString().trim();

        UserInformation userInformation = new UserInformation();
        userInformation.setName(name);
        userInformation.setGeburtsdatum(geburtsdatum);


        databaseReference.setValue(userInformation);

        Toast.makeText(getActivity(), "Information wurde gespeichert...", Toast.LENGTH_SHORT).show();




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    UserInformation user = dataSnapshot.getValue(UserInformation.class);


                    testName.setText(user.getName());
                    testGeb.setText(user.getGeburtsdatum());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.");

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view == logoutButton){
            firebaseAuth.signOut();

            testName.setText("");
            testGeb.setText("");

            Fragment newFragment = new menu1_Fragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        if (view == speichernButton){
            speicherInformation();


        }

    }
}
