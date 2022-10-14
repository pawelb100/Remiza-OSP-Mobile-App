package edu.wseiz.remizaosp.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.wseiz.remizaosp.MainActivity;
import edu.wseiz.remizaosp.databinding.FragmentRegisterBinding;
import edu.wseiz.remizaosp.tools.Database;
import edu.wseiz.remizaosp.tools.DatabaseListener;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private Database database;

    private Boolean verifyInput(String name, String email, String pass) {
        if (!(name.isEmpty())) {
            if(!(email.isEmpty())) {
                if (!(pass.isEmpty())) {
                    return true;
                }
                else
                {
                    Toast.makeText(getActivity(), "Wprowadź hasło", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Wprowadź adres e-mail", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else
        {
            Toast.makeText(getActivity(), "Wprowadź nazwę użytkownika", Toast.LENGTH_SHORT).show();
            return false;
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentRegisterBinding.inflate(inflater, container, false);

        database = new Database();

        binding.signup.setOnClickListener(view -> {

            String name = binding.namebox.getText().toString();
            String email = binding.emailbox.getText().toString();
            String pass = binding.passwordbox.getText().toString();

            if(verifyInput(name, email, pass))
            {
                database.register(name, email, pass, new DatabaseListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), "Rejestracja udana", Toast.LENGTH_LONG).show();
                        Intent activityIntent = new Intent(getActivity(), MainActivity.class);
                        startActivity(activityIntent);
                        requireActivity().finish();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Toast.makeText(getActivity(), "Rejestracja nieudana", Toast.LENGTH_LONG).show();
                    }
                });

            }

        });

        binding.goBackToLogin.setOnClickListener(view -> {
            Navigation.findNavController(view).popBackStack();

        });

        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}