package com.b3sk.fodmaper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.b3sk.fodmaper.helpers.FoodPhotoRetriever;
import com.b3sk.fodmaper.R;
import com.b3sk.fodmaper.helpers.MyApplication;
import com.b3sk.fodmaper.model.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joopk on 3/23/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final List<Food> mFoods;
    private final LayoutInflater mInflater;

    public RecyclerViewAdapter(Context context, List<Food> foods) {
        mFoods = new ArrayList<>(foods);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View foodView = mInflater.inflate(R.layout.card_view_fodmap, parent, false);
        return new RecyclerViewHolder(foodView);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.fodmapName.setText(mFoods.get(position).getName());
        holder.fodmapInfo.setText(mFoods.get(position).getInfo());
        holder.fodmapPhoto.setImageResource(FoodPhotoRetriever.getFoodPhoto(
                mFoods.get(position).getId()));

        int id = mFoods.get(position).getId();
        if(id > 1000) {
            holder.fodmapPhoto.setContentDescription(MyApplication.getAppContext().getString(
                    R.string.fodmap_description));
        }else {
            holder.fodmapPhoto.setContentDescription(MyApplication.getAppContext().getString(
                    R.string.friendly_description));
        }


    }

    @Override
    public int getItemCount() {
        return this.mFoods.size();
    }

    public void animateTo(List<Food> foods) {
        applyAndAnimateRemovals(foods);
        applyAndAnimateAdditions(foods);
        applyAndAnimateMovedItems(foods);
    }

    private void applyAndAnimateRemovals(List<Food> newFoods) {
        for (int i = mFoods.size() - 1; i >= 0; i--) {
            final Food food = mFoods.get(i);
            if (!newFoods.contains(food)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Food> newFoods) {
        for (int i = 0, count = newFoods.size(); i < count; i++) {
            final Food food = newFoods.get(i);
            if (!mFoods.contains(food)) {
                addItem(i, food);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Food> newFoods) {
        for (int toPosition = newFoods.size() - 1; toPosition >= 0; toPosition--) {
            final Food food = newFoods.get(toPosition);
            final int fromPosition = mFoods.indexOf(food);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Food removeItem(int position) {
        final Food food = mFoods.remove(position);
        notifyItemRemoved(position);
        return food;
    }

    public void addItem(int position, Food food) {
        mFoods.add(position, food);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Food food = mFoods.remove(fromPosition);
        mFoods.add(toPosition, food);
        notifyItemMoved(fromPosition, toPosition);
    }

}