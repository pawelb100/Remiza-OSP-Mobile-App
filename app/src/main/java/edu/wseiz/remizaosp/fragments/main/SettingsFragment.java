package edu.wseiz.remizaosp.fragments.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import edu.wseiz.remizaosp.activities.LoginActivity;
import edu.wseiz.remizaosp.databinding.FragmentSettingsBinding;
import edu.wseiz.remizaosp.listeners.FetchUserRoleListener;
import edu.wseiz.remizaosp.listeners.UpdateListener;
import edu.wseiz.remizaosp.models.Role;
import edu.wseiz.remizaosp.viewmodels.Repository;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private Repository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        binding.btnSaveName.setOnClickListener(view -> {

            if (!(binding.etName.getText().toString().isEmpty())) {
                repository.updateUserName(binding.etName.getText().toString(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        info("Zapisano nazwę użytkownika");
                    }

                    @Override
                    public void onFailed() {
                        handleFail();
                    }
                });
            }

        });


        binding.btnSignout.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Wylogować się?")
                    .setMessage("Jesteś pewien?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        repository.signOut();
                        Intent activityIntent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(activityIntent);
                        requireActivity().finish();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });


        repository.fetchUserRole(new FetchUserRoleListener() {
            @Override
            public void onSuccess(Role role) {
                if (role==Role.Officer) {
                    binding.btnSetRegions.setVisibility(View.VISIBLE);
                    binding.btnSetThreats.setVisibility(View.VISIBLE);
                    binding.btnSetStatuses.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailed() {}
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void info(String text) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void handleFail() {
        info("Wystąpił problem z połączeniem");
    }
}