package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.sin

class SynthAudioPlayer {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    fun stopAudio() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            // ignore
        }
        mediaPlayer = null
        _isPlaying.value = false
    }

    fun playAudioTeaserUrl(url: String?, fallbackFrequencies: List<Int>) {
        scope.launch {
            stopAudio()
            _isPlaying.value = true
            if (!url.isNullOrBlank()) {
                try {
                    val player = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        setDataSource(url)
                        prepareAsync()
                        setOnPreparedListener { mp ->
                            mp.start()
                            _isPlaying.value = true
                        }
                        setOnErrorListener { mp, _, _ ->
                            mp.release()
                            mediaPlayer = null
                            _isPlaying.value = false
                            playToneSequence(fallbackFrequencies)
                            true
                        }
                        setOnCompletionListener { mp ->
                            mp.release()
                            mediaPlayer = null
                            _isPlaying.value = false
                        }
                    }
                    mediaPlayer = player
                    return@launch
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            playToneSequence(fallbackFrequencies)
        }
    }

    fun playToneSequence(frequencies: List<Int>, durationPerToneMs: Int = 300) {
        scope.launch {
            _isPlaying.value = true
            try {
                val sampleRate = 44100
                val totalSamples = (sampleRate * (durationPerToneMs / 1000.0)).toInt()
                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                frequencies.forEach { freq ->
                    val buffer = ShortArray(totalSamples)
                    val angularFreq = 2.0 * Math.PI * freq / sampleRate

                    for (i in 0 until totalSamples) {
                        // Apply attack-decay envelope to avoid clicking
                        val attack = (i.toDouble() / (totalSamples * 0.1)).coerceAtMost(1.0)
                        val release = ((totalSamples - i).toDouble() / (totalSamples * 0.2)).coerceAtMost(1.0)
                        val envelope = attack * release

                        val sampleValue = sin(angularFreq * i) * 0.4 * envelope
                        buffer[i] = (sampleValue * Short.MAX_VALUE).toInt().toShort()
                    }

                    val track = AudioTrack.Builder()
                        .setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        .setAudioFormat(
                            AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(sampleRate)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                .build()
                        )
                        .setBufferSizeInBytes(maxOf(buffer.size * 2, minBufferSize))
                        .setTransferMode(AudioTrack.MODE_STATIC)
                        .build()

                    val written = track.write(buffer, 0, buffer.size)
                    if (written > 0) {
                        track.play()
                        Thread.sleep(durationPerToneMs.toLong() + 20L)
                    }
                    try {
                        track.stop()
                        track.release()
                    } catch (e: Exception) {
                        // Ignore cleanup exceptions
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isPlaying.value = false
            }
        }
    }

    fun playReverseEffect() {
        playToneSequence(listOf(880, 659, 554, 440, 330), durationPerToneMs = 150)
    }

    fun playVictoryFanfare() {
        playToneSequence(listOf(440, 554, 659, 880, 1108), durationPerToneMs = 200)
    }

    fun playEasterEggChime() {
        playToneSequence(listOf(523, 659, 783, 1046), durationPerToneMs = 180)
    }
}
