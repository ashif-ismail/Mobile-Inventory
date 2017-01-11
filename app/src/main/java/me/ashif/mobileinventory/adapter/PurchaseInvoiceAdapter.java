package me.ashif.mobileinventory.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.databinding.PurchaseInvoiceItemsBinding;
import me.ashif.mobileinventory.model.PurchaseModel;
import me.ashif.mobileinventory.model.SalesModel;

/**
 * Created by Ashif on 11/1/17,January,2017
 * TechJini Solutions
 * Banglore,India
 */

public class PurchaseInvoiceAdapter extends RecyclerView.Adapter {

    private ArrayList<PurchaseModel> mPurchaseList;
    private Context mContext;
    private PurchaseInvoiceItemsBinding mBinding;

    public PurchaseInvoiceAdapter(Context mContext, ArrayList<PurchaseModel> mPurchaseList) {
        this.mContext = mContext;
        this.mPurchaseList = mPurchaseList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.purchase_invoice_items, parent, false);
        return new PurchasInvoiceViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PurchasInvoiceViewHolder) holder).binding.textItemrname.setText(mPurchaseList.get(position).getItemName());
        ((PurchasInvoiceViewHolder) holder).binding.textCommission.setText(String.valueOf(mPurchaseList.get(position).getCommission()));
        ((PurchasInvoiceViewHolder) holder).binding.textPurchaseamount.setText(String.valueOf(mPurchaseList.get(position).getTotal()));
        ((PurchasInvoiceViewHolder) holder).binding.textSerialno.setText(String.valueOf(mPurchaseList.get(position).getId()));
        ((PurchasInvoiceViewHolder) holder).binding.textUnitprice.setText(String.valueOf(mPurchaseList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return mPurchaseList.size();
    }

    private class PurchasInvoiceViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private PurchaseInvoiceItemsBinding binding;

        public PurchasInvoiceViewHolder(final PurchaseInvoiceItemsBinding mBinding) {
            super(mBinding.getRoot());
            this.binding = mBinding;

            binding.cardViewThird.setOnLongClickListener(this);
            binding.commissionWrapper.setOnLongClickListener(this);
            binding.itemnameWrapper.setOnLongClickListener(this);
            binding.purchaseamountWrapper.setOnLongClickListener(this);
            binding.serialnoWrapper.setOnLongClickListener(this);

            binding.textUnitprice.setOnLongClickListener(this);
            binding.textSerialno.setOnLongClickListener(this);
            binding.textPurchaseamount.setOnLongClickListener(this);
            binding.textCommission.setOnLongClickListener(this);
            binding.textItemrname.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(mContext, "clikced", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
