package me.ashif.mobileinventory.api;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Ashif on 10/1/17,January,2017
 * TechJini Solutions
 * Banglore,India
 */

public class RetrofitClient {
    public static final String BASE_URL = "http://mob-inventory.herokuapp.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
