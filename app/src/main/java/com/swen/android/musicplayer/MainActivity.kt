package com.swen.android.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.swen.android.musicplayer.databinding.ActivityMainBinding
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

class MainActivity : AppCompatActivity() {

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.song1)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        lateinit var runnable: Runnable
        var handler = Handler(Looper.myLooper()!!)
        //Duration
        val durationMinute = mediaPlayer.duration / 1000 / 60
        val durationSecond = mediaPlayer.duration / 1000 % 60
        val textDuration = "${durationMinute}:${durationSecond}"
        binding.endTime.text= textDuration
        //Play Button
        binding.playCurrent.setOnClickListener{
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
                binding.playCurrent.setImageResource(R.drawable.ic_baseline_pause_24)
            }
            else{
                mediaPlayer.pause()
                binding.playCurrent.setImageResource(R.drawable.ic_baseline_play_arrow_24)

            }
        }
        //Seekbar functionality
        binding.timeSeek.progress = 0
        binding.timeSeek.max = mediaPlayer.duration

        binding.timeSeek.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                seekBar: SeekBar?,
                position: Int,
                changed: Boolean
            ) {
                if(changed){
                    mediaPlayer.seekTo(position)
                    val currentTimeStamp = mediaPlayer.timestamp?.anchorMediaTimeUs
                    val currentMinute = TimeUnit.MICROSECONDS.toMinutes(currentTimeStamp!!)
                    val currentSecond = TimeUnit.MICROSECONDS.toSeconds(currentTimeStamp!!)%60
                    if (currentSecond < 10){
                        binding.startTime.text = "$currentMinute:0$currentSecond"
                    }
                    else{
                        binding.startTime.text = "$currentMinute:$currentSecond"
                    }

                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        //seekbar progress during song
        runnable = Runnable {
            binding.timeSeek.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)
        mediaPlayer.setOnCompletionListener {
            binding.playCurrent.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.timeSeek.progress = 0
        }
    }

}
