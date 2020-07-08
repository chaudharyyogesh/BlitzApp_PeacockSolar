package com.example.blitz_github;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blitz_github.Account.Complete_Registration;
import com.example.blitz_github.Account.login_register;
import com.example.blitz_github.Notifications.Notification_activity;
import com.example.blitz_github.Utils.Product;
import com.example.blitz_github.Utils.ProductAdapter;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home_activity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList;
    TextView name;
    ImageView profilePic;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activity);

        productList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        name = (TextView) findViewById(R.id.textView2);
        profilePic = (ImageView) findViewById(R.id.imageView3);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("name"));
                if(documentSnapshot.getString("Image_url")!=null)
                {
                    Picasso.get().load(documentSnapshot.getString("Image_url")).into(profilePic);
                }
            }
        });


        productList.add(
                new Product(
                        1,
                        "1.Career Counselling\n2.Placement Services\n3.Resume Making\n4.Mock Interview\n5.HR Sessions",
                        "",
                        R.drawable.college));

        productList.add(
                new Product(
                        1,
                        "1.Digital Marketing\n2.SEO\n3.Excel\n4.MySQL\n5.Python",
                        "",
                        R.drawable.skill));

        productList.add(
                new Product(
                        1,
                        "1.Talent Acquisition\n2.Talent Retention\n3.Exit Formalities\n4.Performance Appraisal\n5.Attendance Management\n6.Training and Development",
                        "",
                        R.drawable.startup));

        adapter = new ProductAdapter(this,productList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_screen_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_update:
                Intent intent2=new Intent(this,Complete_Registration.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.about_us:
                Intent intent = new Intent(this,About.class);
                startActivity(intent);
                break;
            case R.id.contact:
                Intent intent1 = new Intent(this,Contact.class);
                startActivity(intent1);
                break;
            case R.id.log_out:
                Logout();
                break;
            case R.id.notifications:
                startActivity(new Intent(this,Notification_activity.class));
                break;
        }
            return true;
    }
    private void Logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth!=null)
            FirebaseAuth.getInstance().signOut();

        GoogleSignIn.getClient(this,new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();

        startActivity(new Intent(this,login_register.class));
        finish();
    }

    public void recyclerViewToast(View v)
    {
        Toast.makeText(this,"This functionality will be added soon.",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth = FirebaseAuth.getInstance();//For custom Login
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);//For Google Login
        AccessToken accessToken = AccessToken.getCurrentAccessToken();//For facebook Login

        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(firebaseAuth.getCurrentUser()==null&&isLoggedIn&&account.getIdToken()==null)
        {
            startActivity(new Intent(this, login_register.class));
            finish();
        }
        }

}