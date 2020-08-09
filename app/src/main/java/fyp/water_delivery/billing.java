package fyp.water_delivery;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fyp.water_delivery.Model.Users;
import fyp.water_delivery.Adapter.purchased_item_adapter;
import fyp.water_delivery.Model.Orders;
import fyp.water_delivery.Model.cartItems;
import fyp.water_delivery.Model.dbhelper;
import fyp.water_delivery.R;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

public class billing extends AppCompatActivity implements Listener {
 RecyclerView checkOutItems;
    ArrayList<cartItems> cartItems;
    TextView totalNumberOfItems,subTotal,total;
    Button placeOrderBtn;
    EasyWayLocation easyWayLocation;
    double latitude=0.0,longitude=0.0;
    SharedPreferences prefs;
    Users user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        cartItems=new ArrayList<>();
         user=new Gson().fromJson(prefs.getString("user_info",null),Users.class);
        checkOutItems=findViewById(R.id.checkout_items_list);
        placeOrderBtn=findViewById(R.id.placeorderbtn);
        easyWayLocation = new EasyWayLocation(billing.this, true,billing.this);
        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(billing.this, Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(billing.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                            easyWayLocation.startLocation();
                            if(latitude!=0.0&&longitude!=0.0){
                                try{
                                    Geocoder g=new Geocoder(billing.this,Locale.getDefault());
                                    List<Address> addresses= g.getFromLocation(latitude,longitude,1);
                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
                                    String formattedDate = df.format(c);
                                    Firebase_Operations.PlaceOrders(billing.this,new Orders(user.getName(),addresses.get(0).getAddressLine(0),user.getEmail(),user.getPhone(),latitude,longitude,cartItems,"unconfirmed",FirebaseAuth.getInstance().getCurrentUser().getUid(),formattedDate,new dbhelper(billing.this).getTotalOfAmount(FirebaseAuth.getInstance().getCurrentUser().getUid())),easyWayLocation);
                                    easyWayLocation.endUpdates();
                                }catch(Exception e){
                                    Toast.makeText(billing.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }else{
                            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION }, 1000);
                        }
                    }
            }
        });
        totalNumberOfItems=findViewById(R.id.order_item_count);
        subTotal=findViewById(R.id.order_total_amount);
        total=findViewById(R.id.order_full_amounts);
        Cursor productsList=new dbhelper(this).get_products_in_cart(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(productsList.getCount()==0){
            Toast.makeText(this,"No Products in Cart",Toast.LENGTH_LONG).show();
        }else{
            while(productsList.moveToNext()){
                cartItems.add(new cartItems(productsList.getString(1),productsList.getString(6),productsList.getString(7),productsList.getInt(4),productsList.getInt(2)));
            }
            checkOutItems.setLayoutManager(new LinearLayoutManager(this));
            checkOutItems.setAdapter(new purchased_item_adapter(cartItems,this));
            int totalAmount=new dbhelper(this).getTotalOfAmount(FirebaseAuth.getInstance().getCurrentUser().getUid());
            int totalItems=new dbhelper(this).get_num_of_rows(FirebaseAuth.getInstance().getCurrentUser().getUid());
            subTotal.setText("Rs "+String.valueOf(totalAmount));
            total.setText("Rs "+String.valueOf(totalAmount));
            totalNumberOfItems.setText(String.valueOf(totalItems));
        }
    }

    @Override
    public void locationOn() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SETTING_REQUEST_CODE:
                easyWayLocation.onActivityResult(resultCode);
                break;
        }
    }

    @Override
    public void currentLocation(Location location) {
       if(location!=null){
           latitude=location.getLatitude();
           longitude=location.getLongitude();
       }
    }

    @Override
    public void locationCancelled() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        easyWayLocation.endUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        easyWayLocation.endUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    easyWayLocation.startLocation();
                    if(latitude!=0.0&&longitude!=0.0){
                        try{
                            Geocoder g=new Geocoder(billing.this,Locale.getDefault());
                            List<Address> addresses= g.getFromLocation(latitude,longitude,1);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
                            String formattedDate = df.format(c);
                            Firebase_Operations.PlaceOrders(billing.this,new Orders(user.getName(),addresses.get(0).getAddressLine(0),user.getEmail(),user.getPhone(),latitude,longitude,cartItems,"unconfirmed",FirebaseAuth.getInstance().getCurrentUser().getUid(),formattedDate,new dbhelper(billing.this).getTotalOfAmount(FirebaseAuth.getInstance().getCurrentUser().getUid())),easyWayLocation);
                            easyWayLocation.endUpdates();
                        }catch(Exception e){
                            Toast.makeText(billing.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }  else {
                    Toast.makeText(billing.this,"We need acess to location to place your order",Toast.LENGTH_LONG).show();
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
        }
    }


}
