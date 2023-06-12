package com.bangkit.freshfuel.view.main.ui.recipe

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.databinding.FragmentRecipeBinding
import com.bangkit.freshfuel.utils.RecipeAdapter
import com.bangkit.freshfuel.utils.RecipeViewModelFactory
import kotlinx.coroutines.launch

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupAction()

        return root
    }

    private fun setupRecyclerView() {
        recyclerView = binding.recipeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setupAction() {
        binding.searchTextInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchTextInputEditText.text.toString()
                if (query.isNotEmpty()) {
                    searchQuery(query)
                }
                binding.searchTextInputEditText.clearFocus()
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    private fun searchQuery(query: String) {
        lifecycleScope.launch {
            viewModel.search(query).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        setLoading(true)
                    }

                    is Result.Success -> {
                        setLoading(false)
                        setupData(result.data)
                    }

                    else -> {
                        setLoading(false)
                        Toast.makeText(
                            requireActivity(),
                            "error fetching the data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupData(data: List<String>) {
        adapter = RecipeAdapter(data)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}