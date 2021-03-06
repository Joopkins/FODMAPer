package com.b3sk.fodmaper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.b3sk.fodmaper.MyApplication;
import com.b3sk.fodmaper.R;
import com.b3sk.fodmaper.adapters.RecyclerViewAdapter;
import com.b3sk.fodmaper.model.Food;
import com.b3sk.fodmaper.model.FoodRepo;
import com.b3sk.fodmaper.presenter.FriendlyPresenterImpl;
import com.b3sk.fodmaper.presenter.PresenterManager;
import com.b3sk.fodmaper.view.FriendlyView;

import java.util.List;

import javax.inject.Inject;


/**
 * A placeholder fragment containing a simple view.
 */
public class FodmapFriendlyFragment extends Fragment implements View.OnClickListener, FriendlyView {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */


    public FodmapFriendlyFragment() {
    }

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private FriendlyPresenterImpl presenter;
    private ImageView fruit;
    private ImageView vegi;
    private ImageView protein;
    private ImageView other;
    private ImageView grain;

    @Inject FoodRepo foodRepo;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FodmapFriendlyFragment newInstance() {
        return new FodmapFriendlyFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fodmap_friendly_list, container, false);

        if(savedInstanceState == null) {
            presenter = new FriendlyPresenterImpl(foodRepo);
        }else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
            if(presenter == null) {
                presenter = new FriendlyPresenterImpl(foodRepo);
            }
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.fodmap_recycler);
        protein = (ImageView) rootView.findViewById(R.id.protein_button);
        protein.setOnClickListener(this);
        vegi = (ImageView) rootView.findViewById(R.id.vegitable_button);
        vegi.setOnClickListener(this);
        fruit = (ImageView) rootView.findViewById(R.id.fruit_button);
        fruit.setOnClickListener(this);
        other = (ImageView) rootView.findViewById(R.id.other_button);
        other.setOnClickListener(this);
        grain = (ImageView) rootView.findViewById(R.id.grain_button);
        grain.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.protein_button:
                presenter.onProteinClicked();
                break;
            case R.id.vegitable_button:
                presenter.onVegiClicked();
                break;
            case R.id.fruit_button:
                presenter.onFruitClicked();
                break;
            case R.id.other_button:
                presenter.onOtherClicked();
                break;
            case R.id.grain_button:
                presenter.onGrainClicked();
                break;
        }
    }

    @Override
    public void animateToFilter(List<Food> foodList) {
        recyclerViewAdapter.animateTo(foodList);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void bindFoods(List<Food> foodList) {
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), foodList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onFruitClicked(boolean clicked) {
        if(clicked) {
            fruit.setImageResource(R.drawable.fruit_icon_clicked);
        }else {
            fruit.setImageResource(R.drawable.fruit_icon);
        }
    }

    @Override
    public void onVegiClicked(boolean clicked) {
        if(clicked) {
            vegi.setImageResource(R.drawable.vegi_icon_clicked);
        }else {
            vegi.setImageResource(R.drawable.vegi_icon);
        }
    }

    @Override
    public void onProteinClicked(boolean clicked) {
        if(clicked) {
            protein.setImageResource(R.drawable.protein_icon_clicked);
        }else {
            protein.setImageResource(R.drawable.protein_icon);
        }
    }

    @Override
    public void onOtherClicked(boolean clicked) {
        if(clicked) {
            other.setImageResource(R.drawable.other_icon_clicked);
        }else {
            other.setImageResource(R.drawable.other_icon);
        }
    }

    @Override
    public void onGrainClicked(boolean clicked) {
        if(clicked) {
            grain.setImageResource(R.drawable.grain_icon_clicked);
        }else {
            grain.setImageResource(R.drawable.grain_icon);
        }
    }
}