package fyp.water_delivery;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fyp.water_delivery.Model.Orders;
import fyp.water_delivery.R;

public class orderDetails extends AppCompatActivity {
    Orders order;
    SharedPreferences prefs;
    TextView customerName,phone,email,address,orderedItems;
    Button confirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        customerName=findViewById(R.id.customerName);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        orderedItems=findViewById(R.id.items);
        confirmBtn=findViewById(R.id.confirm_button);
        order=new Gson().fromJson(getIntent().getStringExtra("orderData"),Orders.class);
        if(order!=null){
            customerName.setText(order.getCustomerName());
            phone.setText(order.getPhone());
            email.setText(order.getEmail());
            address.setText(order.getAddress());
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<order.getOrderedItems().size();i++){
                sb.append(order.getOrderedItems().get(i).getName()+" "+order.getOrderedItems().get(i).getWeight()+" "+"x"+order.getOrderedItems().get(i).getQuantity());
                sb.append("\n");
            }
            orderedItems.setText(sb.toString());
            if(order.getAssignTo()!=null){
                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog cancelOrderDialog=new AlertDialog.Builder(orderDetails.this)
                                .setTitle("Cancel Order")
                                .setMessage("Do you want to cancel this Order?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Firebase_Operations.DeleteOrder(orderDetails.this,getIntent().getStringExtra("orderId"));
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        cancelOrderDialog.show();

                    }
                });
            }else{confirmBtn.setVisibility(View.GONE);}

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
