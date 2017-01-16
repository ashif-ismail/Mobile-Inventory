package me.ashif.mobileinventory.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.databinding.SalesInvoiceItemsBinding;
import me.ashif.mobileinventory.listener.OnDeleteClicked;
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
    private MenuInflater inflater;
    private android.view.ActionMode.Callback mCallback;
    private android.view.ActionMode mMode;
    private OnDeleteClicked mListener;

    public SalesInvoiceAdapter(Context mContext, ArrayList<SalesModel> mSalesList,MenuInflater inflater,OnDeleteClicked mListener) {
        this.mContext = mContext;
        this.mSalesList = mSalesList;
        this.inflater = inflater;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sales_invoice_items, parent, false);
        return new SalesInvoiceViewHolder(mBinding);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((SalesInvoiceViewHolder) holder).binding.textItemrname.setText(mSalesList.get(position).getItemName());
        ((SalesInvoiceViewHolder) holder).binding.textCommission.setText(String.valueOf(mSalesList.get(position).getCommission()));
        ((SalesInvoiceViewHolder) holder).binding.textSalesamount.setText(String.valueOf(mSalesList.get(position).getTotal()));
        ((SalesInvoiceViewHolder) holder).binding.textUnitprice.setText(String.valueOf(mSalesList.get(position).getPrice()));

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
                        mListener.deleteClicked(mSalesList.get(position).getId());
                        mSalesList.remove(position);
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
        return mSalesList.size();
    }

    private class SalesInvoiceViewHolder extends RecyclerView.ViewHolder {
        private SalesInvoiceItemsBinding binding;
        public SalesInvoiceViewHolder(SalesInvoiceItemsBinding mBinding) {
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
