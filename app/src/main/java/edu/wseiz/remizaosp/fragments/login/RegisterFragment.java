package edu.wseiz.remizaosp.fragments.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.wseiz.remizaosp.activities.MainActivity;
import edu.wseiz.remizaosp.databinding.FragmentRegisterBinding;
import edu.wseiz.remizaosp.models.Role;
import edu.wseiz.remizaosp.models.User;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentRegisterBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.signup.setOnClickListener(view -> {

            String name = binding.namebox.getText().toString();
            String email = binding.emailbox.getText().toString();
            String pass = binding.passwordbox.getText().toString();
            String confirm = binding.passwordConfirmbox.getText().toString();

            if(verifyInput(name, email, pass, confirm))
            {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        DatabaseReference ref = database.getReference().child("users").child(auth.getCurrentUser().getUid());

                        String key = ref.push().getKey();
                        User user = new User();
                        user.setUid(auth.getUid());
                        user.setEmail(email);
                        user.setName(name);
                        user.setRole(Role.Rescuer);

                        ref.setValue(user);

                        Toast.makeText(getActivity(), "Rejestracja udana", Toast.LENGTH_LONG).show();
                        Intent activityIntent = new Intent(getActivity(), MainActivity.class);
                        startActivity(activityIntent);
                        requireActivity().finish();

                    }
                    else
                        Toast.makeText(getActivity(), "Rejestracja nieudana", Toast.LENGTH_LONG).show();

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

    private boolean verifyInput(String name, String email, String pass, String passConfirm) {
        if (!(name.isEmpty())) {
            if(!(email.isEmpty())) {
                if (!(pass.isEmpty())) {
                    if (pass.equals(passConfirm))
                        return true;
                    else
                    {
                        Toast.makeText(getActivity(), "Hasła są różne", Toast.LENGTH_SHORT).show();
                        return false;
                    }
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
}