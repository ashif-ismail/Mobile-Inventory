package me.ashif.mobileinventory.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.databinding.PurchaseInvoiceItemsBinding;
import me.ashif.mobileinventory.listener.OnDeleteClicked;
import me.ashif.mobileinventory.model.PurchaseModel;

/**
 * Created by Ashif on 11/1/17,January,2017
 * TechJini Solutions
 * Banglore,India
 */

public class PurchaseInvoiceAdapter extends RecyclerView.Adapter {

    private ArrayList<PurchaseModel> mPurchaseList;
    private Context mContext;
    private PurchaseInvoiceItemsBinding mBinding;
    private MenuInflater inflater;
    private android.view.ActionMode.Callback mCallback;
    private android.view.ActionMode mMode;
    private OnDeleteClicked mListener;


    public PurchaseInvoiceAdapter(Context mContext, ArrayList<PurchaseModel> mPurchaseList, MenuInflater inflater, OnDeleteClicked listener) {
        this.mContext = mContext;
        this.mPurchaseList = mPurchaseList;
        this.inflater = inflater;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.purchase_invoice_items, parent, false);
        return new PurchasInvoiceViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((PurchasInvoiceViewHolder) holder).binding.textItemrname.setText(mPurchaseList.get(position).getItemName());
        ((PurchasInvoiceViewHolder) holder).binding.textCommission.setText(String.valueOf(mPurchaseList.get(position).getCommission()));
        ((PurchasInvoiceViewHolder) holder).binding.textPurchaseamount.setText(String.valueOf(mPurchaseList.get(position).getTotal()));
        ((PurchasInvoiceViewHolder) holder).binding.textUnitprice.setText(String.valueOf(mPurchaseList.get(position).getPrice()));

        mCallback = new android.view.ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
                actionMode.setTitle("delete item");
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        Log.d("adapter", "onActionItemClicked: " + mPurchaseList.size());
                        mListener.deleteClicked(mPurchaseList.get(position).getId());
                        Log.e("adat", "onActionItemClicked: "+mPurchaseList.get(position).getId() );
                        mPurchaseList.remove(position);
                        notifyDataSetChanged();
                        actionMode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode actionMode) {
                mMode = null;
            }
        };
    }

    @Override
    public int getItemCount() {
        return mPurchaseList.size();
    }

    private class PurchasInvoiceViewHolder extends RecyclerView.ViewHolder {
        private PurchaseInvoiceItemsBinding binding;


        public PurchasInvoiceViewHolder(final PurchaseInvoiceItemsBinding mBinding) {
            super(mBinding.getRoot());
            this.binding = mBinding;

            mBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mMode != null)
                        return false;
                    else
                        mMode = view.startActionMode(mCallback);
                    return true;
                }
            });

        }

    }
}
