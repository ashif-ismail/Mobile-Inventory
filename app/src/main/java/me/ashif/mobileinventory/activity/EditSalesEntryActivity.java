package me.ashif.mobileinventory.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import me.ashif.mobileinventory.databinding.ActivityEditSalesEntryBinding;
import me.ashif.mobileinventory.model.SalesModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSalesEntryActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private ActivityEditSalesEntryBinding mBinding;
    private ProgressDialog pDialog;
    private ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_sales_entry);

        setObjects();
        setListeners();
        getCustomersList();
        getCustomerCodes();

    }

    private void getCustomerCodes() {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();

        Call<ResponseBody> call = mApiInterface.getAllCustomerCode();
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
                    jArray = j.getJSONArray("entityCode");
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

                mBinding.autocompletesuppliercode.setThreshold(1);
                mBinding.autocompletesuppliercode.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(),getString(R.string.failed),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCustomersList() {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();

        Call<ResponseBody> call = mApiInterface.getAllCustomers();
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
                    jArray = j.getJSONArray("customername");
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
                Toast.makeText(getApplicationContext(),getString(R.string.failed),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setObjects() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);
    }

    private void setListeners() {
        mBinding.buttonSave.setOnClickListener(this);
        mBinding.textItemq.addTextChangedListener(this);
        mBinding.textUnitprice.addTextChangedListener(this);
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
        switch (view.getId()) {
            case R.id.button_save:
                if (isValidEntry()) {
                    showProgress(getString(R.string.loading));
                    SalesModel model = new SalesModel();
                    model.setItemName(mBinding.textItemname.getText().toString());
                    model.setCustomerName(mBinding.textItemsuppliername.getText().toString());
                    model.setCommission(Float.parseFloat(mBinding.textSuppliercomm.getText().toString()));
                    model.setPrice(Integer.parseInt(mBinding.textUnitprice.getText().toString()));
                    model.setQuantity(Integer.parseInt(mBinding.textItemq.getText().toString()));
                    model.setTotal(Float.parseFloat(mBinding.textTotal.getText().toString()));
                    model.setCustomerCode(mBinding.autocompletesuppliercode.getText().toString());
                    postSalesDetails(model);
                } else
                    displayToast(getString(R.string.fieldsmissing));
                break;
        }
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void showProgress(String msg) {
        pDialog.setMessage(msg);
        pDialog.setIndeterminate(true);
        pDialog.show();
    }

    private void postSalesDetails(SalesModel model) {
        Call<ResponseBody> saveCall = mApiInterface.setSales(String.valueOf(model.getCustomerCode()),model.getItemName(), model.getCustomerName(), model.getCommission(), model.getQuantity(), model.getPrice(), model.getTotal());
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

    private boolean isValidEntry() {
        if (mBinding.textItemname.getText().toString().isEmpty() || mBinding.textItemsuppliername.getText().toString().isEmpty()
                || mBinding.textSuppliercomm.getText().toString().isEmpty() || mBinding.textItemq.getText().toString().isEmpty()
                || mBinding.textUnitprice.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }
}
