package me.ashif.mobileinventory.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.databinding.ActivityPurchaseInvoiceBinding;
import me.ashif.mobileinventory.fragment.AddPurchaceInvoiceDialog;

public class PurchaseInvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityPurchaseInvoiceBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_purchase_invoice);

        setListeners();
    }

    private void setListeners() {
        mBinding.fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
            {
                FragmentManager fm = getSupportFragmentManager();
                AddPurchaceInvoiceDialog purchaceInvoiceDialog = AddPurchaceInvoiceDialog.newInstance();
                purchaceInvoiceDialog.show(fm, "fragment_add_purchase");
            }
        }
    }
}
