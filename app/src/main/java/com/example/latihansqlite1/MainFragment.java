package com.example.latihansqlite1;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener,RecyclerviewAdapter.OnUserClickListener{
    RecyclerView recyclerView;
    EditText edtName,edtAge;
    Button btnSubmit;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    List<PersonBean> listPersonInfo;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnsubmit){
            DatabaseHelper db = new DatabaseHelper(context);
            PersonBean currentPerson = new PersonBean();
            String btnStatus=btnSubmit.getText().toString();
            if(btnStatus.equals("Submit")) {
                currentPerson.setName(edtName.getText().toString());
                currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                db.insert(currentPerson);
            }
            if(btnStatus.equals("Update")){
                currentPerson.setName(edtName.getText().toString());
                currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                db.update(currentPerson);
            }
            setupRecyclerView();
            edtName.setText("");
            edtAge.setText("");
            edtName.setFocusable(true);
            btnSubmit.setText("Submit");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getActivity();
        recyclerView=view.findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        edtName=view.findViewById(R.id.edtname);
        edtAge=view.findViewById(R.id.edtage);
        btnSubmit=view.findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(this);
        setupRecyclerView();
    }
    private void setupRecyclerView() {
        DatabaseHelper db=new DatabaseHelper(context);
        listPersonInfo = db.selectUserData();
        RecyclerviewAdapter adapter = new RecyclerviewAdapter(context,listPersonInfo,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onUserClick(PersonBean currentPerson, String action) {
        if(action.equals("Edit")){

            edtName.setText(currentPerson.getName());
            edtName.setFocusable(false);
            edtAge.setText(currentPerson.getAge()+"");
            btnSubmit.setText("Update");
        }
        if(action.equals("Delete")){
            DatabaseHelper db=new DatabaseHelper(context);
            db.delete(currentPerson.getName());
            setupRecyclerView();
        }
    }

}
