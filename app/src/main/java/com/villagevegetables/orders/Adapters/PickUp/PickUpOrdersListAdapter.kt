package com.villagevegetables.orders.Adapters.PickUp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.villagevegetables.orders.Config.ViewController
import com.villagevegetables.orders.Modal.OrderHistoryResponse
import com.villagevegetables.orders.R

class PickUpOrdersListAdapter(private val context: Context,
                              private val items: List<OrderHistoryResponse>,
                              private val onItemClick: (OrderHistoryResponse) -> Unit // Click listener function
) : RecyclerView.Adapter<PickUpOrdersListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val txtAddress: TextView = itemView.findViewById(R.id.txtAddress)
        val txtOrderNumber: TextView = itemView.findViewById(R.id.txtOrderNumber)
        val txtOrderAmount: TextView = itemView.findViewById(R.id.txtOrderAmount)
        val txtOrderStatus: TextView = itemView.findViewById(R.id.txtOrderStatus)
        val txtOrderDate: TextView = itemView.findViewById(R.id.txtOrderDate)
        val txtTotalItems: TextView = itemView.findViewById(R.id.txtTotalItems)
        init {
            itemView.setOnClickListener {
                val animations = ViewController.animation()
                itemView.startAnimation(animations)
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

//        Glide.with(context).load(item.productDetails[0].image).error(R.drawable.single_logo)
//            .placeholder(R.drawable.single_logo)
//            .into(holder.image)
        holder.image.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.scooter_color_ic))

        //remove mobile numbers in address
        val cleanedAddress = item.address.replace(Regex("\\b\\d{10}\\b,?\\s*"), "")
            .trim()
            .trimEnd(',')
            .replace(Regex(",\\s*,+"), ",")
        holder.txtAddress.text = cleanedAddress
        holder.txtOrderNumber.text = item.orderNumber
        if (!item.deliveryCharge.equals("")){
            var sum = item.grandTotal.toDouble() + item.deliveryCharge.toDouble()
            holder.txtOrderAmount.text = "₹" + sum
        }else{
            holder.txtOrderAmount.text = "₹" + item.grandTotal
        }
        holder.txtOrderDate.text = item.createdDate

        //order status
        holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.statusBarBg))
        holder.txtOrderStatus.text = "Pickup"

//        when (item.deliveryStatus) {
//            "1" -> {
//                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
//                holder.txtOrderStatus.text = "Order Placed"
//            }
//            "2" -> {
//                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.pending))
//                holder.txtOrderStatus.text = "Confirmed"
//            }
//            "3" -> {
//                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.statusBarBg))
//                holder.txtOrderStatus.text = "Pickup"
//            }
//            "4" -> {
//                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
//                holder.txtOrderStatus.text = "Delivered"
//            }
//            "5" -> {
//                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.selectedRed))
//                holder.txtOrderStatus.text = "Canceled"
//            }
//        }

        //total items size
        val itemsSize =item.productDetails as ArrayList
        holder.txtTotalItems.text = "("+itemsSize.size+(context.getString(R.string.Items) +")")

    }

    override fun getItemCount(): Int {
        return items.size
    }

}