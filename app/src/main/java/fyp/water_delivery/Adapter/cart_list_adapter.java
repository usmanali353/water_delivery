package fyp.water_delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fyp.water_delivery.Model.dbhelper;
import fyp.water_delivery.R;
import fyp.water_delivery.Utils;

public class cart_list_adapter extends RecyclerView.Adapter<cart_list_adapter.cart_list_viewholder> {
    ArrayList<fyp.water_delivery.Model.cartItems> cartItems;
    Context context;

    public cart_list_adapter(ArrayList<fyp.water_delivery.Model.cartItems> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public cart_list_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitemlayout,parent,false);
        return new cart_list_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final cart_list_viewholder holder, final int position) {
        holder.productName.setText(cartItems.get(position).getName()+" "+cartItems.get(position).getWeight());
        holder.price.setText("Rs "+String.valueOf(cartItems.get(position).getPrice()));
        holder.Quantity.setText(String.valueOf(cartItems.get(position).getQuantity()));
        holder.image.setImageBitmap(Utils.getBitmapFromBytes(cartItems.get(position).getImage()));
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer rows = new dbhelper(context).delete(cartItems.get(position).getProductid());
                if (rows > 0) {
                    cartItems.remove(holder.getAdapterPosition());
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(holder.getAdapterPosition(),getItemCount());
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context,"Item not Removed From Cart", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class cart_list_viewholder extends RecyclerView.ViewHolder{
        TextView productName,price,Quantity;
        ImageView image;
        Button removeBtn;
        public cart_list_viewholder(@NonNull View itemView) {
            super(itemView);
            productName=itemView.findViewById(R.id.textViewName);
            price=itemView.findViewById(R.id.price);
            Quantity=itemView.findViewById(R.id.quantity);
            image=itemView.findViewById(R.id.imageView);
            removeBtn=itemView.findViewById(R.id.buttonRemove);
        }
    }
}
