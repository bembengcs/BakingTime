package net.mavenmobile.bakingtime.rest;

import net.mavenmobile.bakingtime.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by bembengcs on 8/19/2017.
 */

public interface ApiInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getAllRecipes();
}
