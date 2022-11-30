package edu.wseiz.remizaosp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.adapters.StatusListAdapter;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.models.Status;

public class DialogStatusSelect {

    private final Dialog dialog;
    private StatusListAdapter adapter;

    public DialogStatusSelect(Context context, List<Status> statusList, String currentStatusId, OnItemListClick listener) {
        this.dialog = new Dialog(context);
        this.adapter = new StatusListAdapter(context, statusList, currentStatusId, listener);
    }

    public DialogStatusSelect(Context context) {
        this.dialog = new Dialog(context);
    }


    public void setAdapter(List<Status> statusList, String currentStatusId, OnItemListClick listener) {
        this.adapter = new StatusListAdapter(dialog.getContext(), statusList, currentStatusId, listener);
    }

    public void show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_status_select);

        RecyclerView rvStatus = dialog.findViewById(R.id.rvStatuses);

        rvStatus.setAdapter(adapter);
        rvStatus.setLayoutManager(new LinearLayoutManager(this.dialog.getContext()));

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

    }

    public void dismiss() {
        dialog.dismiss();
    }

}
