package com.franjo.smsapp.ui.messages.conversations_details;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private int threadId;

    DetailsViewModelFactory(int threadId) {
        this.threadId = threadId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(threadId);
    }
}
