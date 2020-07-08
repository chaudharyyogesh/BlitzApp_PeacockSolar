package com.example.blitz_github.Utils.Project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blitz_github.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

public class ProjectAdapter extends FirestoreRecyclerAdapter<projectModel,ProjectAdapter.ProjectHolder> {

    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<projectModel> options) {
        super(options);
        Log.v("methods","adapter run");

    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.project_cardview,
                parent,false);
        return new ProjectHolder(v);

    }

    @Override
    protected void onBindViewHolder(@NonNull ProjectHolder holder, int i, @NonNull projectModel project) {
        holder.texttitle.setText(project.getTitle());
        holder.textclient.setText(project.getClient());
        holder.textrole.setText(project.getRole());
        holder.textdescription.setText(project.getDescription());
        holder.textstart.setText(project.getStart());
        holder.textend.setText(project.getEnd());
        Log.v("methods",project.getTitle());//
    }



    public void deleteItem(int position){// delete item
        getSnapshots().getSnapshot(position).getReference().delete();
    }



    class ProjectHolder extends RecyclerView.ViewHolder{

        TextView texttitle;
        TextView textclient;

        public TextView getTexttitle() {
            return texttitle;
        }

        public void setTexttitle(TextView texttitle) {
            this.texttitle = texttitle;
        }

        public TextView getTextclient() {
            return textclient;
        }

        public void setTextclient(TextView textclient) {
            this.textclient = textclient;
        }

        public TextView getTextrole() {
            return textrole;
        }

        public void setTextrole(TextView textrole) {
            this.textrole = textrole;
        }

        public TextView getTextdescription() {
            return textdescription;
        }

        public void setTextdescription(TextView textdescription) {
            this.textdescription = textdescription;
        }

        public TextView getTextstart() {
            return textstart;
        }

        public void setTextstart(TextView textstart) {
            this.textstart = textstart;
        }

        public TextView getTextend() {
            return textend;
        }

        public void setTextend(TextView textend) {
            this.textend = textend;
        }

        TextView textrole;
        TextView textdescription;
        TextView textstart;
        TextView textend;




        public ProjectHolder(@NonNull View itemView) {
            super(itemView);
            texttitle=itemView.findViewById(R.id.project_title);
            textclient=itemView.findViewById(R.id.project_client);
            textrole=itemView.findViewById(R.id.project_role);
            textdescription=itemView.findViewById(R.id.project_description);
            textstart=itemView.findViewById(R.id.project_start_date);
            textend=itemView.findViewById(R.id.project_end_date);

        }
    }
}


