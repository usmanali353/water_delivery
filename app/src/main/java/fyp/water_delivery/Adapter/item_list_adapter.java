package fyp.water_delivery.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import fyp.water_delivery.Model.Bottles;
import fyp.water_delivery.Model.dbhelper;
import fyp.water_delivery.R;
import fyp.water_delivery.Utils;

public class item_list_adapter extends RecyclerView.Adapter<item_list_adapter.item_list_viewholder> {
    ArrayList<Bottles> bottles;
    Context context;
    ArrayList<String> productId;
    public item_list_adapter(ArrayList<Bottles> bottles, Context context, ArrayList<String> productId) {
        this.bottles = bottles;
        this.context = context;
        this.productId=productId;
    }

    @NonNull
    @Override
    public item_list_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new item_list_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull item_list_viewholder holder, final int position) {
      holder.productName.setText(bottles.get(position).getName());
      holder.productPrice.setText("Rs "+String.valueOf(bottles.get(position).getPrice()));
      holder.productWeight.setText(bottles.get(position).getWeight());
      holder.image.setImageBitmap(Utils.getBitmapFromBytes(bottles.get(position).getImage().toBytes()));
      holder.btnOrder.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              View quantityPickerView=LayoutInflater.from(context).inflate(R.layout.quantity_picker,null);
              final NumberPicker quantityPicker=quantityPickerView.findViewById(R.id.selectquantity);
              quantityPicker.setMaxValue(bottles.get(position).getQuantity());
              quantityPicker.setMinValue(1);
              AlertDialog quantitypicker=new AlertDialog.Builder(context)
               .setTitle("Select Quantity")
              // .setMessage("In Stock "+String.valueOf(bottles.get(position).getQuantity()))
               .setView(quantityPickerView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(quantityPicker.getValue()>bottles.get(position).getQuantity()){
                            Toast.makeText(context,"too much bottles selected",Toast.LENGTH_LONG).show();
                        }else{
                            int alreadyExist= new dbhelper(context).check_if_already_exist(FirebaseAuth.getInstance().getCurrentUser().getUid(),productId.get(position));
                            if(alreadyExist>0){
                                Toast.makeText(context,"Item Already Exists",Toast.LENGTH_LONG).show();
                                int isUpdated=new dbhelper(context).updateProduct(productId.get(position),bottles.get(position).getPrice()*quantityPicker.getValue(),quantityPicker.getValue(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                                if(isUpdated>0){
                                    Toast.makeText(context,"Item is Updated",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                if(new dbhelper(context).insert_product_toshoppingcart(bottles.get(position).getName(),bottles.get(position).getPrice()*quantityPicker.getValue(),bottles.get(position).getImage().toBytes(),quantityPicker.getValue(), FirebaseAuth.getInstance().getCurrentUser().getUid(),productId.get(position),bottles.get(position).getWeight())){
                                    Toast.makeText(context,"Item Added to the Cart",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(context,"Item not Added to the Cart due to some error",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                          }
                      }).create();
              quantitypicker.show();
          }
      });
    }

    @Override
    public int getItemCount() {
        return bottles.size();
    }

    class item_list_viewholder extends RecyclerView.ViewHolder{
     TextView productName,productPrice,productWeight;
     Button btnOrder;
     ImageView image;
     CardView productCard;
        public item_list_viewholder(@NonNull View itemView) {
            super(itemView);
            productName=itemView.findViewById(R.id.textViewName);
            productPrice=itemView.findViewById(R.id.textViewPrice);
            productWeight=itemView.findViewById(R.id.textViewQuantity);
            productCard=itemView.findViewById(R.id.productCard);
            btnOrder=itemView.findViewById(R.id.btnOrder);
            image=itemView.findViewById(R.id.imageView);
        }
    }
}
