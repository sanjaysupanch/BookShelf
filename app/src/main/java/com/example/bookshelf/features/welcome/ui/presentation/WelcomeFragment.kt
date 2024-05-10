package com.example.bookshelf.features.welcome.ui.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookshelf.R
import com.example.bookshelf.databinding.FragmentWelcomeBinding
import com.example.bookshelf.features.common.local.BookShelfDataStoreManager

class WelcomeFragment : Fragment() {

    private var _viewBinding: FragmentWelcomeBinding? = null
    private val viewBinding: FragmentWelcomeBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isLogin = BookShelfDataStoreManager.get(requireContext()).getLoginStatus()
        if (isLogin) {
            findNavController().navigate(R.id.action_welcomeFragment_to_dashFragment)
        } else {
            viewBinding.login.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }

            viewBinding.registration.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_registrationFragment)
            }
        }
    }
}