package me.ashif.mobileinventory.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.FragmentAddPurchaceInvoiceDialogBinding;
import me.ashif.mobileinventory.databinding.FragmentAddSalesInvoiceDialogBinding;
import me.ashif.mobileinventory.model.SalesInvoiceModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSalesInvoiceDialog extends DialogFragment implements TextWatcher {

    private ApiInterface mApiInterface;
    private ProgressDialog pDialog;
    private FragmentAddSalesInvoiceDialogBinding mBinding;

    public AddSalesInvoiceDialog() {
        // Required empty public constructor
    }

    public static AddSalesInvoiceDialog newInstance() {
        AddSalesInvoiceDialog salesInvoiceDialog = new AddSalesInvoiceDialog();
        return salesInvoiceDialog;
    }
    private void setObjects() {
        pDialog = new ProgressDialog(getContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        setObjects();
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_add_sales_invoice_dialog, null, false);
        alertDialogBuilder.setView(mBinding.getRoot());

        final SalesInvoiceModel model = new SalesInvoiceModel();

        String spinnerItemName = "item name";
        String spinnerCustomerName = "supplier name";

        if (mBinding.spinnerItemName != null && mBinding.spinnerItemName.getSelectedItem()!= null && mBinding.spinnerCustomerName!= null && mBinding.spinnerCustomerName.getSelectedItem() != null){
            spinnerItemName = mBinding.spinnerItemName.getSelectedItem().toString();
            spinnerCustomerName = mBinding.spinnerCustomerName.getSelectedItem().toString();
        }

        final String finalSpinnerItemName = spinnerItemName;
        final String finalSpinnerCustomerName = spinnerCustomerName;

        mBinding.textItemprice.addTextChangedListener(this);
        mBinding.textItemunit.addTextChangedListener(this);

        alertDialogBuilder.setTitle("Add Sales Details")
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isValidEntry()) {
                            pDialog.setMessage(getString(R.string.loading));
                            pDialog.show();
                            model.setItemName(finalSpinnerItemName);
                            model.setCustomerName(finalSpinnerCustomerName);
                            model.setPrice(Integer.parseInt(mBinding.textItemprice.getText().toString()));
                            model.setQuantity(Integer.parseInt(mBinding.textItemunit.getText().toString()));
                            model.setCustomerCommission(Float.parseFloat(mBinding.textItemcommision.getText().toString()));
                            model.setTotal(Float.parseFloat(mBinding.textSales.getText().toString()));
                            pDialog.setMessage(getString(R.string.loading));
                            postSalesInvoice(model);
                        }
                        else {
                            //display failed
                        }                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        return  alertDialogBuilder.create();
    }

    private void postSalesInvoice(SalesInvoiceModel model) {
        Call<ResponseBody> postInvoiceCall = mApiInterface.setSalesInvoice(model.getItemName(),model.getCustomerName(),model.getCustomerCommission(),model.getQuantity(),model.getPrice(),model.getTotal());
        postInvoiceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private boolean isValidEntry() {
        if (mBinding.textItemcommision.getText().toString().isEmpty() || mBinding.textItemprice.getText().toString().isEmpty()
                || mBinding.textItemunit.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!mBinding.textItemprice.getText().toString().isEmpty() && !
                mBinding.textItemunit.getText().toString().isEmpty()) {
            int totalAmount = Integer.valueOf(mBinding.textItemunit.getText().toString()) *
                    Integer.valueOf(mBinding.textItemprice.getText().toString());
            mBinding.textSales.setText(String.valueOf(totalAmount));
        }
    }
}
