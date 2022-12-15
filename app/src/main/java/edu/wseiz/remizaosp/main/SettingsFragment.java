package edu.wseiz.remizaosp.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wseiz.remizaosp.LoginActivity;
import edu.wseiz.remizaosp.databinding.FragmentSettingsBinding;
import edu.wseiz.remizaosp.viewmodels.Repository;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private Repository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        binding.signout.setOnClickListener(v -> {
            Intent activityIntent = new Intent(getActivity(), LoginActivity.class);
            getActivity().getViewModelStore().clear();
            repository.signOut();
            startActivity(activityIntent);
            requireActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}