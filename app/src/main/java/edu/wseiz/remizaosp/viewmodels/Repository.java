package edu.wseiz.remizaosp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import edu.wseiz.remizaosp.tools.Database;

public class Repository extends AndroidViewModel {


    private final Database database;


    public Repository(@NonNull Application application) {
        super(application);
        database = new Database();
    }

    public Database getDatabase() {
        return this.database;
    }




}
