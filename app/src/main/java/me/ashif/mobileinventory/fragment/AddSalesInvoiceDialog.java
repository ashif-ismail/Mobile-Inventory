package me.ashif.mobileinventory.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.FragmentAddSalesInvoiceDialogBinding;
import me.ashif.mobileinventory.model.SalesModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSalesInvoiceDialog extends DialogFragment implements TextWatcher, AdapterView.OnItemSelectedListener {

    private ApiInterface mApiInterface;
    private ProgressDialog pDialog;
    private FragmentAddSalesInvoiceDialogBinding mBinding;
    private ArrayList<String> mCustomersList;
    private SalesModel mSalesModel;

    public AddSalesInvoiceDialog() {
        // Required empty public constructor
    }

    public static AddSalesInvoiceDialog newInstance(ArrayList<String> listdata) {
        AddSalesInvoiceDialog salesInvoiceDialog = new AddSalesInvoiceDialog();
        Bundle args = new Bundle();
        args.putSerializable("customerList", listdata);
        salesInvoiceDialog.setArguments(args);
        return salesInvoiceDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setObjects();
    }

    private void setObjects() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        mSalesModel = new SalesModel();
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_add_sales_invoice_dialog, null, false);
        alertDialogBuilder.setView(mBinding.getRoot());

        mCustomersList = (ArrayList<String>) getArguments().getSerializable("customerList");
//        if (mCustomersList.size() != 0) {
        mBinding.spinnerCustomerName.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner, mCustomersList));
        //  }
        pDialog.dismiss();
        if (mCustomersList.size() != 0) {
            getItemsForCustomer(mCustomersList.get(0));
        }
        mBinding.spinnerCustomerName.setOnItemSelectedListener(this);
        mBinding.spinnerItemName.setOnItemSelectedListener(this);

        mBinding.textItemprice.addTextChangedListener(this);
        mBinding.textItemunit.addTextChangedListener(this);
        mBinding.textItemcommision.addTextChangedListener(this);

        String spinnerItemName = "item name";
        String spinnerCustomerName = "supplier name";

        if (mBinding.spinnerItemName != null && mBinding.spinnerItemName.getSelectedItem() != null && mBinding.spinnerCustomerName != null && mBinding.spinnerCustomerName.getSelectedItem() != null) {
            spinnerItemName = mBinding.spinnerItemName.getSelectedItem().toString();
            spinnerCustomerName = mBinding.spinnerCustomerName.getSelectedItem().toString();
        }

        final String finalSpinnerItemName = spinnerItemName;
        final String finalSpinnerCustomerName = spinnerCustomerName;

        mBinding.textItemprice.addTextChangedListener(this);
        mBinding.textItemunit.addTextChangedListener(this);

        alertDialogBuilder.setTitle("Update Sales Details")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isValidEntry()) {
                            updateSales();
                        } else {
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

    private void getItemsForCustomer(String customerName) {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        Call<ResponseBody> call = mApiInterface.getItemsForCustomer(customerName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject j = null;
                String json;
                InputStream inputStream = response.body().byteStream();
                try {
                    json = IOUtils.toString(inputStream, "UTF-8");
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
                    for (int i = 0; i < jArray.length(); i++) {
                        try {
                            listdata.add(jArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                mBinding.spinnerItemName.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner, listdata));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void updateSales() {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        Call<ResponseBody> updateInvoiceCall = mApiInterface.updateSales(mSalesModel.getId(), mBinding.textCustomerid.getText().toString(),
                mBinding.spinnerItemName.getSelectedItem().toString(),
                mBinding.spinnerCustomerName.getSelectedItem().toString(), Float.parseFloat(mBinding.textItemcommision.getText().toString()),
                Integer.parseInt(mBinding.textItemunit.getText().toString()), Integer.parseInt(mBinding.textItemprice.getText().toString()),
                Float.parseFloat(mBinding.textSales.getText().toString()));
        updateInvoiceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismiss();
                Log.d("asdad", "onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("asdad", "onResponse: " + t.getMessage());
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
        if (!mBinding.textItemunit.getText().toString().isEmpty() &&
                !mBinding.textItemprice.getText().toString().isEmpty() && !mBinding.textItemcommision.getText().toString().isEmpty()){
            float comm = Float.valueOf(mBinding.textItemcommision.getText().toString()) / 100;
            float totalAmount = Integer.valueOf(mBinding.textItemunit.getText().toString())*
                    Integer.valueOf(mBinding.textItemprice.getText().toString());
            float amountToDetect =totalAmount * comm;
            mBinding.textSales.setText(String.valueOf(totalAmount - amountToDetect));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinnerCustomerName:
                getItemsForCustomer(mBinding.spinnerCustomerName.getSelectedItem().toString());
                break;
            case R.id.spinnerItemName:
                getDetails(mBinding.spinnerCustomerName.getSelectedItem().toString(), mBinding.spinnerItemName.getSelectedItem().toString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getDetails(String customerName, String itemName) {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        Call<ArrayList<SalesModel>> call = mApiInterface.getSalesDetails(customerName, itemName);
        call.enqueue(new Callback<ArrayList<SalesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SalesModel>> call, Response<ArrayList<SalesModel>> response) {
                pDialog.dismiss();
                ArrayList<SalesModel> result = new ArrayList<>();
                result.addAll(response.body());
                mBinding.textItemcommision.setText(String.valueOf(result.get(0).getCommission()));
                mBinding.textItemprice.setText(String.valueOf(result.get(0).getPrice()));
                mBinding.textItemunit.setText(String.valueOf(result.get(0).getQuantity()));
                mBinding.textSales.setText(String.valueOf(result.get(0).getTotal()));
                mBinding.textCustomerid.setText(String.valueOf(result.get(0).getCustomerCode()));
                mSalesModel.setId(result.get(0).getId());
            }

            @Override
            public void onFailure(Call<ArrayList<SalesModel>> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
