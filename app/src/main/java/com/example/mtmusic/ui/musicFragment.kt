package com.example.mtmusic.ui

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.get
import com.example.mtmusic.R
import com.example.mtmusic.bean.musicInfo
import com.example.mtmusic.databinding.FragmentMusicBinding

class musicFragment : Fragment() {

    companion object {
        fun newInstance(id: Long): musicFragment {
            val fragment = musicFragment()
            val bundle = Bundle()
            bundle.putLong("musicId", id)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: playViewModel

    private var musicData: MutableList<musicInfo>? = null
    private var music: musicInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(playViewModel::class.java)
        musicData = viewModel.musicData.value as MutableList<musicInfo>?
        val id = requireArguments().getLong("musicId")
        if (musicData != null) {
            for (music in musicData!!) {
                if (id == music.id) {
                    this.music = music
                    init(music)
                    break
                }
            }
        }

//        binding.like.setOnClickListener {
//            music!!.like = true
//        }
    }

    private fun init(music: musicInfo) {
        // ui
//        if (music.like) {
//            binding.like.isDown = false
//        }
        binding.title.text = music.title
    }

}