package com.profoundtechs.biblequizamharic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder> {

    public interface ItemClickListener {
        void onItemClick(Category category);
    }

    private List<Category> categoryList;
    private ItemClickListener listener;

    public CategoryAdapter(List<Category> categoryList,ItemClickListener listener){
        this.categoryList = categoryList;
        this.listener = listener;
    }


    @Override
    public CategoryAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category_view,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.ItemViewHolder holder, int position) {

        holder.bind(categoryList.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCategoryTitle;
        public TextView tvCategoryMaxScore;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tvCategoryTitle = itemView.findViewById(R.id.tvCategoryTitle);
            tvCategoryMaxScore = itemView.findViewById(R.id.tvCategoryMaxScore);
        }

        public void bind(final Category category, final ItemClickListener listener) {
            tvCategoryTitle.setText(category.getName());
            tvCategoryMaxScore.setText("ከፍተኛ ውጤት: " + category.getHighScore());
            itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(category);
            }
        });
        }
    }
}
