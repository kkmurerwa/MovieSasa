package com.murerwa.moviesasa.adapters

import android.content.Context
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
        val currentCast = castList[position]
        holder.bindCast(currentCast, position, castList)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v

        private lateinit var currentCast : Cast

        private var _binding: ListItemCastMemberBinding? = null

        private val binding get() = _binding!!

        init {
            v.setOnClickListener(this)
            _binding = ListItemCastMemberBinding.bind(view)
        }

        override fun onClick(v: View) {

        }

        fun bindCast(castItem: Cast, position: Int, castList: List<Cast>) {
            this.currentCast = castItem

            binding.tvCastRealName.text = castItem.name
            binding.tvCastCharacter.text = "as ${castItem.character}"
//            binding.tvMovieTitle.text = movieItem.title
//            binding.tvMovieDesc.text = movieItem.overview
//            binding.rbMovieRating.rating = Config().ratingConverter(movieItem.vote_average)
//
            val progressDrawable = CircularProgressDrawable(binding.root.context)
            progressDrawable.strokeWidth = 5f
            progressDrawable.centerRadius = 30f
            progressDrawable.start()

            val customDrawable = if (castItem.gender == 2) R.drawable.ic_profile_male else R.drawable.ic_profile_female

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