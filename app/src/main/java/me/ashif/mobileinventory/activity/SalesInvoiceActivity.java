package me.ashif.mobileinventory.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import me.ashif.mobileinventory.adapter.SalesInvoiceAdapter;
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.ActivitySalesInvoiceBinding;
import me.ashif.mobileinventory.fragment.AddSalesInvoiceDialog;
import me.ashif.mobileinventory.listener.OnDeleteClicked;
import me.ashif.mobileinventory.model.SalesModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesInvoiceActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        OnDeleteClicked {

    private static final String TAG = SalesInvoiceActivity.class.getSimpleName();
    private ActivitySalesInvoiceBinding mBinding;
    private ApiInterface mApiInterface;
    private ProgressDialog pDialog;
    private ArrayList<String> listdata;
    private MenuInflater inflater;
    private float mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sales_invoice);

        setListeners();
        setObjects();
        getCustomersList();
    }

    private void setObjects() {
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        inflater = getMenuInflater();
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

                listdata = new ArrayList<>();
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
                mBinding.spinnerCustomerName.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, listdata));
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
                                getCustomersList();
                                Toast.makeText(getApplicationContext(), R.string.retrying, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void setListeners() {
        mBinding.menuItem1.setOnClickListener(this);
        mBinding.menuItem2.setOnClickListener(this);
        mBinding.spinnerCustomerName.setOnItemSelectedListener(this);
        mBinding.spinnerCustomerCode.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_item1: {
                FragmentManager fm = getSupportFragmentManager();
                AddSalesInvoiceDialog salesInvoiceDialog = AddSalesInvoiceDialog.newInstance(listdata);
                salesInvoiceDialog.show(fm, "fragment_add_sales");
            }
            break;
            case R.id.menu_item2: {
                new AlertDialog.Builder(this)
                        .setTitle("Report")
                        .setMessage("Net Sales Amount : " + mTotal + " riyal")
                        .setPositiveButton("ok", null)
                        .show();
            }
            break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinnerCustomerName:
                getCustomerCode(mBinding.spinnerCustomerName.getSelectedItem().toString());
                break;
            case R.id.spinnerCustomerCode:
                getDetails(mBinding.spinnerCustomerName.getSelectedItem().toString(), mBinding.spinnerCustomerCode.getSelectedItem().toString());
                break;
        }
    }

    private void getCustomerCode(String customerName) {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();

        Call<ResponseBody> call = mApiInterface.getCustomerCode(customerName);
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
                hs.addAll(listdata);
                listdata.clear();
                listdata.addAll(hs);
                mBinding.spinnerCustomerCode.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, codeList));
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
                                getCustomerCode(mBinding.spinnerCustomerName.getSelectedItem().toString());
                                Toast.makeText(getApplicationContext(), R.string.retrying, Toast.LENGTH_SHORT).show();
                            }
                        });
                Toast.makeText(getApplicationContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDetails(String customerName, String customerCode) {
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();

        Call<ArrayList<SalesModel>> salesCall = mApiInterface.getAllSales(customerName, customerCode);
        salesCall.enqueue(new Callback<ArrayList<SalesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SalesModel>> call, Response<ArrayList<SalesModel>> response) {
                pDialog.dismiss();
                ArrayList<SalesModel> result = new ArrayList<>();
                result.addAll(response.body());

                fillList(result);
                ArrayList<Float> totalList = new ArrayList<Float>();
                for (SalesModel s : result) {
                    totalList.add(s.getTotal());
                }
                sumIt(totalList);
            }

            @Override
            public void onFailure(Call<ArrayList<SalesModel>> call, Throwable t) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Error")
                        .setMessage("Failed to reach our servers")
                        .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getDetails(mBinding.spinnerCustomerName.getSelectedItem().toString(), mBinding.spinnerCustomerCode.getSelectedItem().toString());
                                Toast.makeText(getApplicationContext(), R.string.retrying, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void sumIt(ArrayList<Float> totalList) {
        mTotal = 0;
        for (int i = 0; i < totalList.size(); i++) {
            mTotal += totalList.get(i);
        }
    }

    private void fillList(ArrayList<SalesModel> result) {
        mBinding.salesInvoiceList.setAdapter(new SalesInvoiceAdapter(getApplicationContext(), result, inflater, this));
        mBinding.salesInvoiceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void deleteClicked(int id) {
        Log.d(TAG, "deleteTriggered: inside delete");
        Call<ResponseBody> call = mApiInterface.deleteSales(id);
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
