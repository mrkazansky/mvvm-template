package com.mrkaz.tokoin.presentation.ui.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.presentation.base.BaseFragment
import com.mrkaz.tokoin.presentation.base.extension.getNavigationResultLiveData
import com.mrkaz.tokoin.R
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject

class ProfileFragment : BaseFragment() {

    private val authUtils: AuthUtils by inject()

    override fun getLayoutId() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener {
            findNavController().navigate(R.id.profile_to_login)
        }
        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.profile_to_register)
        }
        btnLogout.setOnClickListener {
            authUtils.logout()
        }
        getNavigationResultLiveData<UserEntity>("user")?.observe(viewLifecycleOwner, {
            authUtils.logged(it)
        })
        authUtils.loggingStatus.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    txtName.text = authUtils.getAccount()?.name
                    btnLogout.visibility = View.VISIBLE
                    btnLogin.visibility = View.GONE
                    btnRegister.visibility = View.GONE
                }
                else -> {
                    txtName.text = "Not logged"
                    btnLogin.visibility = View.VISIBLE
                    btnRegister.visibility = View.VISIBLE
                    btnLogout.visibility = View.GONE
                }
            }
        })
    }

}