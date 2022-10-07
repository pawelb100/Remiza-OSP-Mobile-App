package edu.wseiz.remizaosp.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.databinding.FragmentWelcomeBinding;

public class WelcomeFragment extends Fragment {

    private FragmentWelcomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);

        binding.signin.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_loginFragment);
        });

        binding.signup.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_registerFragment);
        });

        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}