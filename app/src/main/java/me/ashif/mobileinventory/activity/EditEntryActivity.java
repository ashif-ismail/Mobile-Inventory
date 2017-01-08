package me.ashif.mobileinventory.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.databinding.ActivityEditEntryBinding;

public class EditEntryActivity extends AppCompatActivity {

    private ActivityEditEntryBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_entry);

        setTextChangeListenerForUnitPrice();
    }

    private void setTextChangeListenerForUnitPrice() {
        mBinding.textUnitprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mBinding.textUnitprice.getText().toString().isEmpty()){
                    int totalAmount = Integer.valueOf(mBinding.textItemq.getText().toString())*
                            Integer.valueOf(mBinding.textUnitprice.getText().toString());
                    mBinding.textTotal.setText(String.valueOf(totalAmount));
                }
            }
        });
    }
}
