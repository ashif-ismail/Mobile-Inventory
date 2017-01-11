package me.ashif.mobileinventory.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import java.util.HashSet;
import java.util.Set;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.adapter.PurchaseInvoiceAdapter;
import me.ashif.mobileinventory.adapter.SalesInvoiceAdapter;
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.ActivitySalesInvoiceBinding;
import me.ashif.mobileinventory.fragment.AddPurchaceInvoiceDialog;
import me.ashif.mobileinventory.fragment.AddSalesInvoiceDialog;
import me.ashif.mobileinventory.model.PurchaseModel;
import me.ashif.mobileinventory.model.SalesModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesInvoiceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ActivitySalesInvoiceBinding mBinding;
    private ApiInterface mApiInterface;
    private ProgressDialog pDialog;
    private ArrayList<String> listdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_sales_invoice);

        setListeners();
        setObjects();
        getCustomersList();
    }

    private void setObjects() {
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
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
                    json = IOUtils.toString(inputStream,"UTF-8");
                    j = new JSONObject(json);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listdata = new ArrayList<>();
                JSONArray jArray = null;
                try {
                    jArray = j.getJSONArray("customername");
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
//                Set<String> hs = new HashSet<>();
//                hs.addAll(listdata);
//                listdata.clear();
//                listdata.addAll(hs);
                mBinding.spinnerCustomerName.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner,listdata));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(),getString(R.string.failed),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListeners() {
        mBinding.fab.setOnClickListener(this);
        mBinding.spinnerCustomerName.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                FragmentManager fm = getSupportFragmentManager();
                AddSalesInvoiceDialog salesInvoiceDialog = AddSalesInvoiceDialog.newInstance(listdata);
                salesInvoiceDialog.show(fm, "fragment_add_sales");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinnerCustomerName:
                getDetails(mBinding.spinnerCustomerName.getSelectedItem().toString());
                break;
        }
    }

    private void getDetails(String customerName) {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();

        Call<ArrayList<SalesModel>> salesCall = mApiInterface.getAllSales(customerName);
        salesCall.enqueue(new Callback<ArrayList<SalesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SalesModel>> call, Response<ArrayList<SalesModel>> response) {
                pDialog.dismiss();
                ArrayList<SalesModel> result = new ArrayList<>();
                result.addAll(response.body());

                mBinding.salesInvoiceList.setAdapter(new SalesInvoiceAdapter(getApplicationContext(), result));
                mBinding.salesInvoiceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                Log.d("asas", "onFailure: "+response.message());
            }

            @Override
            public void onFailure(Call<ArrayList<SalesModel>> call, Throwable t) {
                Log.d("asas", "onFailure: "+t.getMessage());
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
