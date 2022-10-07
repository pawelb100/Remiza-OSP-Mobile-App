package edu.wseiz.remizaosp.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import edu.wseiz.remizaosp.MainActivity;
import edu.wseiz.remizaosp.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentRegisterBinding.inflate(inflater, container, false);

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
                            Toast.makeText(getActivity(), "Rejestracja udana", Toast.LENGTH_SHORT).show();
                            Intent activityIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(activityIntent);
                            requireActivity().finish();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Rejestracja nieudana", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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