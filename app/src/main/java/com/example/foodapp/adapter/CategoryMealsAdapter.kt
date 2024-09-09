package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealItemBinding
import com.example.foodapp.pojo.MealsBycategory

class CategoryMealsAdapter():RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {
    private var mealsList = ArrayList<MealsBycategory>()

    inner class CategoryMealsViewHolder(val binding: MealItemBinding):RecyclerView.ViewHolder(binding.root)

    fun setMealsList(mealsList: List<MealsBycategory>){
        this.mealsList = mealsList as ArrayList<MealsBycategory>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
       return CategoryMealsViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
       Glide.with(holder.itemView)
           .load(mealsList[position].strMealThumb)
           .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal

    }
}