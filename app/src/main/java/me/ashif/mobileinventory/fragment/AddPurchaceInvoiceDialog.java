package me.ashif.mobileinventory.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.activity.PurchaseInvoiceActivity;
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.FragmentAddPurchaceInvoiceDialogBinding;
import me.ashif.mobileinventory.model.PurchaseInvoiceModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPurchaceInvoiceDialog extends DialogFragment implements TextWatcher, AdapterView.OnItemSelectedListener {

    private ApiInterface mApiInterface;
    private ProgressDialog pDialog;
    private FragmentAddPurchaceInvoiceDialogBinding mBinding;
    private ArrayList<String> mSupplierList;

    public AddPurchaceInvoiceDialog() {
        // Required empty public constructor
    }

    public static AddPurchaceInvoiceDialog newInstance(ArrayList<String> listdata) {
        AddPurchaceInvoiceDialog purchaceInvoiceDialog = new AddPurchaceInvoiceDialog();
        Bundle args = new Bundle();
        args.putSerializable("supplierList",listdata);
        purchaceInvoiceDialog.setArguments(args);
        return purchaceInvoiceDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setObjects();
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);
    }

    private void getItemsForSupplier(String supplierName) {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        Call<ResponseBody> itemsCall = mApiInterface.getItemsForSupplier(supplierName);
        itemsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismiss();
                JSONObject j = null;
                String json;
                InputStream inputStream = response.body().byteStream();
                try {
                    json = IOUtils.toString(inputStream,"UTF-8");
                    j = new JSONObject(json);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayList<String> listdata = new ArrayList<>();
                JSONArray jArray = null;
                try {
                    jArray = j.getJSONArray("items");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jArray != null) {
                    for (int i=0;i<jArray.length();i++){
                        try {
                            listdata.add(jArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                mBinding.spinnerItemName.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner,listdata));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setObjects() {
        pDialog = new ProgressDialog(getContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_add_purchace_invoice_dialog, null, false);
        alertDialogBuilder.setView(mBinding.getRoot());

        mSupplierList = (ArrayList<String>) getArguments().getSerializable("supplierList");
        Log.d("asasa", "onCreateDialog: "+mSupplierList.size());
        mBinding.spinnerSupplierName.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner,mSupplierList));
        getItemsForSupplier(mSupplierList.get(0));
        mBinding.textItemprice.addTextChangedListener(this);
        mBinding.textItemunit.addTextChangedListener(this);
        mBinding.spinnerSupplierName.setOnItemSelectedListener(this);

        final PurchaseInvoiceModel model = new PurchaseInvoiceModel();

        String spinnerItemName = "item name";
        String spinnerSupplierName = "supplier name";

        if (mBinding.spinnerItemName != null && mBinding.spinnerItemName.getSelectedItem()!= null && mBinding.spinnerSupplierName!= null && mBinding.spinnerSupplierName.getSelectedItem() != null){
            spinnerItemName = mBinding.spinnerItemName.getSelectedItem().toString();
            spinnerSupplierName = mBinding.spinnerSupplierName.getSelectedItem().toString();
        }

        final String finalSpinnerItemName = spinnerItemName;
        final String finalSpinnerSupplierName = spinnerSupplierName;
        alertDialogBuilder.setTitle("Add Purchase Details")
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isValidEntry()) {
                            pDialog.setMessage(getString(R.string.loading));
                            pDialog.show();
                            model.setItemName(finalSpinnerItemName);
                            model.setSupplierName(finalSpinnerSupplierName);
                            model.setPrice(Integer.parseInt(mBinding.textItemprice.getText().toString()));
                            model.setQuantity(Integer.parseInt(mBinding.textItemunit.getText().toString()));
                            model.setSupplierCommission(Float.parseFloat(mBinding.textItemcommision.getText().toString()));
                            model.setTotal(Float.parseFloat(mBinding.textSales.getText().toString()));
                            pDialog.setMessage(getString(R.string.loading));
                            postPurchaseInvoice(model);
                        }
                        else {
                            //display failed
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        return alertDialogBuilder.create();
    }

    private boolean isValidEntry() {
        if (mBinding.textItemcommision.getText().toString().isEmpty() || mBinding.textItemprice.getText().toString().isEmpty()
                || mBinding.textItemunit.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void postPurchaseInvoice(PurchaseInvoiceModel model) {

        Call<ResponseBody> postInvoiceCall = mApiInterface.setPurchaseInvoice(model.getItemName(),model.getSupplierName(),model.getSupplierCommission(),model.getQuantity(),model.getPrice(),model.getTotal());
        postInvoiceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismiss();
//                Toast.makeText(mContext ,getString(R.string.saved),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
//                Toast.makeText(getContext(),getString(R.string.failed),Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        getItemsForSupplier(adapterView.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
