package kh.edu.rupp.ckcc.eclass.fragment;

import android.app.Fragment;
import android.content.Intent;
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
import kh.edu.rupp.ckcc.eclass.activity.AddCategoryActivity;
import kh.edu.rupp.ckcc.eclass.activity.CategoryActivity;
import kh.edu.rupp.ckcc.eclass.vo.Category;

/**
 * eClass
 * Created by leapkh on 3/1/17.
 */

public class CategoriesFragment extends Fragment implements View.OnClickListener {

    private final String REF_CATEGORIES = "categories";
    public static final String EXTRA_CATEGORY_KEY = "category-key";
    public static final String EXTRA_CATEGORY_NAME = "category-name";

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
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, final int position) {
                viewHolder.txtName.setText(model.getName());
                viewHolder.txtDescription.setText(model.getDescription());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = getRef(position).getKey();
                        Intent intent = new Intent(getActivity(), CategoryActivity.class);
                        intent.putExtra(EXTRA_CATEGORY_KEY, key);
                        intent.putExtra(EXTRA_CATEGORY_NAME, model.getName());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.btn_add).setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add){
            startActivity(new Intent(getActivity(), AddCategoryActivity.class));
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtDescription;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView)itemView.findViewById(R.id.txt_name);
            txtDescription = (TextView)itemView.findViewById(R.id.txt_description);
        }
    }



}
