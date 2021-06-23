package com.example.software_exp_17;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.software_exp_17.utils.PostInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.NullValue;

import java.io.DataOutput;
import java.util.ArrayList;
import java.util.Date;

public class WritePostActivity extends AppCompatActivity {
    private static final String TAG = "WritePostActivity";
    private FirebaseUser user;

    public String totalstudytime = stopwatch.studytime;
    public int progress = MainPlan.progress;
    private String plantxt = String.valueOf(progress);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.writepost);

        TextView timeTextView = (TextView) findViewById(R.id.timeTextView);
        timeTextView.setText(totalstudytime);

        TextView planTextView = (TextView) findViewById(R.id.planTextView);
        planTextView.setText("계획달성률 : " + plantxt + "%");

        findViewById(R.id.check).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.check:
                    upload();
                    break;
            }
        }
    };

    private void upload(){
        final String time = totalstudytime;
        final String plan = plantxt;
        final String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();

        if(contents.length() > 0){
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final DocumentReference documentReference = firebaseFirestore.collection("postlist").document();
            user = FirebaseAuth.getInstance().getCurrentUser();
            PostInfo postInfo = new PostInfo(time, plan, contents, user.getUid(), new Date(), new ArrayList<String>(), 0);
            Log.d(TAG, "before uploader");
            uploader(documentReference, postInfo);

        } else{
            startToast("글을 입력해 주세요.");
        }
    };

    private void uploader(DocumentReference documentReference, PostInfo postInfo){
        documentReference.set(postInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("업로드 완료");
                        startmyActivity(Community.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error writing document", e);
                    }
                });

    }

    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    private void startmyActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}