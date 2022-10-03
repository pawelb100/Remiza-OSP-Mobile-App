package edu.wseiz.remizaosp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.wseiz.remizaosp.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fAuth = FirebaseAuth.getInstance();

        binding.signin.setOnClickListener(view -> {

            String email = binding.emailbox.getText().toString();
            String pass = binding.passwordbox.getText().toString();

            if(!(email.isEmpty()))
            {
                if(!(pass.isEmpty()))
                {
                    fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            System.out.println("Register Successful");
                            Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            System.out.println("Register Failure");
                            Toast.makeText(this, "Register Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

        binding.goBackToLogin.setOnClickListener(view -> {
            finish();

        });

    }
}