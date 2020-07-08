package com.example.blitz_github.Notifications;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blitz_github.Notifications.widgets.BucketRecyclerView;
import com.example.blitz_github.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Notification_activity extends AppCompatActivity {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference notebookRef=db.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("Notifications");//1

    private NotificationAdapter adapter;
    View mEmptyView;//1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_recycler_view);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query=notebookRef.orderBy("time",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<notification> options=new FirestoreRecyclerOptions.Builder<notification>()
                .setQuery(query,notification.class)
                .build();

        adapter=new NotificationAdapter(options);

        BucketRecyclerView recyclerView=findViewById(R.id.recycler_view_notification);//1
       // RecyclerView recyclerView=findViewById(R.id.recycler_view_notification);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mEmptyView=findViewById(R.id.empty_drops);//1
        recyclerView.showIfEmpty(mEmptyView);

        //swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                adapter.deleteItem(viewHolder.getAdapterPosition());
                Toast.makeText(getApplicationContext(),"Notification deleted",Toast.LENGTH_SHORT).show();//
            }
        }).attachToRecyclerView(recyclerView);// attach the recycler view to touch helper



    }
    //tell our adapter when test start listening database changes and test will stop

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
