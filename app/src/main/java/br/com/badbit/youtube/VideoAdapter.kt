package br.com.badbit.youtube

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class VideoAdapter(private val videos: List<Video>, val onClick: (Video) -> Unit) : RecyclerView.Adapter<VideoAdapter.VideoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder =
        VideoHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_video, parent, false)
        )


    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    inner class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(video: Video) {
            with(itemView) {
                setOnClickListener {
                    onClick.invoke(video)
                }

                val thumbnailImageView = findViewById<ImageView>(R.id.video_thumbnail)
                val videoAuthorImageView = findViewById<ImageView>(R.id.video_author)

                Picasso.get().load(video.thumbnailUrl).into(thumbnailImageView)
                Picasso.get().load(video.publisher.pictureProfileUrl).into(videoAuthorImageView)
                val videoTitle = findViewById<TextView>(R.id.video_title)
                val videoInfo = findViewById<TextView>(R.id.video_info)
                videoTitle.text = video.title
                videoInfo.text = context.getString(R.string.info, video.publisher.name, video.viewsCountLabel, video.publishedAt.formatted())
            }
        }
    }

}