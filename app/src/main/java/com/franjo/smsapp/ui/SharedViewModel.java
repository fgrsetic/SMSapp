package com.franjo.smsapp.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.franjo.smsapp.data.SmsData;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<SmsData> selected = new MutableLiveData<>();

    public void select(SmsData smsData) {
        selected.setValue(smsData);
    }

    public LiveData<SmsData> getSelected() {
        return selected;
    }
}
