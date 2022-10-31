package br.com.badbit.youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    private lateinit var rvMain: RecyclerView;
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var motionContainer: ConstraintLayout
    private lateinit var progressRecycler: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val videos: MutableList<Video> = mutableListOf<Video>()
        motionContainer = findViewById(R.id.motion_container)
        progressRecycler = findViewById(R.id.progress_recycler)

        videoAdapter = VideoAdapter(videos) { video ->
            println(video)
        }

        rvMain = findViewById(R.id.rv_main)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = videoAdapter

        CoroutineScope(Dispatchers.IO).launch {
            val res = async { getVideo() }
            val listVideo = res.await()
            withContext(Dispatchers.Main) {
                listVideo?.let {
                    videos.clear()
                    videos.addAll(listVideo.data)
                    videoAdapter.notifyDataSetChanged()
                    motionContainer.removeView(progressRecycler)
                }
            }
        }
    }

    private fun getVideo(): ListVideo? {
        val client = OkHttpClient.Builder().build()

        val request = Request.Builder()
            .get()
            .url("https://tiagoaguiar.co/api/youtube-videos")
            .build()

        return try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                GsonBuilder().create().fromJson(response.body()?.string(), ListVideo::class.java)
            } else {
                Log.e("error-net", "Deu Ruim Aqui")
                null
            }
        } catch (e: Exception) {
            Log.e("error-net", e.toString())
            null
        }
    }
}