package kh.edu.rupp.ckcc.eclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kh.edu.rupp.ckcc.eclass.R;
import kh.edu.rupp.ckcc.eclass.fragment.CategoriesFragment;
import kh.edu.rupp.ckcc.eclass.listener.RecylerViewItemClickListener;
import kh.edu.rupp.ckcc.eclass.utility.MyNetwork;
import kh.edu.rupp.ckcc.eclass.vo.Course;

/**
 * eClass
 * Created by leapkh on 3/1/17.
 */

public class CategoryActivity extends ToolbarActivity implements RecylerViewItemClickListener {

    public static final String EXTRA_COURSE_KEY = "course-key";
    public static final String EXTRA_SERIALIZE_COURSE = "serialize-course";

    private final String REF_COURSES = "courses";
    private final String REF_CATEGORY = "category";

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> adapter;

    private DatabaseReference rootRef;
    private DatabaseReference coursesRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        String categoryId = getIntent().getStringExtra(CategoriesFragment.EXTRA_CATEGORY_KEY);
        rootRef = FirebaseDatabase.getInstance().getReference();
        coursesRef = rootRef.child(REF_COURSES);

        String categoryName = getIntent().getStringExtra(CategoriesFragment.EXTRA_CATEGORY_NAME);
        setToolbarTitle(categoryName);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>(Course.class, R.layout.listitem_category, CourseViewHolder.class, coursesRef.orderByChild(REF_CATEGORY).equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(CourseViewHolder viewHolder, Course model, int position) {
                viewHolder.setItemClickListener(CategoryActivity.this, getRef(position).getKey());
                viewHolder.txtName.setText(model.getName());
                viewHolder.txtDescription.setText(model.getDescription());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecylerViewItemClickListener(int position, String key) {
        Course course = adapter.getItem(position);
        Intent intent = new Intent(this, CourseActivity.class);
        intent.putExtra(EXTRA_COURSE_KEY, key);
        intent.putExtra(EXTRA_SERIALIZE_COURSE, MyNetwork.getInstance(this).getGson().toJson(course));
        startActivity(intent);
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtName;
        public TextView txtDescription;
        private RecylerViewItemClickListener recylerViewItemClickListener;
        private String key;

        public CourseViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            txtName = (TextView)itemView.findViewById(R.id.txt_name);
            txtDescription = (TextView)itemView.findViewById(R.id.txt_description);
        }

        public void setItemClickListener(RecylerViewItemClickListener listener, String key){
            this.recylerViewItemClickListener = listener;
            this.key = key;
        }

        @Override
        public void onClick(View v) {
            if(recylerViewItemClickListener != null){
                recylerViewItemClickListener.onRecylerViewItemClickListener(getAdapterPosition(), key);
            }
        }
    }

}
