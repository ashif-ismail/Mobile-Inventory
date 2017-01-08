package me.ashif.mobileinventory.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.ashif.mobileinventory.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSalesInvoiceDialog extends DialogFragment {


    public AddSalesInvoiceDialog() {
        // Required empty public constructor
    }

    public static AddSalesInvoiceDialog newInstance() {
        AddSalesInvoiceDialog salesInvoiceDialog = new AddSalesInvoiceDialog();
        return salesInvoiceDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_sales_invoice_dialog, null);
        alertDialogBuilder.setView(view);

        alertDialogBuilder.setTitle("Add Sales Details")
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do work
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        return  alertDialogBuilder.create();
    }

}
