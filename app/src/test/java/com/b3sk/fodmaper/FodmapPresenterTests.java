package com.b3sk.fodmaper;

import com.b3sk.fodmaper.data.foodLoader;
import com.b3sk.fodmaper.model.Food;
import com.b3sk.fodmaper.model.FoodRepository;
import com.b3sk.fodmaper.presenter.FodmapPresenter;
import com.b3sk.fodmaper.view.FodmapView;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Joopkins on 5/14/16.
 */
public class FodmapPresenterTests {


    FodmapPresenter fodmapPresenter;
    FodmapView fodmapView;
    List<Food> foods;

    @Before
    public void setup() {
        foods = new ArrayList<>();
        foods.add(new Food("FoodA", 1, 1, 0, 0, 0));
        foods.add(new Food("FoodB", 1, 1, 0, 0, 0));
        fodmapPresenter = new FodmapPresenter();
        fodmapView = mock(FodmapView.class);
    }

    @Test
    public void noInteractionWithViewOnQueryNullView() {
        fodmapPresenter.setModel(foods);
        fodmapPresenter.onQueryTextChanged("string");
        verifyNoMoreInteractions(fodmapView);
    }

    @Test
    public void noInteractionWithViewOnQueryNullModel() {
        fodmapPresenter.onQueryTextChanged("string");
        verifyZeroInteractions(fodmapView);
    }


    @Test
    public void verifyOnDataLoadedSetsModel() {
        FodmapPresenter fPresenter = mock(FodmapPresenter.class);
        doCallRealMethod().when(fPresenter).onDataLoaded(anyListOf(Food.class), anyString());
        doCallRealMethod().when(fPresenter).setModel(anyListOf(Food.class));
        fPresenter.onDataLoaded(foods, "string");
        verify(fPresenter).setModel(foods);
    }


}