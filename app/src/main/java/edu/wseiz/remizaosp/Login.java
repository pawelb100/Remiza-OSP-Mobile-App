package edu.wseiz.remizaosp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.wseiz.remizaosp.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fAuth = FirebaseAuth.getInstance();


        binding.signin.setOnClickListener(view -> {

            String email = binding.emailbox.getText().toString();
            String pass = binding.passwordbox.getText().toString();

            if(!(email.isEmpty()))
            {
               if(!(pass.isEmpty()))
               {
                   fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                       if(task.isSuccessful())
                       {
                           System.out.println("Login Successful");
                           Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           System.out.println("Login Failure");
                           Toast.makeText(this, "Login Failure", Toast.LENGTH_SHORT).show();
                       }
                   });
               }
            }

        });

        binding.openRegister.setOnClickListener(view -> {

            Intent myIntent = new Intent(Login.this, Register.class);
            //myIntent.putExtra("key", value); //Optional parameters
            Login.this.startActivity(myIntent);
        });




    }



}