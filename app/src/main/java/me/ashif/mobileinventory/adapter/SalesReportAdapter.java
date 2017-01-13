package me.ashif.mobileinventory.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;
import me.ashif.mobileinventory.model.SalesModel;

/**
 * Created by Ashif on 13/1/17,January,2017
 * TechJini Solutions
 * Banglore,India
 */
public class SalesReportAdapter extends LongPressAwareTableDataAdapter<SalesModel> {

    private static final int TEXT_SIZE = 14;

    /**
     * Creates a new {@link LongPressAwareTableDataAdapter} with the given paramters.
     *
     * @param context   The context that shall be used.
     * @param data      The data that shall be displayed.
     * @param tableView The table to listen for long presses by the user.
     */
    public SalesReportAdapter(Context context, List<SalesModel> data, TableView<SalesModel> tableView) {
        super(context, data, tableView);
    }


    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final SalesModel model = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderItemName(model);
                break;
            case 1:
                renderedView = renderCustomerName(model);
                break;
            case 2:
                renderedView = renderCustomerCode(model);
                break;
            case 3:
                renderedView = renderCommission(model);
                break;
            case 4:
                renderedView = renderUnits(model);
                break;
            case 5:
                renderedView = renderPrice(model);
                break;
            case 6:
                renderedView = renderAmount(model);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        return null;
    }

    private View renderAmount(SalesModel model) {
        return renderString(String.valueOf(model.getTotal()));
    }

    private View renderPrice(SalesModel model) {
        return renderString(String.valueOf(model.getPrice()));
    }

    private View renderUnits(SalesModel model) {
        return renderString(String.valueOf(model.getQuantity()));
    }

    private View renderCustomerCode(SalesModel model) {
        return renderString(model.getCustomerCode());
    }

    private View renderCommission(SalesModel model) {
        return renderString(String.valueOf(model.getCommission()));
    }

    private View renderCustomerName(SalesModel model) {
        return renderString(model.getCustomerName());
    }

    private View renderItemName(SalesModel model) {
        return renderString(model.getItemName());
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }
}
