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
import me.ashif.mobileinventory.databinding.FragmentAddPurchaceInvoiceDialogBinding;
import me.ashif.mobileinventory.model.PurchaseModel;
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
    private PurchaseModel mPurchaseModel;

    public AddPurchaceInvoiceDialog() {
        // Required empty public constructor
    }

    public static AddPurchaceInvoiceDialog newInstance(ArrayList<String> listdata) {
        AddPurchaceInvoiceDialog purchaceInvoiceDialog = new AddPurchaceInvoiceDialog();
        Bundle args = new Bundle();
        args.putSerializable("supplierList", listdata);
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

    private void setObjects() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        mPurchaseModel = new PurchaseModel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_add_purchace_invoice_dialog, null, false);
        alertDialogBuilder.setView(mBinding.getRoot());

        mSupplierList = (ArrayList<String>) getArguments().getSerializable("supplierList");
        //  if (mSupplierList.size() != 0) {
        mBinding.spinnerSupplierName.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner, mSupplierList));
        //  }
        pDialog.dismiss();
        if (mSupplierList.size() != 0) {
            getItemsForSupplier(mSupplierList.get(0));
        }
        mBinding.textItemprice.addTextChangedListener(this);
        mBinding.textItemunit.addTextChangedListener(this);

        mBinding.spinnerSupplierName.setOnItemSelectedListener(this);
        mBinding.spinnerItemName.setOnItemSelectedListener(this);

        final PurchaseModel model = new PurchaseModel();

//        if (mBinding.spinnerItemName != null && mBinding.spinnerItemName.getSelectedItem()!= null && mBinding.spinnerSupplierName!= null && mBinding.spinnerSupplierName.getSelectedItem() != null){
//            String spinnerItemName = mBinding.spinnerItemName.getSelectedItem().toString();
//            String spinnerSupplierName = mBinding.spinnerSupplierName.getSelectedItem().toString();
//        }

        alertDialogBuilder.setTitle("Update Purchase Invoice")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isValidEntry()) {
                            pDialog.setMessage(getString(R.string.loading));
                            pDialog.show();
                            updatePurchase();
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

    private boolean isValidEntry() {
        if (mBinding.textItemcommision.getText().toString().isEmpty() || mBinding.textItemprice.getText().toString().isEmpty()
                || mBinding.textItemunit.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void updatePurchase() {
        Call<ResponseBody> updatePurchaseCall = mApiInterface.updatePurchase(mPurchaseModel.getId(), mBinding.textSupppliercode.getText().toString(),
                mBinding.spinnerItemName.getSelectedItem().toString()
                , mBinding.spinnerSupplierName.getSelectedItem().toString(), Float.parseFloat(mBinding.textItemcommision.getText().toString()),
                Integer.parseInt(mBinding.textItemunit.getText().toString()), Integer.parseInt(mBinding.textItemprice.getText().toString()),
                Float.parseFloat(mBinding.textSales.getText().toString()));
        updatePurchaseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismiss();
//                Toast.makeText(mContext ,getString(R.string.saved),Toast.LENGTH_SHORT).show();
                Log.d("asas", "onResponse: " + response.message());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
//                Toast.makeText(getContext(),getString(R.string.failed),Toast.LENGTH_SHORT).show();
                Log.d("asas", "onResponse: " + t.getMessage());
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
            case R.id.spinnerSupplierName:
                getItemsForSupplier(mBinding.spinnerSupplierName.getSelectedItem().toString());
                break;
            case R.id.spinnerItemName:
                getDetails(mBinding.spinnerSupplierName.getSelectedItem().toString(), mBinding.spinnerItemName.getSelectedItem().toString());
                break;
        }
    }

    private void getDetails(String supplier, String item) {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        Call<ArrayList<PurchaseModel>> call = mApiInterface.getPurchaseDetails(supplier, item);
        call.enqueue(new Callback<ArrayList<PurchaseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PurchaseModel>> call, Response<ArrayList<PurchaseModel>> response) {
                pDialog.dismiss();
                ArrayList<PurchaseModel> result = new ArrayList<>();
                result.addAll(response.body());
                mBinding.textItemcommision.setText(String.valueOf(result.get(0).getCommission()));
                mBinding.textItemprice.setText(String.valueOf(result.get(0).getPrice()));
                mBinding.textItemunit.setText(String.valueOf(result.get(0).getQuantity()));
                mBinding.textSales.setText(String.valueOf(result.get(0).getTotal()));
                mBinding.textSupppliercode.setText(String.valueOf(result.get(0).getSupplierCode()));
                mPurchaseModel.setId(Integer.valueOf(result.get(0).getId()));
            }

            @Override
            public void onFailure(Call<ArrayList<PurchaseModel>> call, Throwable t) {
                pDialog.dismiss();
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
