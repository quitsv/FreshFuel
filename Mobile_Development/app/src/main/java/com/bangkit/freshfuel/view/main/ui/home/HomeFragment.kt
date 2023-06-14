package com.bangkit.freshfuel.view.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.databinding.FragmentHomeBinding
import com.bangkit.freshfuel.utils.RecipeViewModelFactory
import com.bangkit.freshfuel.utils.adapter.ProgressAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        RecipeViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProgressAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupViewModel()

        return root
    }

    private fun setupViewModel() {
        viewModel.userData.let { data ->
            binding.greetings.text = getString(R.string.greetings, data.dataUser?.name)
        }
    }

    private fun setupRecyclerView() {
        recyclerView = binding.menuRecyclerView
        recyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}