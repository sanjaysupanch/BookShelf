package com.example.bookshelf.features.authentication.ui.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bookshelf.R
import com.example.bookshelf.databinding.FragmentRegistrationBinding
import com.example.bookshelf.features.authentication.data.model.User
import com.example.bookshelf.features.authentication.ui.viewmodel.RegistrationViewModel
import com.example.bookshelf.features.common.local.BookShelfDataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private var _viewBinding: FragmentRegistrationBinding? = null
    private val viewBinding: FragmentRegistrationBinding get() = _viewBinding!!

    private val mViewModel: RegistrationViewModel by viewModels()

    private lateinit var mEmail: String
    private lateinit var mPassword: String
    private lateinit var mName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        observer()
    }

    private fun observer() {
        var selectedCountry = ""
        lifecycleScope.launch {
            mViewModel.mCountryList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .onEach { country ->
                    if (country.isNotEmpty()) {
                        val defaultCountry = requireActivity().getString(R.string.default_country)
                        viewBinding.loading.visibility = View.GONE
                        val adapter = ArrayAdapter(
                            requireActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            country
                        )
                        viewBinding.country.adapter = adapter
                        val defaultIndex = country.indexOf(defaultCountry)
                        if (defaultIndex != -1) {
                            viewBinding.country.setSelection(defaultIndex)
                        }

                        viewBinding.country.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    selectedCountry = country[position]
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                }
                            }
                    } else {
                        viewBinding.loading.visibility = View.VISIBLE
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }

        with(viewBinding) {
            email.addTextChangedListener(afterTextChangedListener)
            name.addTextChangedListener(afterTextChangedListener)
            password.addTextChangedListener(afterTextChangedListener)

            signInButton.setOnClickListener {
                lifecycleScope.launch {
                    val isUserExist = mViewModel.mUserExist.value
                    if (isUserExist) {
                        showToast(requireActivity().getString(R.string.email_taken))
                    } else {
                        val success = mViewModel.insertUser(
                            User(
                                email = mEmail,
                                name = mName,
                                password = mPassword,
                                country = selectedCountry
                            )
                        )
                        if (success) {
                            showToast(requireActivity().getString(R.string.registered_msg))
                            val dataStoreManager = BookShelfDataStoreManager.get(requireContext())
                            dataStoreManager.setLoginStatus(true)
                            dataStoreManager.setCurrentUser(mEmail)
                            findNavController().navigate(R.id.action_registrationFragment_to_dashFragment)
                        } else {
                            showToast(requireActivity().getString(R.string.invalid_email_password_msg))
                        }
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
                mName = name.text.toString()
                mViewModel.isUserExisting(mEmail)
                val usernameValid = isValidEmail(mEmail)
                val passwordValid = isValidPassword(mPassword)
                signInButton.isEnabled = usernameValid && passwordValid && mName.isNotEmpty()
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

//    //Method to get ip address
//    fun getIpAddress(): String {
//        val wifiManager =
//            requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        val wifiInfo = wifiManager.connectionInfo
//        val ipAddress = wifiInfo.ipAddress
//
//        return String.format(
//            "%d.%d.%d.%d",
//            ipAddress and 0xff,
//            ipAddress shr 8 and 0xff,
//            ipAddress shr 16 and 0xff,
//            ipAddress shr 24 and 0xff
//        )
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}