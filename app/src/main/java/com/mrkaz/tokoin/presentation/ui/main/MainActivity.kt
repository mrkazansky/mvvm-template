package com.mrkaz.tokoin.presentation.ui.main

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.mrkaz.tokoin.presentation.base.BaseActivity
import com.mrkaz.tokoin.presentation.base.extension.setupWithNavController
import com.mrkaz.tokoin.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {


    private val viewModel: MainVM by viewModel()
    private var currentNavController: LiveData<NavController>? = null
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.show(this, "loading", null)
    }
    private val loadingObserver = Observer<Boolean> { visible ->
        when {
            visible -> {
                progressDialog.show()
            }
            else -> {
                progressDialog.dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        viewModel.loadingLiveData.observe(this, loadingObserver)
        viewModel.setupData()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        setupUI()
    }

    private fun setupUI() {
        val navGraphIds = listOf(R.navigation.news, R.navigation.news_ref, R.navigation.profile)
        val controller = navigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.viewChildFrame,
            intent = intent
        )
        controller.observe(this, { navController ->
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.list, R.id.profile_page -> navigation.visibility = View.VISIBLE
                    else -> navigation.visibility = View.GONE
                }
            }
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
