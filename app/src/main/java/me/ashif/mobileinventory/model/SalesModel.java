package me.ashif.mobileinventory.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "itemName",
        "customerCode",
        "quantity",
        "price",
        "total",
        "customerName",
        "commission"
})
public class SalesModel {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("itemName")
    private String itemName;
    @JsonProperty("customerCode")
    private String customerCode;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("total")
    private float total;
    @JsonProperty("customerName")
    private String customerName;
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

    @JsonProperty("customerCode")
    public String getCustomerCode() {
        return customerCode;
    }

    @JsonProperty("customerCode")
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
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

    @JsonProperty("customerName")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("customerName")
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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