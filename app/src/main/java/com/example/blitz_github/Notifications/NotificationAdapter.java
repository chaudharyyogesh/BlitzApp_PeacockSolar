package com.example.blitz_github.Notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blitz_github.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
// firebase recycler adapter
public class NotificationAdapter extends FirestoreRecyclerAdapter<notification,NotificationAdapter.NotificationHolder> {

    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<notification> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationHolder holder, int i, @NonNull notification notification) {
        holder.textViewtitle.setText(notification.getTitle());
        holder.textViewmessage.setText(notification.getMessage());
        holder.textViewtime.setText(notification.getTime());
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notification,
                parent,false);
        return new NotificationHolder(v);

    }

    public void deleteItem(int position){// delete item
        getSnapshots().getSnapshot(position).getReference().delete();
    }



    class NotificationHolder extends RecyclerView.ViewHolder{

        TextView textViewtitle;
        TextView textViewmessage;
        TextView textViewtime;


        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            textViewtitle=itemView.findViewById(R.id.title);
            textViewmessage=itemView.findViewById(R.id.message);
            textViewtime=itemView.findViewById(R.id.notification_time);
        }
    }
}