package me.ashif.mobileinventory.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.ActivityPurchaseInvoiceBinding;
import me.ashif.mobileinventory.fragment.AddPurchaceInvoiceDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseInvoiceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ActivityPurchaseInvoiceBinding mBinding;
    private ApiInterface mApiInterface;
    private ProgressDialog pDialog;
    private ArrayList<String> listdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_purchase_invoice);

        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        setListeners();
        setObjects();
        getSuppliersList();
    }

    private void setObjects() {
        pDialog = new ProgressDialog(this);
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
                    jArray = j.getJSONArray("suppliername");
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

                mBinding.spinnerSupplierName.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner,listdata));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setListeners() {
        mBinding.fab.setOnClickListener(this);
        mBinding.spinnerSupplierName.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
            {
                FragmentManager fm = getSupportFragmentManager();
                AddPurchaceInvoiceDialog purchaceInvoiceDialog = AddPurchaceInvoiceDialog.newInstance(listdata);
                purchaceInvoiceDialog.show(fm, "fragment_add_purchase");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
