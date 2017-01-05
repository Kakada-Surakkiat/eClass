package kh.edu.rupp.ckcc.eclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kh.edu.rupp.ckcc.eclass.R;
import kh.edu.rupp.ckcc.eclass.app.AppConstant;
import kh.edu.rupp.ckcc.eclass.vo.Category;

/**
 * eClass
 * Created by leapkh on 5/1/17.
 */

public class AddCategoryActivity extends ToolbarActivity implements View.OnClickListener, ValueEventListener, ChildEventListener {

    private EditText etxtName;
    private EditText etxtDescription;

    private DatabaseReference categoryRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initDatabaseReference();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add){
            addCategory();
        }
    }

    private void initView(){
        setContentView(R.layout.activity_add_category);
        setToolbarTitle(R.string.add_category);
        etxtName = (EditText)findViewById(R.id.etxt_name);
        etxtDescription = (EditText)findViewById(R.id.etxt_description);
        findViewById(R.id.btn_add).setOnClickListener(this);
    }

    private void initDatabaseReference(){
        categoryRef = FirebaseDatabase.getInstance().getReference(AppConstant.REF_CATEGORIES);
    }

    private void addCategory(){
        categoryRef.orderByChild(AppConstant.REF_NAME).equalTo(etxtName.getText().toString().trim());
        categoryRef.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        boolean isCategoryExist = false;
        String categoryName = etxtName.getText().toString().trim();
        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
            Category category = snapshot.getValue(Category.class);
            if(category.getName().equals(categoryName)){
                isCategoryExist = true;
                break;
            }
        }
        if(isCategoryExist){
            showLongToast(R.string.msg_item_exists);
        }else{
            Category category = new Category(categoryName, etxtDescription.getText().toString());
            categoryRef.push().setValue(category);
            categoryRef.addChildEventListener(this);
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        showLongToast(R.string.msg_add_success);
        etxtName.getText().clear();
        etxtDescription.getText().clear();
        etxtName.requestFocus();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
