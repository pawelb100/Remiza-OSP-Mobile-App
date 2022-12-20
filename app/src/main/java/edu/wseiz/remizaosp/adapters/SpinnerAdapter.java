package edu.wseiz.remizaosp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.wseiz.remizaosp.R;

public class SpinnerAdapter extends ArrayAdapter<String> {

    public SpinnerAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, objects);
    }

    public SpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_item, parent, false
            );
        }
        TextView tv = convertView.findViewById(R.id.tv_item);
        String currentItem = getItem(position);

        if (currentItem!=null)
            tv.setText(currentItem);

        return convertView;
    }
}
