package com.example.mtmusic.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mtmusic.R
import com.example.mtmusic.adapter.musicAdapter
import com.example.mtmusic.bean.musicInfo
import com.example.mtmusic.databinding.FragmentPlayBinding

class playFragment : Fragment(), musicAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = playFragment()
    }

    private lateinit var viewModel: playViewModel

    private var _binding : FragmentPlayBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[playViewModel::class.java]

        viewModel.searchStore()
        val musicDataObserver = Observer<List<musicInfo>> {
            // UI
            val recyclerView = binding.recyclerView
            val adapter = musicAdapter(viewModel.musicData.value!!, this)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }

        viewModel.musicData.observe(viewLifecycleOwner, musicDataObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        // 替换Fragment
        val fragment = musicFragment.newInstance(viewModel.musicData.value!![position].id)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.container, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

}