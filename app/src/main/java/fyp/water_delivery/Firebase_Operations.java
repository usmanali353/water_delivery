package fyp.water_delivery;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easywaylocation.EasyWayLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

import fyp.water_delivery.Adapter.orders_list_adapter;
import fyp.water_delivery.Model.Bottles;
import fyp.water_delivery.Model.Users;
import fyp.water_delivery.Adapter.item_list_adapter;
import fyp.water_delivery.Model.Orders;
import fyp.water_delivery.Model.dbhelper;

public class Firebase_Operations {
    public static void SignIn(final Context context, String email, String password){
        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Authenticating User....");
        pd.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                pd.dismiss();
                                if(documentSnapshot.exists()){
                                    Toast.makeText(context,"Login Sucess",Toast.LENGTH_LONG).show();
                                    Users u=documentSnapshot.toObject(Users.class);
                                    prefs.edit().putString("user_info",new Gson().toJson(u)).apply();
                                    context.startActivity(new Intent(context, MainActivity.class));
                                    ((AppCompatActivity) context).finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void SignUp (final Context context, final String email, final String Password, final String phone, final String name){
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Registering User...");
        pd.show();
       FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Log.e("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                   Users u=new Users(name,email,Password,phone);
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        if(task.isSuccessful()){
                           Toast.makeText(context,"Registration Sucess",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               pd.dismiss();
               Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
           }
       });
    }
    public static void getProducts(final Context context, final RecyclerView productList){
        final ArrayList<Bottles> bottles=new ArrayList<>();
        final ArrayList<String> bottleId=new ArrayList<>();
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Fetching Bottles...");
        pd.show();
        FirebaseFirestore.getInstance().collection("Products").whereGreaterThan("quantity",0).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pd.dismiss();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for(int i=0;i<queryDocumentSnapshots.getDocuments().size();i++){
                        bottles.add(queryDocumentSnapshots.getDocuments().get(i).toObject(Bottles.class));
                        bottleId.add(queryDocumentSnapshots.getDocuments().get(i).getId());
                    }
                    productList.setAdapter(new item_list_adapter(bottles,context,bottleId));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void PlaceOrders(Context context, Orders order, EasyWayLocation easyWayLocation){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Placing Order....");
        pd.show();
        FirebaseFirestore.getInstance().collection("Orders").document().set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pd.dismiss();
                    easyWayLocation.endUpdates();
                    Toast.makeText(context,"Order is Placed",Toast.LENGTH_LONG).show();
                    for(int i=0;i<order.getOrderedItems().size();i++){
                        decrementQuantity(context,order.getOrderedItems().get(i).getQuantity(),order.getOrderedItems().get(i).getProductid());
                    }
                    new dbhelper(context).delete_all(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    context.startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    ((AppCompatActivity)context).finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void getOrders(Context context,RecyclerView orders_list){
        ArrayList<Orders> orders=new ArrayList<>();
        ArrayList<String> orderId=new ArrayList<>();
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Fetching your Orders...");
        pd.show();
        FirebaseFirestore.getInstance().collection("Orders").whereEqualTo("userId",FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pd.dismiss();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                   for(int i=0;i<queryDocumentSnapshots.getDocuments().size();i++){
                       orders.add(queryDocumentSnapshots.getDocuments().get(i).toObject(Orders.class));
                       orderId.add(queryDocumentSnapshots.getDocuments().get(i).getId());
                   }
                   orders_list.setAdapter(new orders_list_adapter(orders,orderId,context));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void decrementQuantity(Context context,int quantity,String productId){
        FirebaseFirestore.getInstance().collection("Products").document(productId).update("quantity", FieldValue.increment(-quantity)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void DeleteOrder(Context context,String orderId){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Canceling Order...");
        pd.show();
        FirebaseFirestore.getInstance().collection("Orders").document(orderId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(context,"Order Canceled",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
