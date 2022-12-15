package edu.wseiz.remizaosp.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import edu.wseiz.remizaosp.adapters.EventListAdapter;
import edu.wseiz.remizaosp.databinding.FragmentEventsBinding;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.listeners.FetchEventsListener;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.viewmodels.Repository;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;

    private Repository repository;

    private EventListAdapter adapter;
    private List<Event> events;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        repository.fetchEvents(new FetchEventsListener() {
            @Override
            public void onSuccess(List<Event> data) {
                events = data;
                setAdapter();
            }

            @Override
            public void onFailed() {

            }
        });


        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository.removeEventsListener();
        binding = null;
    }


    private void setAdapter() {

        if (adapter == null) {
            adapter = new EventListAdapter(getContext(), events, new OnItemListClick() {
                @Override
                public void onItemClick(Object item) {

                }

                @Override
                public void onItemLongClick(Object item, View view) {

                }
            });
            binding.rvEvents.setAdapter(adapter);
            binding.rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else
            adapter.updateData(events);

    }
}