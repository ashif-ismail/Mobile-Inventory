package me.ashif.mobileinventory.model;

/**
 * Created by Ashif on 9/1/17,January,2017
 * TechJini Solutions
 * Banglore,India
 */

public class SalesInvoiceModel {

    private String itemName;
    private String customerName;
    private float customerCommission;
    private int price;
    private int quantity;
    private float total;

    public float getCustomerCommission() {
        return customerCommission;
    }

    public void setCustomerCommission(float customerCommission) {
        this.customerCommission = customerCommission;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
