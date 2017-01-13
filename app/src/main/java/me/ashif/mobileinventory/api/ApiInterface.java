package me.ashif.mobileinventory.api;

import java.util.ArrayList;

import me.ashif.mobileinventory.model.PurchaseModel;
import me.ashif.mobileinventory.model.SalesModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ashif on 10/1/17,January,2017
 * TechJini Solutions
 * Banglore,India
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("purchase/add")
    Call<ResponseBody> setPurchase(@Field("supplierCode") String supplierCode,
                                   @Field("itemName") String itemName,
                                   @Field("supplierName") String supplierName,
                                   @Field("commission") float commission,
                                   @Field("quantity") int quantity,
                                   @Field("price") int price,
                                   @Field("total") float total);

    @FormUrlEncoded
    @PUT("purchase/update/{id}")
    Call<ResponseBody> updatePurchase(@Path("id") Integer id,
                                      @Field("supplierCode") String supplierCode,
                                      @Field("itemName") String itemName,
                                      @Field("supplierName") String supplierName,
                                      @Field("commission") float commission,
                                      @Field("quantity") int quantity,
                                      @Field("price") int price,
                                      @Field("total") float total);

    @FormUrlEncoded
    @POST("sales/add")
    Call<ResponseBody> setSales(@Field("customerCode") String customerCode,
                                @Field("itemName") String itemName,
                                @Field("customerName") String customerName,
                                @Field("commission") float commission,
                                @Field("quantity") int quantity,
                                @Field("price") int price,
                                @Field("total") float total);

    @FormUrlEncoded
    @PUT("sales/update/{id}")
    Call<ResponseBody> updateSales(@Path("id") Integer id,
                                   @Field("customerCode") String customerCode,
                                   @Field("itemName") String itemName,
                                   @Field("customerName") String customerName,
                                   @Field("commission") float commission,
                                   @Field("quantity") int quantity,
                                   @Field("price") int price,
                                   @Field("total") float total);

    @GET("purchase/suppliers")
    Call<ResponseBody> getAllSuppliers();

    @GET("purchase/items")
    Call<ResponseBody> getItemsForSupplier(@Query("supplierName") String supplierName);

    @GET("purchase/details")
    Call<ArrayList<PurchaseModel>> getPurchaseDetails(@Query("supplierName") String supplierName, @Query("itemName") String itemName);

    @GET("purchase/invoice")
    Call<ArrayList<PurchaseModel>> getAllPurchases(@Query("supplierName") String supplierName,
                                                   @Query("supplierCode") String supplierCode);

    @GET("sales/customers")
    Call<ResponseBody> getAllCustomers();

    @GET("sales/items")
    Call<ResponseBody> getItemsForCustomer(@Query("customerName") String customerName);

    @GET("sales/details")
    Call<ArrayList<SalesModel>> getSalesDetails(@Query("customerName") String customerName, @Query("itemName") String itemName);

    @GET("sales/invoice")
    Call<ArrayList<SalesModel>> getAllSales(@Query("customerName") String customerName,
                                            @Query("customerCode") String customerCode);

    @GET("purchase/suppliercode")
    Call<ResponseBody> getSupplierCode(@Query("supplierName") String supplierName);

    @GET("purchase/suppliercodes")
    Call<ResponseBody> getAllSupplierCode();

    @GET("sales/customercodes")
    Call<ResponseBody> getAllCustomerCode();

    @GET("sales/customercode")
    Call<ResponseBody> getCustomerCode(@Query("customerName") String customerName);

    @DELETE("purchase/delete/{id}")
    Call<ResponseBody> deletePurchase(@Path("id") Integer id);

    @DELETE("sales/delete/{id}")
    Call<ResponseBody> deleteSales(@Path("id") Integer id);

    @GET("sales")
    Call<ArrayList<SalesModel>> getAllSalesReport();

    @GET("purchase/")
    Call<ResponseBody> getAllPurchaseReport();
}
