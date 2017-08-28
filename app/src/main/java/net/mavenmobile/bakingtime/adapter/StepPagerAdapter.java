package net.mavenmobile.bakingtime.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.mavenmobile.bakingtime.fragment.RecipeDetailFragment;
import net.mavenmobile.bakingtime.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bembengcs on 8/25/2017.
 */

public class StepPagerAdapter extends FragmentPagerAdapter {

    private List<Step> mStepList;
    private Context mContext;

    public StepPagerAdapter(FragmentManager fm, List<Step> stepList, Context context) {
        super(fm);
        this.mStepList = new ArrayList<>();
        this.mContext = context;
        this.mStepList.addAll(stepList);
    }

    @Override
    public Fragment getItem(int position) {
        Step step = mStepList.get(position);
        String videoURL = step.getVideoURL();
        String description = step.getDescription();
        return RecipeDetailFragment.newInstance(videoURL, description);
    }

    @Override
    public int getCount() {
        return mStepList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Step step = mStepList.get(position);
        return step.getShortDescription();
    }
}
