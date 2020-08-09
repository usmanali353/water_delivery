package fyp.water_delivery;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fyp.water_delivery.R;

public class orders_list_page extends AppCompatActivity {
  RecyclerView orders_list;
  SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orders_list=findViewById(R.id.orders_list);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        orders_list.setLayoutManager(new LinearLayoutManager(this));
        Firebase_Operations.getOrders(this, orders_list);
    }

}
