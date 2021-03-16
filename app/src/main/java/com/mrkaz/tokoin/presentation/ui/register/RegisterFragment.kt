package com.mrkaz.tokoin.presentation.ui.register

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.presentation.base.BaseDialogFragment
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.presentation.base.extension.setNavigationResult
import com.mrkaz.tokoin.R
import kotlinx.android.synthetic.main.fragment_profile.btnRegister
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.tietPassword
import kotlinx.android.synthetic.main.fragment_register.tietUserName
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : BaseDialogFragment() {

    //region variable
    private val viewModel: RegisterVM by viewModel()
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.show(activity, "loading", null)
    }
    //endregion


    //region state
    private val registerObserver = Observer<LiveDataWrapper<UserEntity>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.ResponseStatus.ERROR -> {
                txtError.visibility = View.VISIBLE
                txtError.text = result.error?.message ?: ""
            }
            LiveDataWrapper.ResponseStatus.SUCCESS -> {
                result.response?.let {
                    setNavigationResult(it, "user")
                    showToast("${it.username} register successfully")
                    dismiss()
                }
            }
        }
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
    //endregion

    override fun getLayoutId() = R.layout.fragment_register

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.responseLiveData.observe(viewLifecycleOwner, this.registerObserver)
        viewModel.loadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)
        btnRegister.setOnClickListener {
            viewModel.register(
                tietUserName.text.toString().replace(" ", ""),
                tietPassword.text.toString().replace(" ", "")
            )
        }
    }
}