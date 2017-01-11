package me.ashif.mobileinventory.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.databinding.SalesInvoiceItemsBinding;
import me.ashif.mobileinventory.model.SalesModel;

/**
 * Created by Ashif on 11/1/17,January,2017
 * TechJini Solutions
 * Banglore,India
 */

public class SalesInvoiceAdapter extends RecyclerView.Adapter {

    private ArrayList<SalesModel> mSalesList;
    private Context mContext;
    private SalesInvoiceItemsBinding mBinding;

    public SalesInvoiceAdapter(Context mContext, ArrayList<SalesModel> mSalesList) {
        this.mContext = mContext;
        this.mSalesList = mSalesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sales_invoice_items, parent, false);
        return new SalesInvoiceViewHolder(mBinding);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SalesInvoiceViewHolder) holder).binding.textItemrname.setText(mSalesList.get(position).getItemName());
        ((SalesInvoiceViewHolder) holder).binding.textCommission.setText(String.valueOf(mSalesList.get(position).getCommission()));
        ((SalesInvoiceViewHolder) holder).binding.textSalesamount.setText(String.valueOf(mSalesList.get(position).getTotal()));
        ((SalesInvoiceViewHolder) holder).binding.textSerialno.setText(String.valueOf(mSalesList.get(position).getId()));
        ((SalesInvoiceViewHolder) holder).binding.textUnitprice.setText(String.valueOf(mSalesList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return mSalesList.size();
    }

    private class SalesInvoiceViewHolder extends RecyclerView.ViewHolder {
        private SalesInvoiceItemsBinding binding;
        public SalesInvoiceViewHolder(SalesInvoiceItemsBinding mBinding) {
            super(mBinding.getRoot());
            this.binding = mBinding;
        }
    }
}
