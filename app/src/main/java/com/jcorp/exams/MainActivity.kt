package com.jcorp.exams

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.firestore.FirebaseFirestore
import com.jcorp.exams.databinding.ActivityMainBinding
import com.jcorp.exams.directmsg.DMFragment
import com.jcorp.exams.directmsg.DMListView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding : ActivityMainBinding
    private val viewModel by viewModels<mViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        getUserData()
        observe()
        setUi()
    }

    private fun observe() {
        viewModel.curUserData.observe(this, Observer {
            Glide.with(this)
                .asBitmap()
                .load(it.PhotoUrl)
                .apply(
                    RequestOptions
                        .circleCropTransform()
                        .placeholder(R.drawable.placeholder))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.bottomView.menu.findItem(R.id.nul).icon = BitmapDrawable(resources, resource)
                    }

                })
        })

        viewModel.shouldHideBottomView.observe(this, Observer {
            when(it) {
                true -> {
                    binding.bottomView.visibility = View.INVISIBLE
                }
                false -> {
                    binding.bottomView.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun getUserData() {
        val prefs = getSharedPreferences("UserData", MODE_PRIVATE)
        viewModel.userEmail = prefs.getString("Email", "")!!

        FirebaseFirestore.getInstance().collection("UserData")
            //.document(viewModel.email)
            .document(viewModel.userEmail)
            .get()
            .addOnSuccessListener {
                it.toObject(CurUserData::class.java)?.let { it1 -> viewModel.setCurUserData(it1) }
            }
    }

    private fun setUi() {
        binding.bottomView.itemIconTintList = null
        binding.bottomView.setOnItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.container, DMFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_friendList -> {
                //친구창
            }

            R.id.menu_directMsg -> {

            }

            R.id.menu_search -> {
                //탐색창
            }

            R.id.menu_mention -> {
                //멘션창
            }

            R.id.nul -> {
                //프로필창
            }
        }
        return true
    }
}