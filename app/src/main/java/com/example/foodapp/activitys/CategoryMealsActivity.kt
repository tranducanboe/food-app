package com.example.foodapp.activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapter.CategoryMealsAdapter
import com.example.foodapp.databinding.ActivityCategoryMealsBinding
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.videomodel.CategoryMealsVideModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsVideModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Chuẩn bị RecyclerView
        prepareRecyclerView()

        // Khởi tạo ViewModel để lấy dữ liệu các món ăn theo danh mục
        categoryMealsViewModel = ViewModelProvider(this).get(CategoryMealsVideModel::class.java)

        // Gọi hàm để lấy danh sách các món ăn dựa trên tên danh mục được truyền từ HomeFragment
        categoryMealsViewModel.getMealByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        // Quan sát LiveData để cập nhật giao diện khi dữ liệu thay đổi
        categoryMealsViewModel.observerMealsLiveData().observe(this, Observer {mealsList->
            binding.tvCategoryCount.text = mealsList.size.toString()
            categoryMealsAdapter.setMealsList(mealsList)
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Chuẩn bị RecyclerView và thiết lập adapter cho nó
    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }
}