package net.matrixhome.matrixiptv.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import net.matrixhome.matrixiptv.R
import net.matrixhome.matrixiptv.core.ChannelApp
import net.matrixhome.matrixiptv.data.Settings
import net.matrixhome.matrixiptv.databinding.ChannelsListFragmentBinding
import net.matrixhome.matrixiptv.presentation.channels.ChannelAdapter
import net.matrixhome.matrixiptv.presentation.channels.ChannelUI
import net.matrixhome.matrixiptv.presentation.programms.ProgrammAdapter
import net.matrixhome.matrixiptv.presentation.programms.ProgrammUI
import net.matrixhome.matrixiptv.ui.adapters.Channels.CategoryAdapter
import net.matrixhome.matrixiptv.viewmodel.ChannelsListViewModel
import net.matrixhome.matrixiptv.viewmodel.factory.ChannelsListViewModelFactory

class ChannelsListFragment : Fragment() {


    private var _binding: ChannelsListFragmentBinding? = null
    private val binding get() = _binding!!
    private val snap: SnapHelper = PagerSnapHelper()
    private lateinit var thisFragment: ChannelsListFragment


    private var programmTimer: CountDownTimer? = null
    private lateinit var viewModelIPTV: ChannelsListViewModel

    //private lateinit var sharedViewModel: IPTVViewModel
    private val TAG = "ChannelsFragment_log"


    private lateinit var adapterCat: CategoryAdapter
    private lateinit var layoutCategManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChannelsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelIPTV = ViewModelProvider(
            requireActivity(), ChannelsListViewModelFactory(
                requireActivity().application)
        ).get(ChannelsListViewModel::class.java)
        val settings = Settings(requireContext())
        val viewModel = (requireActivity().application as ChannelApp).mainViewModel


        //CHANNELS ADAPTER
        val adapterChannels = ChannelAdapter(object : ChannelAdapter.Retry {
            override fun tryAgain() {
                viewModel.fetchChannels()
            }
        }, object : ChannelAdapter.ClickListener {
            override fun onChannelClickListener(id: String, itemPosition: Int, number: Int) {
                viewModel.fetchProgramms(id)
                viewModelIPTV.seekTo(itemPosition)
                settings.saveChannelNumber(itemPosition)
                settings.saveID(id)
            }

            override fun onFocusChannelListener(id: String) {
                //fixme 19.07.2022 post timer in ~500ms
                //todo check how it`s works
                //fixme 29.07.2022 it doesn't work on my TV
                programmTimer?.cancel()
                programmTimer = object : CountDownTimer(500, 100){
                    override fun onTick(p0: Long) {
                        Log.d(TAG, "onTick: Matrix trix tick tick...")
                    }
                    override fun onFinish() {
                        viewModel.fetchProgramms(id)
                    }
                }.start()
            }
        })



        //CATEGORY ADAPTER
        adapterCat = CategoryAdapter(object : CategoryAdapter.OnCategoryItemClickListener {
            override fun onCategoryItemClick(item: String, position: Int) {
                viewModel.fetchSortedChannels(item)
                settings.saveCategory(position)
                settings.saveCategoryName(item)
            }
        })

        layoutCategManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        with(binding.categoryRV) {
            adapter = adapterCat
            layoutManager = layoutCategManager
        }
        snap.attachToRecyclerView(binding.categoryRV)

        binding.categoryRV.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                viewModelIPTV.setChannelListState(newState)
                super.onScrollStateChanged(recyclerView, newState)
            }
        })


        //PROGRAMM ADAPTER
        val programmAdapter = ProgrammAdapter()
        binding.programListRV.adapter = programmAdapter
        binding.programListRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.programListRV.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.observeProgramms(this, {
            programmAdapter.update(it)
            binding.programListRV.suppressLayout(it.contains(ProgrammUI.Progress))
        })


        binding.programListRV.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                viewModelIPTV.setChannelListState(newState)
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        
        
        //CHANNELS ADAPTER
        binding.channelListRV.adapter = adapterChannels
        binding.channelListRV.layoutManager = object : LinearLayoutManager(requireContext()
            , LinearLayoutManager.VERTICAL
            , false){
            override fun onInterceptFocusSearch(focused: View, direction: Int): View? {
                //fixme 16.08.22 check focus on item when scroll list
                val pos = getPosition(focused)
                if (//direction == View.FOCUS_RIGHT
                //|| direction == View.FOCUS_LEFT
                 direction == View.FOCUS_DOWN
                // direction == View.FOCUS_UP
                ) {
                    return focused
                }
                    else
                        scrollToPosition(pos + 1)
                    return super.onInterceptFocusSearch(focused, direction)
            }
        }
        binding.channelListRV.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )


        viewModel.observeChannelUI(this) {
            viewModelIPTV.initPlayer(it)
            adapterChannels.update(it)
            binding.channelListRV.suppressLayout(it.contains(ChannelUI.Progress))
            when (it[0]) {
                is ChannelUI.Base -> {
                    adapterCat.update(resources.getStringArray(R.array.categories))
                    layoutCategManager.scrollToPosition(settings.fetchCategory())
                    viewModelIPTV.initPlayer(it)
                    adapterChannels.update(it)
                    viewModelIPTV.seekTo(settings.fetchChannelNumber())
                    viewModel.fetchProgramms(settings.fetchID())
                }
                is ChannelUI.Progress -> adapterCat.update(arrayOf("progress"))
                is ChannelUI.Fail -> adapterCat.update(arrayOf("progress"))
            }
        }
        binding.channelListRV.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                viewModelIPTV.setChannelListState(newState)
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        viewModel.fetchChannels()
    }
}