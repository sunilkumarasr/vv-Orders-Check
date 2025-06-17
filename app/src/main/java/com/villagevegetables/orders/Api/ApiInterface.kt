package com.villagevegetables.orders.Api

import com.villagevegetables.orders.Modal.ProductModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("get_all_orders_list")
    fun getOrdersListApi(
        @Field("api_key") apiKey: String,
        @Field("delivery_status") deliveryStatus: String,
        @Field("start_date") startDate: String,
        @Field("end_date") endDate: String
    ): Call<ProductModel>

    @FormUrlEncoded
    @POST("get_orders_list")
    fun getOrdersHistoryApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String
    ): Call<ProductModel>

    @FormUrlEncoded
    @POST("update_order_status")
    fun getPickUpStatusChangeApi(
        @Field("api_key") apiKey: String,
        @Field("order_id") orderId: String,
        @Field("delivery_status") deliveryStatus: String,
    ): Call<ProductModel>

}