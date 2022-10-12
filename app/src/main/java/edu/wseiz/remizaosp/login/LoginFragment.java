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

import edu.wseiz.remizaosp.MainActivity;
import edu.wseiz.remizaosp.databinding.ActivityLoginBinding;
import edu.wseiz.remizaosp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth fAuth;

    private Boolean verifyInput(String email, String pass) {
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        fAuth = FirebaseAuth.getInstance();

        binding.signin.setOnClickListener(view -> {

            String email = binding.emailbox.getText().toString();
            String pass = binding.passwordbox.getText().toString();

            if(verifyInput(email, pass))
            {
                    fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Logowanie udane", Toast.LENGTH_SHORT).show();
                            Intent activityIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(activityIntent);
                            requireActivity().finish();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Logowanie nieudane", Toast.LENGTH_SHORT).show();
                        }
                    });

            }

        });

        binding.gotoRegister.setOnClickListener(view -> {
            Navigation.findNavController(view).popBackStack();

        });



        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}