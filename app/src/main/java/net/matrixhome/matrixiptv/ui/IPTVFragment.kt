package net.matrixhome.matrixiptv.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.snackbar.Snackbar
import net.matrixhome.matrixiptv.R
import net.matrixhome.matrixiptv.databinding.IPTVFragmentBinding
import net.matrixhome.matrixiptv.players.DebugInfo
import net.matrixhome.matrixiptv.viewmodel.ChannelsListViewModel
import net.matrixhome.matrixiptv.viewmodel.factory.ChannelsListViewModelFactory

class IPTVFragment : Fragment(),
    Player.Listener, IOnKeyDown {
    private val TAG = "IPTVFragment_log"
    private var _binding: IPTVFragmentBinding? = null
    private val binding get() = _binding!!
    private var timer: CountDownTimer? = null
    private var numberToSeekTimer: CountDownTimer? = null
    private var numberToPlayTimer: CountDownTimer? = null

    private lateinit var playerView: PlayerView
    private lateinit var fragment: ChannelsListFragment
    private lateinit var debugInfo: DebugInfo

    /* private val viewModel: IPTVViewModel by viewModels {
        IPTVViewModelFactory((activity?.application as App).netRepo)
     }*/
    private lateinit var viewModel: ChannelsListViewModel
    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        numberToSeekTimer?.cancel()
        numberToPlayTimer?.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IPTVFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val mainViewModel = (requireActivity().application as ChannelApp).mainViewModel
        playerView = view.findViewById(R.id.exo_video_view)
        //fixme remove !! at fragment
        fragment = requireActivity().supportFragmentManager.findFragmentById(R.id.channel_list_container) as ChannelsListFragment

        viewModel = ViewModelProvider(
            requireActivity(), ChannelsListViewModelFactory(
                requireActivity().application)
        ).get(ChannelsListViewModel::class.java)

        viewModel.player.observe(requireActivity(), Observer {
            playerView.player = it
            it?.prepare()
            it?.play()
            it?.addListener(this)
            debugInfo = DebugInfo(it!!, binding.techInfo)
            fragmetActivityTimer()
        })

//        viewModel.playlist.observe(requireActivity(), Observer {
//            if (viewModel.player.value == null)
//            viewModel.initPlayer()
//        })


        binding.root.setOnClickListener {
            showHide()
        }

        playerView.setOnClickListener {
            showHide()
            Log.d(TAG, "onViewCreated: player view touch listener")
        }

        viewModel.observeChannelListScroll(this, {
            Log.d(TAG, "onViewCreated: $it")
            fragmetActivityTimer()
        })



    }

    override fun onPlayerError(error: PlaybackException) {
        //todo something with errors occured because of source
        Log.d(TAG, "onPlayerError: ${error.errorCodeName}")
        Log.d(TAG, "onPlayerError: ${error.localizedMessage}")


        when(error.errorCode){
            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> {
                Snackbar.make(binding.root,
                    requireActivity().resources.getString(R.string.no_signal)
                    , Snackbar.LENGTH_LONG)
                    .show()
                var position: Int = 0
                position = viewModel.player.value?.currentMediaItemIndex?.plus(1) ?: 0
                viewModel.player.value?.prepare()
                viewModel.player.value?.seekTo(position, 0)
            }
            PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW -> {
                Snackbar.make(binding.root,
                    requireActivity().resources.getString(R.string.error_connection_internet)
                    , Snackbar.LENGTH_LONG)
                    .show()

                var position: Int = 0
                position = viewModel.player.value?.currentMediaItemIndex ?: 0
                viewModel.player.value?.prepare()
                viewModel.player.value?.seekTo(position, 0)
            }
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                //fixme как то отключить постоянное появление snackbar
                Snackbar.make(binding.root,
                    requireActivity().resources.getString(R.string.connection_failed)
                    , Snackbar.LENGTH_LONG)
                    .show()
                viewModel.player.value?.prepare()
            }
            else -> {
                Snackbar.make(binding.root, error.errorCodeName, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying)
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        else requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onEvents(player: Player, events: Player.Events) {
        debugInfo.updateAndPost()
        super.onEvents(player, events)
    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    ) {
        fragmetActivityTimer()
    }


    override fun OnKeyDownPressed(keyCode: Int, event: KeyEvent?) {
        Log.d(TAG, "OnKeyDownPressed: $keyCode")
        Log.d(TAG, "OnKeyDownPressed: $event")
        when(keyCode){
            KeyEvent.KEYCODE_DPAD_UP -> {
                showHide()
                //viewModel.player.value?.seekToNext()
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                showHide()
                //viewModel.player.value?.seekToPrevious()
            }
            KeyEvent.KEYCODE_DPAD_DOWN_LEFT ->{
                showHide()
            }
            KeyEvent.KEYCODE_DPAD_DOWN_RIGHT ->{
                showHide()
            }
            KeyEvent.KEYCODE_DPAD_LEFT ->{
                showHide()
            }
            KeyEvent.KEYCODE_DPAD_RIGHT ->{
                showHide()
            }
            KeyEvent.KEYCODE_ENTER -> {
                showHide()
            }
            KeyEvent.KEYCODE_PAGE_UP -> {
                viewModel.player.value?.seekToNext()
            }
            KeyEvent.KEYCODE_PAGE_DOWN -> {
                showHide()
                viewModel.player.value?.seekToPrevious()
            }
            KeyEvent.KEYCODE_CHANNEL_DOWN -> {
                viewModel.player.value?.seekToPrevious()
            }
            KeyEvent.KEYCODE_CHANNEL_UP -> {
                viewModel.player.value?.seekToNext()
            }
            KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT -> {
                /////////////
                showHide()
            }

            KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT -> {
                /////////////
                showHide()
            }
            KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN ->{
                showHide()
            }
            KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP ->{
                showHide()
            }
            KeyEvent.KEYCODE_0 -> {
                addNUmberChannelToSeek("0")
            }
            KeyEvent.KEYCODE_1 -> {
                addNUmberChannelToSeek("1")
            }
            KeyEvent.KEYCODE_2 -> {
                addNUmberChannelToSeek("2")
            }
            KeyEvent.KEYCODE_3 -> {
                addNUmberChannelToSeek("3")
            }
            KeyEvent.KEYCODE_4 -> {
                addNUmberChannelToSeek("4")
            }
            KeyEvent.KEYCODE_5 -> {
                addNUmberChannelToSeek("5")
            }
            KeyEvent.KEYCODE_6 -> {
                addNUmberChannelToSeek("6")
            }
            KeyEvent.KEYCODE_7 -> {
                addNUmberChannelToSeek("7")
            }
            KeyEvent.KEYCODE_8 -> {
                addNUmberChannelToSeek("8")
            }
            KeyEvent.KEYCODE_9 -> {
                addNUmberChannelToSeek("9")
            }
        }
    }

    private fun addNUmberChannelToSeek(number: String){
        if (binding.channelToSeek.text == ""){
            binding.channelToSeek.text = number
        }else {
            binding.channelToSeek.text = "${binding.channelToSeek.text}$number"
        }
        binding.channelToSeek.visibility = View.VISIBLE
        numberCountTimer()
        numberToPlayCountTimer()
    }


    private fun showHide(){
        if (getScreenLandscapeOrientation()){
            if (fragment?.isHidden == true){
                requireActivity().supportFragmentManager.beginTransaction().show(fragment!!).commit()
            }
            fragmetActivityTimer()
        }
    }

    private fun fragmetActivityTimer(){
        if (getScreenLandscapeOrientation()){
            timer?.cancel()
            timer = object : CountDownTimer(5000, 1000) {
                override fun onTick(p0: Long) {
                    Log.d(TAG, "onTick:")
                    //requireActivity().supportFragmentManager.beginTransaction().hide(fragment!!).commit()
                    //showHide()
                }

                override fun onFinish() {
                    Log.d(TAG, "onFinish:")
                    requireActivity().supportFragmentManager.beginTransaction().hide(fragment!!).commit()
                }
            }
            timer?.start()
        }
    }
    
    private fun numberCountTimer(){
        if (getScreenLandscapeOrientation()){
            numberToSeekTimer?.cancel()
            numberToSeekTimer = object : CountDownTimer(10000, 1000) {
                override fun onTick(p0: Long) {
                    Log.d(TAG, "onTick: input channel number to seek")
                }

                override fun onFinish() {
                    Log.d(TAG, "onFinish:")
                    binding.channelToSeek.text = ""
                    binding.channelToSeek.visibility = View.GONE
                }
            }
            numberToSeekTimer?.start()
        }
    }
    
    private fun numberToPlayCountTimer(){
        if (getScreenLandscapeOrientation()){
            numberToPlayTimer?.cancel()
            numberToPlayTimer = object : CountDownTimer(2000, 1000) {
                override fun onTick(p0: Long) {
                    Log.d(TAG, "onTick: wait until the end to play this game")
                }

                override fun onFinish() {
                    Log.d(TAG, "onFinish:")
                    Log.d(TAG, "onFinish: here must be play the channel))")
                }
            }
            numberToPlayTimer?.start()
        }
    }

    private fun getScreenLandscapeOrientation(): Boolean = when(requireActivity().resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> false
                Configuration.ORIENTATION_LANDSCAPE -> true
            else -> false
        }
}

