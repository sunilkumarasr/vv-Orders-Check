package com.villagevegetables.orders.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.villagevegetables.orders.Adapters.OrdersDetailsAdapter
import com.villagevegetables.orders.Api.RetrofitClient
import com.villagevegetables.orders.Config.ViewController
import com.villagevegetables.orders.Modal.OrderHistoryResponse
import com.villagevegetables.orders.Modal.ProductModel
import com.villagevegetables.orders.R
import com.villagevegetables.orders.databinding.ActivityOrderDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailsActivity : AppCompatActivity() {

    val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    var orderType: String = ""
    var orderId: String = ""
    var customerId: String = ""

    var cellNumber: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )

        orderType = intent.getStringExtra("orderType").toString()
        orderId = intent.getStringExtra("orderId").toString()
        customerId = intent.getStringExtra("customerId").toString()


        inits()

    }

    private fun inits() {

        if (orderType == "Order Placed"){
            binding.imgCall.visibility = View.VISIBLE
            binding.cardViewPickUp.visibility = View.VISIBLE
            binding.txtButton.setText(R.string.conform)
        }else if (orderType == "Pending"){
            binding.imgCall.visibility = View.VISIBLE
            binding.cardViewPickUp.visibility = View.VISIBLE
            binding.txtButton.setText(R.string.Pickup)
        }else if (orderType == "Delivered"){
            binding.imgCall.visibility = View.VISIBLE
            binding.cardViewPickUp.visibility = View.VISIBLE
            binding.txtButton.setText(R.string.Delivered)
        }else{
            binding.cardViewPickUp.visibility = View.GONE
            binding.imgCall.visibility = View.GONE
            binding.txtOrderStatus.visibility = View.VISIBLE
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getOrdersHistoryApi()
        }

        binding.cardViewPickUp.setOnClickListener {
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.showToast(applicationContext, "Please check your connection ")
            } else {
                conformationDialog()
            }
        }

        binding.imgCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:"+cellNumber)
            startActivity(intent)
        }

    }

    private fun getOrdersHistoryApi() {
        ViewController.showLoading(this@OrderDetailsActivity)
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getOrdersHistoryApi(getString(R.string.api_key), customerId )
        call.enqueue(object : Callback<ProductModel> {
            override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {
                        val selectedServicesList = response.body()?.response
                        //empty
                        if (selectedServicesList != null) {
                            if (selectedServicesList.isNotEmpty()) {
                                DataSet(selectedServicesList)
                            }else{
                                binding.linearNoData.visibility = View.VISIBLE
                            }
                        }else{
                            binding.linearNoData.visibility = View.VISIBLE
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                    binding.linearNoData.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                ViewController.hideLoading()
                binding.linearNoData.visibility = View.VISIBLE
                Log.e("onFailureCategoryModel", "API Call Failed: ${t.message}")
            }
        })
    }
    private fun DataSet(selectedOrdersList: List<OrderHistoryResponse>) {

        for (order in selectedOrdersList) {
            if (order.orderId == orderId) {
                val ordersItemsList = order.productDetails.toList() ?: emptyList()
                binding.recyclerview.layoutManager = GridLayoutManager(this@OrderDetailsActivity, 1)
                binding.recyclerview.adapter = OrdersDetailsAdapter(this@OrderDetailsActivity, ordersItemsList) { item ->
//                    val intent = Intent(this@OrderDetailsActivity, ProductsDetailsActivity::class.java).apply {
//                        putExtra("productsId", item.productsId)
//                    }
//                    startActivity(intent)
//                    overridePendingTransition(R.anim.from_right, R.anim.to_left)
                }

                //items total
                val totalItemsPrice = ordersItemsList.sumByDouble {
                    (it.price.toDoubleOrNull() ?: 0.0)
                }
                binding.txtItemsHeader.text = getString(R.string.Items)+ " (" + ordersItemsList.size+")"
                binding.txtItems.text = getString(R.string.Items)+ " " + ordersItemsList.size
                binding.txtItemsPrice.text = "₹" + order.grandTotal
                //remove mobile numbers in address
                val cleanedAddress = order.address.replace(Regex("\\b\\d{10}\\b,?\\s*"), "")
                    .trim()
                    .trimEnd(',')
                    .replace(Regex(",\\s*,+"), ",")
                binding.txtAddress.text = getString(R.string.Address_) +cleanedAddress
                binding.txtName.text = getString(R.string.Name_) +order.fullName
                binding.txtMobile.text = getString(R.string.Mobile_) +"941*******"
                cellNumber = order.mobile
                binding.txtEmail.text = getString(R.string.Email_) +"*******@gmail.com"
                binding.txtOrderDate.text =getString(R.string.OrderDate_) + order.updatedDate
                if (!order.deliveryCharge.equals("")){
                    binding.txtDeliveryCharge.text = "₹" +order.deliveryCharge
                    var sum = order.grandTotal.toDouble() + order.deliveryCharge.toDouble()
                    binding.txtOrderTotalAmount.text = "₹" + sum.toString()
                }else{
                    binding.txtDeliveryCharge.text = getString(R.string.Free)
                    binding.txtOrderTotalAmount.text = "₹" + order.grandTotal
                }

                // set fields
                binding.txtOrderNumber.text = order.orderNumber

                break
            }
        }

    }

    private fun conformationDialog() {
        val bottomSheetDialog = BottomSheetDialog(this@OrderDetailsActivity)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_conformation, null)
        bottomSheetDialog.setContentView(view)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        buttonCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        buttonOk.setOnClickListener {
            bottomSheetDialog.dismiss()
            if (orderType.equals("Order Placed")){
                //Order Placed status 3
                getPickUpStatusChangeApi("2")
            }else if (orderType.equals("Pending")){
                //PickUp status 3
                getPickUpStatusChangeApi("3")
            }else{
                //Delivered status 4
                getPickUpStatusChangeApi("4")
            }
        }
        bottomSheetDialog.show()
    }
    private fun getPickUpStatusChangeApi(status: String) {
        //PickUp status 3
        //Delivered status 4
        ViewController.showLoading(this@OrderDetailsActivity)
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getPickUpStatusChangeApi(getString(R.string.api_key), orderId, status )
        call.enqueue(object : Callback<ProductModel> {
            override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {
                        if ( response.body()?.status != null &&  response.body()?.status==true) {
                            ViewController.showToast(this@OrderDetailsActivity,response.body()?.message.toString())
                            finish()
                        }else{
                            ViewController.showToast(this@OrderDetailsActivity,"Try Again")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    ViewController.showToast(this@OrderDetailsActivity,"Try Again")
                }
            }
            override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(this@OrderDetailsActivity,"Try Again")
            }
        })
    }

}