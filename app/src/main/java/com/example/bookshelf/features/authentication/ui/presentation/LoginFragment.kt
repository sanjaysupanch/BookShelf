package com.example.bookshelf.features.authentication.ui.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bookshelf.R
import com.example.bookshelf.databinding.FragmentLoginBinding
import com.example.bookshelf.features.authentication.ui.viewmodel.LoginViewModel
import com.example.bookshelf.features.common.local.BookShelfDataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _viewBinding: FragmentLoginBinding? = null
    private val viewBinding: FragmentLoginBinding
        get() = _viewBinding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var mEmail: String
    private lateinit var mPassword: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        observe()
    }

    private fun observe() {
        with(viewBinding) {
            email.addTextChangedListener(afterTextChangedListener)
            password.addTextChangedListener(afterTextChangedListener)

            loginButton.setOnClickListener {
                lifecycleScope.launch {
                    val mUser = loginViewModel.mUser.value
                    if (mUser && mEmail.isNotEmpty()) {
                        showToast(requireActivity().getString(R.string.sign_in_msg))
                        val dataStoreManager = BookShelfDataStoreManager.get(requireContext())
                        dataStoreManager.setLoginStatus(true)
                        dataStoreManager.setCurrentUser(mEmail)
                        BookShelfDataStoreManager.get(requireContext()).setLoginStatus(true)
                        findNavController().navigate(R.id.action_loginFragment_to_dashFragment)
                    } else {
                        showToast(requireActivity().getString(R.string.error_1))
                    }
                }
            }
        }
    }

    private val afterTextChangedListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            with(viewBinding) {
                mEmail = email.text.toString()
                mPassword = password.text.toString()
                loginViewModel.getUserList(mEmail, mPassword)
                val usernameValid = isValidEmail(mEmail)
                val passwordValid = isValidPassword(mPassword)
                loginButton.isEnabled = usernameValid && passwordValid
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[!@#\$%&()])(?=.*[a-z])(?=.*[A-Z]).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        return pattern.matcher(password).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}