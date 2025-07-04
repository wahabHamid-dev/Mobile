package com.wahab.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wahab.myapp.databinding.FragmentSignupBinding
import com.wahab.myapp.data.AppResult

class SignupFragment : Fragment() {

    private lateinit var signupViewModel: SignupViewModel
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        val fullNameEditText = binding.nameEditText
        val usernameEditText = binding.usernameEditText
        val emailEditText = binding.emailEditText
        val idCardEditText = binding.idCardEditText
        val cellNumberEditText = binding.cellNumberEditText
        val passwordEditText = binding.passwordEditText
        val signupButton = binding.signupButton
        val progressBar = binding.progressBar

        signupButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val idCard = idCardEditText.text.toString().trim()
            val cellNumber = cellNumberEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (fullName.isNotEmpty() && username.isNotEmpty() &&
                email.isNotEmpty() && idCard.isNotEmpty() &&
                cellNumber.isNotEmpty() && password.isNotEmpty()
            ) {
                progressBar.visibility = View.VISIBLE
                signupViewModel.signup(username, fullName, email, idCard, cellNumber, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        signupViewModel.signupResult.observe(viewLifecycleOwner) { result ->
            progressBar.visibility = View.GONE
            when (result) {
                is AppResult.Success -> {
                    val user = result.data
                    Toast.makeText(context, "Welcome ${user.displayName}", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_signup_to_dashboard)
                }
                is AppResult.Error -> {
                    Toast.makeText(
                        context,
                        result.exception.message ?: "Signup failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
