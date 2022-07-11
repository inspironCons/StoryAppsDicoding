package bpai.dicoding.storyapss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bpai.dicoding.storyapss.databinding.ListItemHitoryBinding
import bpai.dicoding.storyapss.model.Stories
import com.bumptech.glide.Glide

class StoriesAdapter:PagingDataAdapter<Stories,StoriesAdapter.ViewHolder>(diffCallback) {

    private lateinit var itemClickListener:OnClickListener

    inner class ViewHolder(private val binding:ListItemHitoryBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(element: Stories?){
            with(binding){

                tvUsername.text = element?.name

                Glide.with(itemView.context)
                    .load(element?.image)
                    .into(ivImage)

                itemView.setOnClickListener { itemClickListener.onClick(element,binding) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemHitoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    fun setOnClickListener(clickListener:OnClickListener){
        this.itemClickListener = clickListener
    }

    interface OnClickListener{
        fun onClick(data: Stories?,view: ListItemHitoryBinding)
    }

    companion object{
        val diffCallback =object : DiffUtil.ItemCallback<Stories>(){
            override fun areItemsTheSame(oldItem: Stories, newItem: Stories): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Stories, newItem: Stories): Boolean = oldItem.name == oldItem.name &&
                    oldItem.image == newItem.image
        }
    }
}