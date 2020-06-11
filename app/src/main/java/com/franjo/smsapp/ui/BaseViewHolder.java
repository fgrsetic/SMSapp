package com.franjo.smsapp.ui;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.BR;
import com.franjo.smsapp.domain.Message;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    // ViewDataBinding, the base class for all generated bindings, instead of the specific ItemBinding
    private final ViewDataBinding binding;

    public BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        // BR.obj as the key in setVariable() layout tag "obj"
        binding.setVariable(BR.obj, obj);
        // This is important, because it forces the data binding to execute immediately,
        // which allows the RecyclerView to make the correct view size measurements
        binding.executePendingBindings();
    }

    public void bind(Object obj, Object obj2) {
        // BR.obj as the key in setVariable() layout tag "obj"
        binding.setVariable(BR.obj, obj);
        binding.setVariable(BR.obj2, obj2);
        // This is important, because it forces the data binding to execute immediately,
        // which allows the RecyclerView to make the correct view size measurements
        binding.executePendingBindings();
    }

    public void bind(Object obj, OnItemClickListener listener) {
        binding.setVariable(BR.obj, obj);
        binding.setVariable(BR.listener, listener);
        binding.executePendingBindings();
    }
}
