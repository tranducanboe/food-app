package com.example.foodapp.videomodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.pojo.MealsByCategoryList
import com.example.foodapp.pojo.MealsBycategory
import com.example.foodapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsVideModel: ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealsBycategory>>()
    fun getMealByCategory(categoryName: String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object: Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let {mealsList->
                    mealsLiveData.postValue(mealsList.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("CategoryMealsViewModel", t.message.toString())
            }

        } )
    }
    fun observerMealsLiveData(): LiveData<List<MealsBycategory>>{
        return mealsLiveData
    }
}