package com.example.foodapp.activitys

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.pojo.Meal
import com.example.foodapp.videomodel.MealViewModel
import com.example.foodapp.videomodel.HomeViewModelFactory
import com.example.foodapp.videomodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var youtobeLink:String
    private lateinit var mealMvvm: MealViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory).get(MealViewModel::class.java)

       /* mealMvvm = ViewModelProvider(this).get(MealViewModel::class.java)*/


        getMealInformationFromIntent()
        setInformationInViews()
        loadingCase()


        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onFavoriteClick()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun onFavoriteClick() {
        binding.btnAddToFavorite.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal save",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private var mealToSave:Meal? = null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailLivedata().observe(this,object : Observer<Meal>{
            override fun onChanged(value: Meal) {
                onResponseCase()
                val meal = value
                mealToSave = meal

                binding.tvCategory.text= "Category: ${meal!!.strCategory}"
                binding.tvArea.text = "Area: ${meal!!.strArea}"
                binding.tvInstructionStep.text = meal.strInstructions

            }

        })
    }

    //thiết lập thông tin của món ăn vào các thành phần giao diện người dùng
    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapingToolbar.title = mealName
        binding.collapingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

    }
    //lấy thông tin về món ăn từ Intent đã được gửi đến Activity
    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }
    //thiết lập trạng thái giao diện người dùng khi dữ liệu đang được tải.
    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFavorite.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutobe.visibility = View.INVISIBLE
    }
    //thiết lập lại giao diện người dùng khi dữ liệu đã được tải
    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFavorite.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutobe.visibility = View.VISIBLE
    }
}