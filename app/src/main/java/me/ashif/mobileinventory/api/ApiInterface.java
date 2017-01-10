package me.ashif.mobileinventory.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ashif on 10/1/17,January,2017
 * TechJini Solutions
 * Banglore,India
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("purchase/add")
    Call<ResponseBody> setPurchase(@Field("itemName") String itemName,
                                   @Field("supplierName") String supplierName,
                                   @Field("commission") float commission,
                                   @Field("quantity") int quantity,
                                   @Field("price") int price,
                                   @Field("total") float total);

    @FormUrlEncoded
    @POST("sales/add")
    Call<ResponseBody> setSales(@Field("itemName") String itemName,
                                   @Field("customerName") String customerName,
                                   @Field("commission") float commission,
                                   @Field("quantity") int quantity,
                                   @Field("price") int price,
                                   @Field("total") float total);

    @FormUrlEncoded
    @POST("p_invoice/add")
    Call<ResponseBody> setPurchaseInvoice(@Field("itemName") String itemName,
                                @Field("supplierName") String customerName,
                                @Field("supplierCommission") float commission,
                                @Field("quantity") int quantity,
                                @Field("price") int price,
                                @Field("total") float total);

    @FormUrlEncoded
    @POST("s_invoice/add")
    Call<ResponseBody> setSalesInvoice(@Field("itemName") String itemName,
                                       @Field("customerName") String customerName,
                                       @Field("customerCommission") float customerCommission,
                                       @Field("quantity") int quantity,
                                       @Field("price") int price,
                                       @Field("total") float total);
    @GET("purchase/suppliers")
    Call<ResponseBody> getAllSuppliers();

    @GET("purchase/items")
    Call<ResponseBody> getItemsForSupplier(@Query("supplierName") String supplierName);
}
