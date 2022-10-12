package edu.wseiz.remizaosp.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.databinding.FragmentFirefighterListBinding;

public class FirefighterListFragment extends Fragment {


    private FragmentFirefighterListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirefighterListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}