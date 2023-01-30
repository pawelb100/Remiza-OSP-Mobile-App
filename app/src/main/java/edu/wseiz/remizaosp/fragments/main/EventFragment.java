package edu.wseiz.remizaosp.fragments.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.adapters.UsersListAdapter;
import edu.wseiz.remizaosp.databinding.FragmentEventBinding;
import edu.wseiz.remizaosp.listeners.FetchEventListener;
import edu.wseiz.remizaosp.listeners.FetchUsersByParticipationsListener;
import edu.wseiz.remizaosp.listeners.UpdateListener;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.User;
import edu.wseiz.remizaosp.viewmodels.Repository;


public class EventFragment extends Fragment {

    private FragmentEventBinding binding;
    private Repository repository;

    private String eventId;

    private UsersListAdapter acceptedAdapter;
    private UsersListAdapter rejectedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        boolean editable = false;
        acceptedAdapter = null;
        rejectedAdapter = null;

        if (getArguments()!=null) {
            eventId = getArguments().getString("eventId");
            editable = getArguments().getBoolean("editable");
        }

        binding.btnShowOnMap.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+binding.tvAddress.getText().toString()+", "+binding.tvRegion.getText().toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        if (editable) {
            binding.editFab.setVisibility(View.VISIBLE);
            binding.editFab.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("eventId", eventId);
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_eventFragment_to_editEventFragment, bundle);
            });
            binding.deleteFab.setVisibility(View.VISIBLE);
            binding.deleteFab.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                    .setTitle("Usunąć to zdarzenie?")
                    .setMessage("Jesteś pewien? Tego nie można cofnąć.")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        repository.removeEventListener(eventId);
                        repository.deleteEvent(eventId, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                info("Usunięto");
                                quit();
                            }

                            @Override
                            public void onFailed() {
                                handleFail();
                            }
                        });
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show());
        }

        repository.fetchEventById(eventId, new FetchEventListener() {
            @Override
            public void onSuccess(Event event) {
                binding.tvThreatType.setText(event.getTitle());

                if (event.isOngoing())
                    binding.ivDangerIcon.setBackgroundResource(R.drawable.ic_baseline_warning_amber_24);
                else
                    binding.ivDangerIcon.setBackgroundResource(R.drawable.ic_baseline_calendar_today_24);

                if(!(event.getDescription().isEmpty()))
                    binding.tvDescription.setText(event.getDescription());
                else
                    binding.tvDescription.setVisibility(View.GONE);

                if (event.isOngoing())
                    binding.tvOngoing.setText("Zdarzenie trwające");
                else
                    binding.tvOngoing.setText("Zdarzenie zakończone");

                binding.tvAddress.setText(event.getAddress().getStreet());
                binding.tvRegion.setText(event.getAddress().getRegion());

                repository.fetchUsersByParticipations(event.getParticipationList(), new FetchUsersByParticipationsListener() {
                    @Override
                    public void onSuccess(List<User> accepted, List<User> rejected) {

                        if (acceptedAdapter==null) {
                            acceptedAdapter = new UsersListAdapter(getContext(), accepted);
                            binding.rcAccepted.setAdapter(acceptedAdapter);
                            binding.rcAccepted.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                        else
                            acceptedAdapter.updateData(accepted);

                        if (rejectedAdapter==null) {
                            rejectedAdapter = new UsersListAdapter(getContext(), rejected);
                            binding.rcRejected.setAdapter(rejectedAdapter);
                            binding.rcRejected.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                        else
                            rejectedAdapter.updateData(rejected);
                    }

                    @Override
                    public void onFailed() {
                        handleFail();
                    }
                });




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

        return binding.getRoot();
    }

    @Override public void onDestroyView () {
        super.onDestroyView();
        repository.removeEventListener(eventId);
        repository.removeUsersListener();
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