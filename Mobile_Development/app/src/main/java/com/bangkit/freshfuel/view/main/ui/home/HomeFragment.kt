package com.bangkit.freshfuel.view.main.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.freshfuel.R
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.data.preference.RecipePreference
import com.bangkit.freshfuel.databinding.FragmentHomeBinding
import com.bangkit.freshfuel.model.response.ProgressItem
import com.bangkit.freshfuel.utils.RecipeViewModelFactory
import com.bangkit.freshfuel.utils.adapter.ProgressAdapter
import com.bangkit.freshfuel.utils.getCurrentDateFormatted
import com.bangkit.freshfuel.view.landing.NewUserActivity
import kotlinx.coroutines.launch

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
    private val currentDate = getCurrentDateFormatted()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupUI(emptyList())
        setupViewModel()

        return root
    }

    override fun onResume() {
        setupViewModel()
        super.onResume()
    }

    private fun setupViewModel() {
        val email = viewModel.userData.dataUser?.email!!
        viewModel.userData.let { user ->
            binding.greetings.text = getString(R.string.greetings, user.dataUser?.name)

            lifecycleScope.launch {
                viewModel.getHistory(email).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            setLoading(true)
                        }

                        is Result.Success -> {
                            setLoading(false)
                            val data = result.data
                            if (data.isEmpty()) {
                                generateRandom(user.dataUser?.allergies!!)
                            } else {
                                getCurrentProgress(email)
                            }
                        }

                        else -> {
                            setLoading(false)
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentProgress(email: String) {
        lifecycleScope.launch {
            viewModel.getCurrentProgress(email, currentDate)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            setLoading(true)
                        }

                        is Result.Success -> {
                            setLoading(false)
                            setupUI(result.data)
                            setupAdapter(result.data)
                        }

                        is Result.Error -> {
                            setLoading(false)
                            Toast.makeText(
                                requireActivity(),
                                "error fetching the recipe data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    private fun generateRandom(allergies: String) {
        lifecycleScope.launch {
            viewModel.getRandom(allergies).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        setLoading(true)
                    }

                    is Result.Success -> {
                        setLoading(false)
                        val recipePreference = RecipePreference.getInstance(requireActivity())
                        recipePreference.setRecipe(result.data)
                        val intent = Intent(requireActivity(), NewUserActivity::class.java)
                        startActivity(intent)
                    }

                    else -> {
                        setLoading(false)
                        Toast.makeText(
                            requireContext(),
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setupUI(data: List<ProgressItem>) {
        var sumCalories = 0
        if (data.isNotEmpty()) {
            for (item in data) {
                if (item.status == 1) {
                    sumCalories += item.calories!!
                }
            }
        } else {
            sumCalories = 0
        }

        binding.caloriesCount.text = getString(R.string.calories_count, sumCalories)

        if (sumCalories > 1500) {
            val color = ContextCompat.getColor(requireContext(), R.color.error)
            binding.caloriesCount.backgroundTintList = ColorStateList.valueOf(color)
        } else {
            val color = ContextCompat.getColor(requireContext(), R.color.primary)
            binding.caloriesCount.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    private fun setupAdapter(data: List<ProgressItem>) {
        adapter = ProgressAdapter(data)
        recyclerView.adapter = adapter
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        recyclerView = binding.menuRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}