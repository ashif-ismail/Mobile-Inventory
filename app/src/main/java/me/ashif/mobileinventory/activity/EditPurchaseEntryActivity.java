package me.ashif.mobileinventory.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import me.ashif.mobileinventory.R;
import me.ashif.mobileinventory.api.ApiInterface;
import me.ashif.mobileinventory.api.RetrofitClient;
import me.ashif.mobileinventory.databinding.ActivityEditEntryBinding;
import me.ashif.mobileinventory.model.PurchaseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (!mBinding.textUnitprice.getText().toString().isEmpty() && !
                mBinding.textItemq.getText().toString().isEmpty()){
            int totalAmount = Integer.valueOf(mBinding.textItemq.getText().toString())*
                    Integer.valueOf(mBinding.textUnitprice.getText().toString());
            mBinding.textTotal.setText(String.valueOf(totalAmount));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save:
                if (isValidEntry()) {
                    showProgress(getString(R.string.loading));
                    PurchaseModel model = new PurchaseModel();
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
        if (mBinding.textItemname.getText().toString().isEmpty() || mBinding.textItemsuppliername.getText().toString().isEmpty()
                || mBinding.textSuppliercomm.getText().toString().isEmpty() || mBinding.textItemq.getText().toString().isEmpty()
                || mBinding.textUnitprice.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }

    private void displayToast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    private void postPurchaseDetails(final PurchaseModel model) {
        Call<ResponseBody> saveCall = mApiInterface.setPurchase(model.getItemName(),model.getSupplierName(),model.getCommission(),model.getQuantity(),model.getPrice(),model.getTotal());
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
