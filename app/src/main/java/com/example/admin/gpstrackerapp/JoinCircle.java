package com.example.admin.gpstrackerapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircle extends AppCompatActivity {


    Pinview pinview;
    DatabaseReference reference,currentreference;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id,join_user_id;
    DatabaseReference circlereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        pinview=(Pinview)findViewById(R.id.pinview2);
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("users");
        currentreference=FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        current_user_id=user.getUid();


    }

    public void submitonclick(View v)
    {

        Query query=reference.orderByChild("code").equalTo(pinview.getValue());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    CreateUser createUser =null;
                    for(DataSnapshot childDss : dataSnapshot.getChildren())
                    {
                        createUser= childDss.getValue(CreateUser.class);
                        join_user_id=createUser.userid;
                        circlereference=FirebaseDatabase.getInstance().getReference().child("users")
                                .child(join_user_id).child("circlemembers");


                        CircleJoin circleJoin= new CircleJoin(current_user_id);
                        CircleJoin circleJoin1=new CircleJoin(join_user_id);

                        circlereference.child(user.getUid()).setValue(circleJoin)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(),"user joined circle success",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"circle code invalid",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
