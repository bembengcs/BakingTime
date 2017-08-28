package net.mavenmobile.bakingtime.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.adapter.StepPagerAdapter;
import net.mavenmobile.bakingtime.model.Recipe;
import net.mavenmobile.bakingtime.model.Step;
import net.mavenmobile.bakingtime.rest.ApiClient;
import net.mavenmobile.bakingtime.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailTestActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private ApiInterface apiService;
    private Step step;
    private List<Step> mStepList = new ArrayList<>();
    private Recipe recipe;
    private String TAG = RecipeDetailActivity.class.getSimpleName();
    private FragmentPagerAdapter mAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_test);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        recipe = getIntent().getExtras().getParcelable("recipe");
        mStepList = new ArrayList<>();
        mStepList.addAll(recipe.getSteps());

        Bundle args = new Bundle();
        args.putParcelable("step", step);
        mViewPager.addOnPageChangeListener(this);
        setupValue(step);
    }

    private void setupValue(Step step) {
        this.step = step;
        for (int i = 0; i < mStepList.size(); i++) {
            mStepList.add(recipe.getSteps().get(i));
        }
        setupAdapter(mStepList);
    }

    private void setupAdapter(List<Step> stepList) {
        this.mStepList = stepList;
        StepPagerAdapter adapter = new StepPagerAdapter(getSupportFragmentManager(), mStepList, this);
        mViewPager.setAdapter(adapter);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
