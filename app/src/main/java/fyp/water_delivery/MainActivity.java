package fyp.water_delivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import fyp.water_delivery.Model.Users;
import fyp.water_delivery.Model.dbhelper;
import fyp.water_delivery.R;

public class MainActivity extends AppCompatActivity {
    RecyclerView productList;
    NavigationView nv;
    SharedPreferences prefs;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    TextView numberofItems;
    Users u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        u=new Gson().fromJson(prefs.getString("user_info",null), Users.class);
        nv=findViewById(R.id.nav_view);
        View header = nv.inflateHeaderView(R.layout.navbarheader);
        TextView name=header.findViewById(R.id.name);
        TextView email=header.findViewById(R.id.email);
        ImageView img=header.findViewById(R.id.letterimg);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(u.getName().substring(0, 1), color1);
        img.setImageDrawable(drawable);
        name.setText(u.getName());
        email.setText(u.getEmail());
        nv.inflateMenu(R.menu.menu_main);
        numberofItems = (TextView) nv.getMenu().findItem(R.id.action_cart).getActionView();
        drawerLayout=findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        ) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                numberofItems.setText(String.valueOf(new dbhelper(MainActivity.this).get_num_of_rows(FirebaseAuth.getInstance().getCurrentUser().getUid())));
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        productList=findViewById(R.id.products_list);
        productList.setLayoutManager(new LinearLayoutManager(this));
        Firebase_Operations.getProducts(MainActivity.this,productList);
        actionBarDrawerToggle.syncState();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.action_cart) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startActivity(new Intent(MainActivity.this, shopping_cart.class));
                }else  if(id==R.id.action_orders){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startActivity(new Intent(MainActivity.this,orders_list_page.class));
                }else if(id==R.id.action_logout){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    prefs.edit().remove("user_info").apply();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,Selection.class));
                    finish();
                }
                return true;
            }
        });
    }
}
