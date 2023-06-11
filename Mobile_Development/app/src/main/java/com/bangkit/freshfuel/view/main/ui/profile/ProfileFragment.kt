package com.bangkit.freshfuel.view.main.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.databinding.FragmentProfileBinding
import com.bangkit.freshfuel.utils.ViewModelFactory
import com.bangkit.freshfuel.view.login.LoginActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViewModel()
        setupButton()

        return root
    }

    private fun setupButton() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            viewModel.getUser().observe(viewLifecycleOwner) { data ->
                when (data) {
                    is Result.Loading -> {
                        binding.tvName.text = resources.getString(R.string.loading)
                        binding.tvEmail.text = resources.getString(R.string.loading)
                    }

                    is Result.Success -> {
                        binding.tvName.text = data.data.dataUser?.name
                        binding.tvEmail.text = data.data.dataUser?.email
                    }

                    else -> {
                        binding.tvName.text = resources.getString(R.string.name)
                        binding.tvEmail.text = resources.getString(R.string.prompt_email)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}