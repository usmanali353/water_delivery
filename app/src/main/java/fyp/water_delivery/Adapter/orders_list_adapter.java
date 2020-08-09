package fyp.water_delivery.Adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import fyp.water_delivery.Model.Orders;
import fyp.water_delivery.R;
import fyp.water_delivery.orderDetails;

public class orders_list_adapter extends RecyclerView.Adapter<orders_list_adapter.order_list_viewholder> {
    ArrayList<Orders> orders;
    ArrayList<String> orderId;
    Context context;

    public orders_list_adapter(ArrayList<Orders> orders,ArrayList<String> orderId, Context context) {
        this.orders = orders;
        this.context = context;
        this.orderId=orderId;
    }

    @NonNull
    @Override
    public order_list_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_layout,parent,false);
        return new order_list_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull order_list_viewholder holder, int position) {
      holder.orderDate.setText(orders.get(position).getDate());
      holder.orderTotal.setText("Rs "+String.valueOf(orders.get(position).getTotalAmount()));
      if(orders.get(position).getAssignTo()!=null){
          holder.orderStatus.setText("Departed");
          holder.orderStatus.setTextColor(Color.parseColor("#008000"));
      }else{
          holder.orderStatus.setText("UnConformed");
          holder.orderStatus.setTextColor(Color.parseColor("#FF0000"));
      }
      holder.orderAddress.setText(orders.get(position).getAddress());
      holder.order_detail.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              context.startActivity(new Intent(context, orderDetails.class).putExtra("orderId",orderId.get(position)).putExtra("orderData",new Gson().toJson(orders.get(position))));
          }
      });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class order_list_viewholder extends RecyclerView.ViewHolder{
      TextView orderDate,orderTotal,orderStatus,orderAddress,order_detail;
        public order_list_viewholder(@NonNull View itemView) {
            super(itemView);
            orderDate=itemView.findViewById(R.id.order_date);
            orderTotal=itemView.findViewById(R.id.order_price);
            orderStatus=itemView.findViewById(R.id.order_status);
            orderAddress=itemView.findViewById(R.id.order_id);
            order_detail=itemView.findViewById(R.id.order_detail);
        }
    }
}
