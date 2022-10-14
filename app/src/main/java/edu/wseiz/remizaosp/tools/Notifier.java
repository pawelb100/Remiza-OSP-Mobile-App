package edu.wseiz.remizaosp.tools;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class Notifier {

    private Activity activity;

    public Notifier(Activity activity)
    {
        this.activity = activity;
    }

    public void info(String message)
    {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public void alert(String message)
    {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public void messageBox(String title, String message, String buttonOk)
    {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(buttonOk, null)
                .show();
    }

    public void dialogBox(String title, String message, String buttonPositive, String buttonNegative, Boolean isCancellable, int icon, DialogInterface.OnClickListener actionPositive, DialogInterface.OnClickListener actionNegative)
    {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonPositive, actionPositive)
                .setNegativeButton(buttonNegative, actionNegative)
                .setCancelable(isCancellable)
                .setIcon(icon)
                .show();
    }

    /*
    notifier.dialogBox("mytitle", "mymessage", "Yes", "No", false, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
     */

    public void inputTextBox(String title, String message, String buttonPositive, String buttonNegative, Boolean isCancellable, int icon, EditText editText, DialogInterface.OnClickListener actionPositive, DialogInterface.OnClickListener actionNegative)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton(buttonPositive, actionPositive);
        if (isCancellable)
        {
            alert.setNegativeButton(buttonNegative, actionNegative);
            alert.setCancelable(isCancellable);
        }
        alert.setIcon(icon);
        alert.setView(editText);
        alert.show();


    }

    /*
    final EditText inputTextBox = new EditText(this);
        notifier.inputTextBox("mytitle", "mymessage", "Yes", "No", true, 0, inputTextBox, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = inputTextBox.getText().toString();
                    }
                },
               null);
     */

    public void selectListItemDialog(String title, String[] array, DialogInterface.OnClickListener action ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setItems(array, action);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
