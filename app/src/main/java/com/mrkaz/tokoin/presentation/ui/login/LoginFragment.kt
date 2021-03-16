package com.mrkaz.tokoin.presentation.ui.login

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.presentation.base.BaseDialogFragment
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.presentation.base.extension.setNavigationResult
import com.mrkaz.tokoin.R
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseDialogFragment() {

    //region variable
    private val viewModel: LoginVM by viewModel()
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.show(activity, "loading", null)
    }
    //endregion

    //region state
    private val authObserver = Observer<LiveDataWrapper<UserEntity>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.ResponseStatus.ERROR -> {
                txtError.visibility = View.VISIBLE
                txtError.text = result.error?.message ?: ""
            }
            LiveDataWrapper.ResponseStatus.SUCCESS -> {
                result.response?.let {
                    setNavigationResult(it, "user")
                    showToast("${it.username} logged successfully")
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

    override fun getLayoutId() = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.responseLiveData.observe(viewLifecycleOwner, this.authObserver)
        viewModel.loadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)
        btnLogin.setOnClickListener {
            viewModel.login(
                tietUserName.text.toString().replace(" ", ""),
                tietPassword.text.toString().replace(" ", "")
            )
        }
    }
}