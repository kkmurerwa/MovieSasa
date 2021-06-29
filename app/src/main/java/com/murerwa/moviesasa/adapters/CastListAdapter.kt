package com.murerwa.moviesasa.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.murerwa.moviesasa.R
import com.murerwa.moviesasa.databinding.ListItemCastMemberBinding
import com.murerwa.moviesasa.models.Cast

class CastListAdapter : RecyclerView.Adapter<CastListAdapter.ViewHolder>() {
    private val castList: ArrayList<Cast> = arrayListOf()

    private lateinit var context: Context
    fun setList(newList: ArrayList<Cast>) {
        castList.clear()
        castList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setContext(context: Context) {
        this.context = context
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_cast_member, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount() = castList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCast(castList[position])
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private lateinit var currentCast : Cast
        private val binding = ListItemCastMemberBinding.bind(view)

        fun bindCast(castItem: Cast) {
            this.currentCast = castItem

            binding.tvCastRealName.text = castItem.name
            binding.tvCastCharacter.text = "as ${castItem.character}"

            // Create spinner drawable for the glide placeholder
            val progressDrawable = CircularProgressDrawable(binding.root.context)
            progressDrawable.strokeWidth = 5f
            progressDrawable.centerRadius = 30f
            progressDrawable.start()

            // Create a custom load error placeholder for glide based on gender
            val customDrawable = if (castItem.gender == 2) R.drawable.ic_profile_male else R.drawable.ic_profile_female


            Log.d("URL", "https://image.tmdb.org/t/p/w500/${castItem.profile_path}")
            // Load images with glide
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w500/${castItem.profile_path}")
                .placeholder(progressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(customDrawable)
                .fallback(customDrawable)
                .into(binding.imvCastPhoto)
        }
    }
}