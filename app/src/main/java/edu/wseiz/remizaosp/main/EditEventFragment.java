package edu.wseiz.remizaosp.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.wseiz.remizaosp.adapters.SpinnerAdapter;
import edu.wseiz.remizaosp.adapters.UsersListAdapter;
import edu.wseiz.remizaosp.databinding.FragmentEditEventBinding;
import edu.wseiz.remizaosp.listeners.AddEventListener;
import edu.wseiz.remizaosp.listeners.FetchEventListener;
import edu.wseiz.remizaosp.listeners.FetchUsersByParticipationsListener;
import edu.wseiz.remizaosp.listeners.UpdateListener;
import edu.wseiz.remizaosp.models.Address;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.Participation;
import edu.wseiz.remizaosp.models.User;
import edu.wseiz.remizaosp.viewmodels.Repository;

public class EditEventFragment extends Fragment {

    private FragmentEditEventBinding binding;
    private Repository repository;

    private String eventId;

    private Event currentEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditEventBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        eventId = getArguments().getString("eventId");

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


        repository.singleFetchEventById(eventId, new FetchEventListener() {
            @Override
            public void onSuccess(Event event) {

            binding.spinnerThreat.setSelection(threats.indexOf(event.getTitle()));
            binding.etAddress.setText(event.getAddress().getStreet());
            binding.spinnerRegion.setSelection(regions.indexOf(event.getAddress().getRegion()));
            binding.etDescription.setText(event.getDescription());
            binding.switchOngoing.setChecked(event.isOngoing());

            currentEvent = event;

            repository.removeEventListener(eventId);
            }

            @Override
            public void onNoData() {
                info("Brak danych");
                quit();
            }

            @Override
            public void onFailed() {
                handleFail();
            }
        });

        binding.btnSaveEvent.setOnClickListener(v -> {
            Event newEvent = new Event();
            newEvent.setTitle(binding.spinnerThreat.getSelectedItem().toString());
            newEvent.setAddress(new Address(binding.etAddress.getText().toString(), binding.spinnerRegion.getSelectedItem().toString()));
            newEvent.setDescription(binding.etDescription.getText().toString());
            newEvent.setOngoing(binding.switchOngoing.isChecked());

            repository.updateEvent(currentEvent, newEvent, new UpdateListener() {
                @Override
                public void onSuccess() {
                    info("Zdarzenie zapisane");
                    quit();
                }

                @Override
                public void onFailed() {
                    info("Wystąpił błąd");
                }
            });
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

    private void quit() {
        Navigation.findNavController(binding.getRoot()).popBackStack();
    }


}