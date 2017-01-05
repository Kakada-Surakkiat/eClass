package kh.edu.rupp.ckcc.eclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kh.edu.rupp.ckcc.eclass.R;
import kh.edu.rupp.ckcc.eclass.app.AppConstant;
import kh.edu.rupp.ckcc.eclass.utility.MyNetwork;
import kh.edu.rupp.ckcc.eclass.vo.Course;

/**
 * eClass
 * Created by leapkh on 5/1/17.
 */

public class CourseActivity extends ToolbarActivity implements View.OnClickListener {

    private DatabaseReference userCourseRef;
    private Course course;
    private String courseId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course);
        String serializedCourse = getIntent().getStringExtra(CategoryActivity.EXTRA_SERIALIZE_COURSE);
        course = MyNetwork.getInstance(this).getGson().fromJson(serializedCourse, Course.class);
        courseId = getIntent().getStringExtra(CategoryActivity.EXTRA_COURSE_KEY);
        setToolbarTitle(course.getName());

        TextView txtName = (TextView)findViewById(R.id.txt_name);
        txtName.setText(course.getName());
        TextView txtDescription = (TextView)findViewById(R.id.txt_description);
        txtDescription.setText(course.getDescription());
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_register){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userCourseRef = FirebaseDatabase.getInstance().getReference(AppConstant.REF_USER_COURSE);
            userCourseRef.child(courseId).child(AppConstant.REF_USER).setValue(user.getUid());
            userCourseRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    showLongToast(R.string.msg_add_success);
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
            });
        }
    }
}
