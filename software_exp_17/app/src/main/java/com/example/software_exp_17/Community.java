package com.example.software_exp_17;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.software_exp_17.bindingAdapter.MainAdapter;
import com.example.software_exp_17.utils.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;

public class Community extends AppCompatActivity {
    private static final String TAG = "Community";
    private FirebaseUser user;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    //private RecyclerView.Adapter mAdapter;
    //private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        Button sortBtn = (Button) findViewById(R.id.sortBtn);
        if(MainPlan.searchInt == 1){
            sortBtn.setText("최신순");
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("postlist")
                .orderBy(MainPlan.search, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<PostInfo> postList = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                postList.add(new PostInfo(
                                        document.getData().get("totaltime").toString(),
                                        document.getData().get("plan").toString(),
                                        document.getData().get("contents").toString(),
                                        document.getData().get("publisher").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        (ArrayList<String>) document.getData().get("heart"),
                                        (long) document.getData().get("heartcount"),
                                        document.getId()
                                ));


                            }
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Community.this));

                            RecyclerView.Adapter mAdapter = new MainAdapter(Community.this, postList);
                            recyclerView.setAdapter(mAdapter);


                        } else {
                            //
                        }
                    }
                });

        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.sortBtn).setOnClickListener(onClickListener);
        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
    }

    protected void onResume() {
        super.onResume();

        if (user != null) {
            CollectionReference collectionReference = db.collection("postlist");
            collectionReference
                    .orderBy(MainPlan.search, Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<PostInfo> postList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    postList.add(new PostInfo(
                                            document.getData().get("totaltime").toString(),
                                            document.getData().get("plan").toString(),
                                            document.getData().get("contents").toString(),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createdAt").getTime()),
                                            (ArrayList<String>) document.getData().get("heart"),
                                            (long) document.getData().get("heartcount"),
                                            document.getId()
                                    ));
                                }
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Community.this));

                                RecyclerView.Adapter mAdapter = new MainAdapter(Community.this, postList);
                                recyclerView.setAdapter(mAdapter);
                            }
                        }
                    });


        }
    }



    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.floatingActionButton:
                    startActivity(WritePostActivity.class);
                    break;
                case R.id.sortBtn:
                    if(MainPlan.searchInt == 0){
                        MainPlan.searchInt = 1; // 0은 최신순 1은 ㅇ추천순
                        MainPlan.search = "heartcount";
                    }else {
                        MainPlan.searchInt = 0;
                        MainPlan.search = "createdAt";
                    }
                    finish();
                    startActivity(new Intent(Community.this, Community.class));
                    break;
                default:
                    break;
            }
        }
    };

    private void startActivity(Class c){
        Intent intent = new Intent(this, c);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}