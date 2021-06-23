package com.example.software_exp_17.bindingAdapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.software_exp_17.R;
import com.example.software_exp_17.utils.PostInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private static final String TAG = "MainAdapter";

    private ArrayList<PostInfo> localDataSet;
    private Activity activity;

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }

        //public TextView getTextView() {
        //    return textView;
        //}
    }

    public MainAdapter(Activity activity, ArrayList<PostInfo> dataSet){
        Log.d(TAG, "init");

        this.activity = activity;
        localDataSet = dataSet;
    }




    // Create new views (invoked by the layout manager)
    @Override
    public MainAdapter.MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        Log.d(TAG, "onCreateViewHolder");

        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_post, viewGroup, false);

        final MainViewHolder mainViewHolder = new MainViewHolder(v);

        return mainViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

        CardView cardView = holder.cardView;

        ImageButton heartimage = (ImageButton) cardView.findViewById(R.id.heartButton);
        ArrayList<String> heartlist = localDataSet.get(position).getHeart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(heartlist.indexOf(user.getUid()) == -1){
            heartimage.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        } else {
            heartimage.setImageResource(R.drawable.ic_baseline_favorite_24);
        }

        TextView hearttextView = cardView.findViewById(R.id.heartTextView);
        hearttextView.setText(heartlist.size() + "개");

        String docId = localDataSet.get(position).getId();

        heartimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(heartlist.indexOf(user.getUid()) == -1){
                    heartimage.setImageResource(R.drawable.ic_baseline_favorite_24);
                    heartlist.add(user.getUid());
                } else {
                    heartimage.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    heartlist.remove(user.getUid());
                }
                hearttextView.setText(heartlist.size() + "개");
                localDataSet.get(position).setHeart(heartlist);
                localDataSet.get(position).setHeartcount(heartlist.size());
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("postlist").document(docId).update("heart",heartlist);
                db.collection("postlist").document(docId).update("heartcount",heartlist.size());

            }
        });

        TextView textView = cardView.findViewById(R.id.titleTextView);
        textView.setText(localDataSet.get(position).getTotaltime());

        TextView progressTextView = cardView.findViewById(R.id.progressTextView);
        progressTextView.setText("계획달성률 : " + localDataSet.get(position).getPlan() + "%");

        TextView createdAtTextView = cardView.findViewById(R.id.createdAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(localDataSet.get(position).getCreatedAt()));

        TextView contentsTextView = cardView.findViewById(R.id.contentsTextView);
        contentsTextView.setText(localDataSet.get(position).getContents());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}