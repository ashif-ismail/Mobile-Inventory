package me.ashif.mobileinventory.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.adapter.SalesReportAdapter;
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.ActivitySalesReportBinding;
import me.ashif.mobileinventory.model.SalesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReportActivity extends AppCompatActivity {

    private static final String TAG = SalesInvoiceActivity.class.getSimpleName();
    private ActivitySalesReportBinding mBinding;
    private ApiInterface mApiInterface;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sales_report);
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        progressDialog = new ProgressDialog(this);
        getAllSalesReport();

    }

    private void getAllSalesReport() {

        Log.d(TAG, "getAllSalesReport: inside sales");
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        Call<ArrayList<SalesModel>> call = mApiInterface.getAllSalesReport();
        call.enqueue(new Callback<ArrayList<SalesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SalesModel>> call, Response<ArrayList<SalesModel>> response) {
                progressDialog.dismiss();
                ArrayList<SalesModel> result = new ArrayList<SalesModel>();
                result.addAll(result);

                fillList(result);
                Log.d(TAG, "onResponse: " + response.raw().toString());
                Log.d(TAG, "onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<ArrayList<SalesModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void fillList(ArrayList<SalesModel> result) {
        Log.d(TAG, "fillList: inside fill");
        SalesReportAdapter salesReportAdapter = new SalesReportAdapter(this, result, mBinding.tableView);
        mBinding.tableView.setDataAdapter(salesReportAdapter);

    }

}
