package edu.wseiz.remizaosp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null)
        {
            //Start Activity
            finish();
        }


        String email = "pbbp100@gmail.com";
        String pass = "P@ssw0rd";

        fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                System.out.println("Register Succesful");
            }
            else
            {
                System.out.println("Register Failure");
            }
        });

    }
}