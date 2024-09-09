package com.example.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.bumptech.glide.Glide
import com.example.foodapp.activitys.CategoryMealsActivity
import com.example.foodapp.activitys.MainActivity
import com.example.foodapp.activitys.MealActivity
import com.example.foodapp.adapter.CategoryAdapter
import com.example.foodapp.adapter.MostPopularAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.pojo.MealsBycategory
import com.example.foodapp.pojo.Meal

import com.example.foodapp.videomodel.HomeViewModel
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoryAdapter

    companion object{
        const val MEAL_ID = "com.example.foodapp.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodapp.fragments.categoryName"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerview()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomClick()

        viewModel.getPopularItems()
        observerPopularItemsLiveData()
        onPopularItemClick()

        viewModel.getCategories()
        observerCategoriesLiveData()
        prepareCategoriesRecyclerview()

        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category ->
            val intent = Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerview() {
        binding.recViewCategoris.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observerCategoriesLiveData() {
        viewModel.observerCategoriesLiveData().observe(viewLifecycleOwner, Observer{ categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)

            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerview() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observerPopularItemsLiveData() {
        viewModel.observerPopularItemsLiveData().observe(viewLifecycleOwner,Observer
         { mealList->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsBycategory>)
        })
    }

    private fun onRandomClick() {
        binding.randomMealCard.setOnClickListener {
            val i = Intent(activity,MealActivity::class.java)
            i.putExtra(MEAL_ID,randomMeal.idMeal)
            i.putExtra(MEAL_NAME,randomMeal.strMeal)
            i.putExtra(MEAL_THUMB,randomMeal.strMealThumb)

            startActivity(i)

        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLivedata().observe(viewLifecycleOwner, Observer
            { meal ->
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)

                this.randomMeal = meal
            })
    }
}