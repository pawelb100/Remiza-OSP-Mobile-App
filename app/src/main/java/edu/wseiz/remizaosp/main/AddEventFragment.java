package edu.wseiz.remizaosp.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import edu.wseiz.remizaosp.adapters.SpinnerAdapter;
import edu.wseiz.remizaosp.databinding.FragmentAddEventBinding;
import edu.wseiz.remizaosp.listeners.AddEventListener;
import edu.wseiz.remizaosp.models.Address;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.viewmodels.Repository;


public class AddEventFragment extends Fragment {

private FragmentAddEventBinding binding;
private Repository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        List<String> regions = new ArrayList<>();
        regions.add("Mokotów");
        regions.add("Ursynów");
        regions.add("Wilanów");
        regions.add("Włochy");
        regions.add("Ochota");

        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), regions);
        binding.spinnerRegion.setAdapter(adapter);

        List<String> threats = new ArrayList<>();
        threats.add("Pożar");
        threats.add("Wypadek");
        threats.add("Drzewo");

        SpinnerAdapter adapter2 = new SpinnerAdapter(getContext(), threats);
        binding.spinnerThreat.setAdapter(adapter2);

        binding.btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.setTitle(binding.spinnerThreat.getSelectedItem().toString());
                event.setAddress(new Address(binding.etAddress.getText().toString(), binding.spinnerRegion.getSelectedItem().toString()));
                event.setDescription(binding.etDescription.getText().toString());
                event.setOngoing(binding.switchOngoing.isChecked());
                event.setTimestamp(System.currentTimeMillis());
                repository.addEvent(event, new AddEventListener() {
                    @Override
                    public void onSuccess(String generatedId) {
                        Snackbar snackbar = Snackbar.make(binding.getRoot(), "Zdarzenie dodane", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        Navigation.findNavController(binding.getRoot()).popBackStack();
                    }

                    @Override
                    public void onFailed() {
                        Snackbar snackbar = Snackbar.make(binding.getRoot(), "Błąd", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
            }
        });

        return binding.getRoot();
    }
}