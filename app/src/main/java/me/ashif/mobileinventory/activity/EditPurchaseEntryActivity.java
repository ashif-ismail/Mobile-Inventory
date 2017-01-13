package me.ashif.mobileinventory.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.databinding.tool.MergedBinding;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.ActivityEditEntryBinding;
import me.ashif.mobileinventory.model.PurchaseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class EditPurchaseEntryActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private ActivityEditEntryBinding mBinding;
    private ApiInterface mApiInterface;
    private static String TAG = EditPurchaseEntryActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_entry);

        setObjects();
        setListeners();
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        getAllSuppliers();
        getAllSupplierCodes();

    }

    private void getAllSupplierCodes() {
        Call<ResponseBody> call = mApiInterface.getAllSupplierCode();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.setMessage(getString(R.string.loading));
                pDialog.show();

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

                ArrayList<String> codes = new ArrayList<>();
                JSONArray jArray = null;
                try {
                    jArray = j.getJSONArray("entityCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        try {
                            codes.add(jArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Set<String> hs = new HashSet<>();
                hs.addAll(codes);
                codes.clear();
                codes.addAll(hs);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getBaseContext(), android.R.layout.simple_spinner_dropdown_item, codes);

                mBinding.autocompletesuppliercode.setThreshold(1);
                mBinding.autocompletesuppliercode.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.failed),LENGTH_SHORT).show();
            }
        });
    }

    private void getAllSuppliers() {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        Call<ResponseBody> call = mApiInterface.getAllSuppliers();
        call.enqueue(new Callback<ResponseBody>() {
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
                    jArray = j.getJSONArray("suppliername");
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
                Set<String> hs = new HashSet<>();
                hs.addAll(listdata);
                listdata.clear();
                listdata.addAll(hs);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getBaseContext(), android.R.layout.simple_spinner_dropdown_item, listdata);

                mBinding.textItemsuppliername.setThreshold(1);
                mBinding.textItemsuppliername.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.failed),LENGTH_SHORT).show();
            }
        });
    }

    private void setObjects() {
        pDialog = new ProgressDialog(this);
    }

    private void setListeners() {

        mBinding.textItemq.addTextChangedListener(this);
        mBinding.textUnitprice.addTextChangedListener(this);
        mBinding.buttonSave.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!mBinding.textUnitprice.getText().toString().isEmpty() &&
                !mBinding.textItemq.getText().toString().isEmpty() && !mBinding.textItemq.getText().toString().isEmpty()){
            float comm = Float.valueOf(mBinding.textSuppliercomm.getText().toString()) / 100;
            float totalAmount = Integer.valueOf(mBinding.textItemq.getText().toString())*
                    Integer.valueOf(mBinding.textUnitprice.getText().toString());
            float amountToDetect =totalAmount * comm;
            mBinding.textTotal.setText(String.valueOf(totalAmount - amountToDetect));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save:
                if (isValidEntry()) {
                    showProgress(getString(R.string.loading));
                    PurchaseModel model = new PurchaseModel();
                    model.setSupplierCode(mBinding.autocompletesuppliercode.getText().toString());
                    model.setItemName(mBinding.textItemname.getText().toString());
                    model.setSupplierName(mBinding.textItemsuppliername.getText().toString());
                    model.setCommission(Float.parseFloat(mBinding.textSuppliercomm.getText().toString()));
                    model.setPrice(Integer.parseInt(mBinding.textUnitprice.getText().toString()));
                    model.setQuantity(Integer.parseInt(mBinding.textItemq.getText().toString()));
                    model.setTotal(Float.parseFloat(mBinding.textTotal.getText().toString()));
                    postPurchaseDetails(model);
                }
                else
                displayToast(getString(R.string.fieldsmissing));
                break;
        }
    }

    private void showProgress(String msg) {
        pDialog.setMessage(msg);
        pDialog.setIndeterminate(true);
        pDialog.show();
    }

    private boolean isValidEntry() {
        if (mBinding.autocompletesuppliercode.getText().toString().isEmpty() || mBinding.textItemname.getText().toString().isEmpty() || mBinding.textItemsuppliername.getText().toString().isEmpty()
                || mBinding.textSuppliercomm.getText().toString().isEmpty() || mBinding.textItemq.getText().toString().isEmpty()
                || mBinding.textUnitprice.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(),msg, LENGTH_SHORT).show();
    }

    private void postPurchaseDetails(final PurchaseModel model) {
        Call<ResponseBody> saveCall = mApiInterface.setPurchase(String.valueOf(model.getSupplierCode()),model.getItemName(),model.getSupplierName(),model.getCommission(),model.getQuantity(),model.getPrice(),model.getTotal());
        saveCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismiss();
                displayToast(getString(R.string.saved));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                displayToast(getString(R.string.failed));
            }
        });
    }
}
