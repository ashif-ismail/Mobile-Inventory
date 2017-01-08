package me.ashif.mobileinventory.activity;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.databinding.ActivitySalesInvoiceBinding;
import me.ashif.mobileinventory.fragment.AddPurchaceInvoiceDialog;
import me.ashif.mobileinventory.fragment.AddSalesInvoiceDialog;

public class SalesInvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySalesInvoiceBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_sales_invoice);

        setListeners();
    }

    private void setListeners() {
        mBinding.fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                FragmentManager fm = getSupportFragmentManager();
                AddSalesInvoiceDialog salesInvoiceDialog = AddSalesInvoiceDialog.newInstance();
                salesInvoiceDialog.show(fm, "fragment_add_sales");
        }
    }
}
