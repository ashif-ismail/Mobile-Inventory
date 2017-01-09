package me.ashif.mobileinventory.model;

/**
 * Created by Ashif on 9/1/17,January,2017
 * TechJini Solutions
 * Banglore,India
 */

public class PurchaseInvoiceModel {

    private String itemName;
    private String supplierName;
    private float supplierCommission;
    private int price;
    private int quantity;
    private float total;

    public float getSupplierCommission() {
        return supplierCommission;
    }

    public void setSupplierCommission(float supplierCommission) {
        this.supplierCommission = supplierCommission;
    }

    public String getName() {
        return itemName;
    }

    public void setName(String name) {
        this.itemName = name;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }


}
