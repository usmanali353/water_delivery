package fyp.water_delivery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import fyp.water_delivery.Model.cartItems;
import fyp.water_delivery.Adapter.cart_list_adapter;
import fyp.water_delivery.Model.dbhelper;
import fyp.water_delivery.R;

public class shopping_cart extends AppCompatActivity {
 ArrayList<fyp.water_delivery.Model.cartItems> cartItems;
 RecyclerView cartItemsList;
 Button checkout;
// "id","productname","price","image","quantity","userId","productid","weight"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cartItems=new ArrayList<>();
        cartItemsList=findViewById(R.id.cartItemsList);
        checkout=findViewById(R.id.checkout);
        Cursor productsList=new dbhelper(this).get_products_in_cart(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(productsList.getCount()==0){
            Toast.makeText(this,"No Products in Cart",Toast.LENGTH_LONG).show();
        }else{
            while(productsList.moveToNext()){
                cartItems.add(new cartItems(productsList.getString(1),productsList.getString(6),productsList.getString(7),productsList.getBlob(3),productsList.getInt(4),productsList.getInt(2)));
            }
            cartItemsList.setLayoutManager(new LinearLayoutManager(this));
            cartItemsList.setAdapter(new cart_list_adapter(cartItems,this));
            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(shopping_cart.this,billing.class));
                }
            });
        }

    }

}
