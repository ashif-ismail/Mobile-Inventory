package me.ashif.mobileinventory.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

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
        mApiInterface = RetrofitClient.getClient().create(ApiInterface.class);
    }

    private void setObjects() {
        pDialog = new ProgressDialog(this);
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
        if (!mBinding.textUnitprice.getText().toString().isEmpty() && !
                mBinding.textItemq.getText().toString().isEmpty()) {
            int totalAmount = Integer.valueOf(mBinding.textItemq.getText().toString()) *
                    Integer.valueOf(mBinding.textUnitprice.getText().toString());
            mBinding.textTotal.setText(String.valueOf(totalAmount));
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
                    model.setCustomerName(mBinding.textItemeuppliername.getText().toString());
                    model.setCommission(Float.parseFloat(mBinding.textSuppliercomm.getText().toString()));
                    model.setPrice(Integer.parseInt(mBinding.textUnitprice.getText().toString()));
                    model.setQuantity(Integer.parseInt(mBinding.textItemq.getText().toString()));
                    model.setTotal(Float.parseFloat(mBinding.textTotal.getText().toString()));
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
        Call<ResponseBody> saveCall = mApiInterface.setSales(model.getItemName(), model.getCustomerName(), model.getCommission(), model.getQuantity(), model.getPrice(), model.getTotal());
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
        if (mBinding.textItemname.getText().toString().isEmpty() || mBinding.textItemeuppliername.getText().toString().isEmpty()
                || mBinding.textSuppliercomm.getText().toString().isEmpty() || mBinding.textItemq.getText().toString().isEmpty()
                || mBinding.textUnitprice.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }
}
