package me.ashif.mobileinventory.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "itemName",
        "supplierCode",
        "quantity",
        "price",
        "total",
        "supplierName",
        "commission"
})
public class PurchaseModel {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("itemName")
    private String itemName;
    @JsonProperty("supplierCode")
    private Object supplierCode;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("total")
    private float total;
    @JsonProperty("supplierName")
    private String supplierName;
    @JsonProperty("commission")
    private float commission;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("itemName")
    public String getItemName() {
        return itemName;
    }

    @JsonProperty("itemName")
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @JsonProperty("supplierCode")
    public Object getSupplierCode() {
        return supplierCode;
    }

    @JsonProperty("supplierCode")
    public void setSupplierCode(Object supplierCode) {
        this.supplierCode = supplierCode;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("price")
    public Integer getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Integer price) {
        this.price = price;
    }

    @JsonProperty("total")
    public float getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(float total) {
        this.total = total;
    }

    @JsonProperty("supplierName")
    public String getSupplierName() {
        return supplierName;
    }

    @JsonProperty("supplierName")
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @JsonProperty("commission")
    public float getCommission() {
        return commission;
    }

    @JsonProperty("commission")
    public void setCommission(float commission) {
        this.commission = commission;
    }

}