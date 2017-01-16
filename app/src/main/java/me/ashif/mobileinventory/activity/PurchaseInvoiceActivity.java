package me.ashif.mobileinventory.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuInflater;
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
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.ActivityPurchaseInvoiceBinding;
import me.ashif.mobileinventory.fragment.AddPurchaceInvoiceDialog;
import me.ashif.mobileinventory.listener.OnDeleteClicked;
import me.ashif.mobileinventory.model.PurchaseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseInvoiceActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        OnDeleteClicked {

    private static String TAG = PurchaseInvoiceActivity.class.getSimpleName();
    private ActivityPurchaseInvoiceBinding mBinding;
    private ApiInterface mApiInterface;
    private ProgressDialog pDialog;
    private ArrayList<String> listdata;
    private MenuInflater inflater;
    private float mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_purchase_invoice);

        setListeners();
        setObjects();
        getSuppliersList();
    }

    private void setObjects() {
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        inflater = getMenuInflater();
    }

    private void getSuppliersList() {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        Call<ResponseBody> suppliersCall = mApiInterface.getAllSuppliers();
        suppliersCall.enqueue(new Callback<ResponseBody>() {
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

                listdata = new ArrayList<>();
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
                mBinding.spinnerSupplierNameAc.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, listdata));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Error")
                        .setMessage("Failed to reach our servers")
                        .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getSuppliersList();
                                Toast.makeText(getApplicationContext(), R.string.retrying, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    private void setListeners() {
        mBinding.menuItem1.setOnClickListener(this);
        mBinding.menuItem2.setOnClickListener(this);
        mBinding.spinnerSupplierNameAc.setOnItemSelectedListener(this);
        mBinding.spinnerSuppliercodeAc.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_item1: {
                FragmentManager fm = getSupportFragmentManager();
                AddPurchaceInvoiceDialog purchaceInvoiceDialog = AddPurchaceInvoiceDialog.newInstance(listdata);
                purchaceInvoiceDialog.show(fm, "fragment_add_purchase");
            }
            break;
            case R.id.menu_item2:{
                new AlertDialog.Builder(this)
                        .setTitle("Report")
                        .setMessage("Net Purchase Amount : " + mTotal + " riyal")
                        .setPositiveButton("ok",null)
                        .show();
            }
            break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinnerSupplierNameAc:
                getSupplierCode(mBinding.spinnerSupplierNameAc.getSelectedItem().toString());
                break;
            case R.id.spinnerSuppliercodeAc:
                getDetails(mBinding.spinnerSupplierNameAc.getSelectedItem().toString(), mBinding.spinnerSuppliercodeAc.getSelectedItem().toString());
                break;
        }
    }

    private void getSupplierCode(String supplierName) {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        Call<ResponseBody> call = mApiInterface.getSupplierCode(supplierName);
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
                ArrayList<String> codeList;
                codeList = new ArrayList<>();
                JSONArray jArray = null;
                try {
                    jArray = j.getJSONArray("entityCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        try {
                            codeList.add(jArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Set<String> hs = new HashSet<>();
                hs.addAll(codeList);
                codeList.clear();
                codeList.addAll(hs);
                mBinding.spinnerSuppliercodeAc.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, codeList));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Error")
                        .setMessage("Failed to reach our servers")
                        .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getSupplierCode(mBinding.spinnerSupplierNameAc.getSelectedItem().toString());
                                Toast.makeText(getApplicationContext(), R.string.retrying, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


    private void getDetails(String supplierName, String supplierCode) {

        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
        Call<ArrayList<PurchaseModel>> call = mApiInterface.getAllPurchases(supplierName, supplierCode);
        call.enqueue(new Callback<ArrayList<PurchaseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PurchaseModel>> call, Response<ArrayList<PurchaseModel>> response) {
                pDialog.dismiss();
                ArrayList<PurchaseModel> result = new ArrayList<>();
                result.addAll(response.body());
                fillList(result);
                ArrayList<Float> totalList = new ArrayList<Float>();
                for (PurchaseModel p : result){
                    totalList.add(p.getTotal());
                }
                sumIt(totalList);

            }

            @Override
            public void onFailure(Call<ArrayList<PurchaseModel>> call, Throwable t) {
                pDialog.dismiss();
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Error")
                        .setMessage("Failed to reach our servers")
                        .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getDetails(mBinding.spinnerSupplierNameAc.getSelectedItem().toString(), mBinding.spinnerSuppliercodeAc.getSelectedItem().toString());
                                Toast.makeText(getApplicationContext(), R.string.retrying, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void sumIt(ArrayList<Float> totalList) {
        mTotal = 0;
        for(int i = 0; i < totalList.size(); i++)
        {
            mTotal += totalList.get(i);
        }
    }

    private void fillList(ArrayList<PurchaseModel> result) {
        mBinding.purchaseInvoiceList.setAdapter(new PurchaseInvoiceAdapter(getApplicationContext(), result, inflater, this));
        mBinding.purchaseInvoiceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void deleteClicked(final int id) {
        Log.d(TAG, "deleteTriggered: inside delete");
        Call<ResponseBody> call = mApiInterface.deletePurchase(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.message());
                Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
