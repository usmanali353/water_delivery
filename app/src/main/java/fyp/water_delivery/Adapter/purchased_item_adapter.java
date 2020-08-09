package fyp.water_delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fyp.water_delivery.Model.cartItems;
import fyp.water_delivery.R;

public class purchased_item_adapter extends RecyclerView.Adapter<purchased_item_adapter.purchased_item_viewholder> {
    public purchased_item_adapter(ArrayList<fyp.water_delivery.Model.cartItems> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    ArrayList<cartItems> cartItems;
Context context;
    @NonNull
    @Override
    public purchased_item_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.purchased_item_layout,parent,false);
        return new purchased_item_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull purchased_item_viewholder holder, int position) {
           holder.checkOutItemName.setText(cartItems.get(position).getName()+" "+cartItems.get(position).getWeight());
           holder.checkOutItemQuantity.setText("x "+String.valueOf(cartItems.get(position).getQuantity()));
           holder.checkOutItemPrice.setText("Rs "+String.valueOf(cartItems.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class purchased_item_viewholder extends RecyclerView.ViewHolder{
        TextView checkOutItemName,checkOutItemQuantity,checkOutItemPrice;
        public purchased_item_viewholder(@NonNull View itemView) {
            super(itemView);
            checkOutItemName=itemView.findViewById(R.id.checkout_item_name);
            checkOutItemQuantity=itemView.findViewById(R.id.checkout_item_quantity);
            checkOutItemPrice=itemView.findViewById(R.id.checkout_item_price);
        }
    }
}
