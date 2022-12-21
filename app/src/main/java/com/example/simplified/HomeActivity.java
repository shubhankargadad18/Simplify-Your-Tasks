package com.example.simplified;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplified.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab_btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private EditText titleUp;
    private EditText noteUp;
    private Button btnDeleteUp;
    private Button btnUpdateUp;
    private String title;
    private String note;
    private String post_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uId);
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        fab_btn=findViewById(R.id.fab_btn);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                View myView = inflater.inflate(R.layout.custominputfield,null);
                myDialog.setView(myView);
                AlertDialog dialog = myDialog.create();
                EditText title = myView.findViewById(R.id.edt_title);
                EditText note = myView.findViewById(R.id.edt_note);
                Button btn_save = myView.findViewById(R.id.btn_save);
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mTitle = title.getText().toString().trim();
                        String mNote = note.getText().toString().trim();
                        if(TextUtils.isEmpty(mTitle)){
                            title.setError("Required...");
                        }
                        if(TextUtils.isEmpty(mNote)){
                            title.setError("Required...");
                        }
                        String id = mDatabase.push().getKey();
                        String datee = DateFormat.getDateInstance().format(new Date());
                        Data data = new Data(mTitle,mNote,datee,id);
                        mDatabase.child(id).setValue(data);
                        Toast.makeText(getApplicationContext(),"Data Insert",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.item_data,
                        MyViewHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());

                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key=getRef(position).getKey();
                        title=model.getTitle();
                        note=model.getNote();


                        updateData();

                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView=itemView;
        }
        public void setTitle(String title){
            TextView mTitle = myView.findViewById(R.id.title);
            mTitle.setText(title);
        }
        public void setNote(String note){
            TextView mNote = myView.findViewById(R.id.note);
            mNote.setText(note);
        }
        public void setDate(String date){
            TextView mDate = myView.findViewById(R.id.date);
            mDate.setText(date);
        }
    }
    public void updateData(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);

        View myview=inflater.inflate(R.layout.updateinputfield,null);
        mydialog.setView(myview);

        final AlertDialog dialog=mydialog.create();

        titleUp=myview.findViewById(R.id.edt_title_upd);
        noteUp=myview.findViewById(R.id.edt_note_upd);

        titleUp.setText(title);
        titleUp.setSelection(title.length());

        noteUp.setText(note);
        noteUp.setSelection(note.length());


        btnDeleteUp=myview.findViewById(R.id.btn_delete_upd);
        btnUpdateUp=myview.findViewById(R.id.btn_update_upd);

        btnUpdateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title=titleUp.getText().toString().trim();
                note=noteUp.getText().toString().trim();

                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(title,note,mDate,post_key);
                mDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        btnDeleteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}