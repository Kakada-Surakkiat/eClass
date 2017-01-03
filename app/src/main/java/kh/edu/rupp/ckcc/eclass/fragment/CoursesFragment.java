package kh.edu.rupp.ckcc.eclass.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kh.edu.rupp.ckcc.eclass.R;
import kh.edu.rupp.ckcc.eclass.vo.Category;

/**
 * eClass
 * Created by leapkh on 3/1/17.
 */

public class CoursesFragment extends Fragment {

    private final String REF_CATEGORIES = "categories";

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    private DatabaseReference rootRef;
    private DatabaseReference categoriesRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootRef = FirebaseDatabase.getInstance().getReference();
        categoriesRef = rootRef.child(REF_CATEGORIES);

        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.listitem_category, CategoryViewHolder.class, categoriesRef) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, int position) {
                viewHolder.txtName.setText(model.getName());
                viewHolder.txtDescription.setText(model.getDescription());
            }
        };
        recyclerView.setAdapter(adapter);

        return view;

    }

    /*
    private void processLoadCategories(){
        rootRef = FirebaseDatabase.getInstance().getReference();
        categoriesRef = rootRef.child(REF_CATEGORIES);
        categoriesRef.addListenerForSingleValueEvent(this);
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("ckcc", "onDataChange");
        for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
            Category category = childSnapshot.getValue(Category.class);
            Log.d("ckcc", "Cat name: " + category.getName());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

*/
    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName;
        public TextView txtDescription;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView)itemView.findViewById(R.id.txt_name);
            txtDescription = (TextView)itemView.findViewById(R.id.txt_description);
        }

    }

}
