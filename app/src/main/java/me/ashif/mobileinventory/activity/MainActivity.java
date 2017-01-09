package me.ashif.mobileinventory.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setListeners();
    }

    private void setListeners() {
        mBinding.cardViewFirst.setOnClickListener(this);
        mBinding.cardViewSecond.setOnClickListener(this);
        mBinding.cardViewThird.setOnClickListener(this);
        mBinding.cardViewSales.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_view_first: {
                Intent purchaseEntry = new Intent(MainActivity.this,EditPurchaseEntryActivity.class);
                startActivity(purchaseEntry);
            }
            break;
            case R.id.card_view_second: {
                Intent purchaseInvoice = new Intent(MainActivity.this, PurchaseInvoiceActivity.class);
                startActivity(purchaseInvoice);
            }
            break;
            case R.id.card_view_third: {
                Intent salesInvoice = new Intent(MainActivity.this, SalesInvoiceActivity.class);
                startActivity(salesInvoice);
            }
            break;
            case R.id.card_view_sales:{
                Intent salesEntry = new Intent(MainActivity.this,EditSalesEntryActivity.class);
                startActivity(salesEntry);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
