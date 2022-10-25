package edu.wseiz.remizaosp.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wseiz.remizaosp.MainActivity;
import edu.wseiz.remizaosp.databinding.FragmentFirefighterListBinding;
import edu.wseiz.remizaosp.tools.Database;
import edu.wseiz.remizaosp.tools.DatabaseListener;
import edu.wseiz.remizaosp.tools.Notifier;

public class FirefighterListFragment extends Fragment {


    private FragmentFirefighterListBinding binding;
    private Notifier notifier;
    private Database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirefighterListBinding.inflate(inflater, container, false);

        notifier = ((MainActivity) requireActivity()).getNotifier();
        database = ((MainActivity) requireActivity()).getDatabase();


        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}