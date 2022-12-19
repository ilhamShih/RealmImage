package com.realm.imade.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.realm.imade.R
import com.realm.imade.db.BaseList


class ListAdapter(
    mBaseList: List<BaseList>,
    mContext: Context,
    listAdapterInterface: ListAdapterInterface
) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var baseLists: List<BaseList> = mBaseList
    var context: Context = mContext
    var listener: ListAdapterInterface = listAdapterInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(baseLists[position].image).into(holder.imageView)
        holder.imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        holder.imageView.setOnClickListener {
            listener.onResult(
                baseLists[holder.adapterPosition],
                holder.adapterPosition
            )
        }
    }

    override fun getItemCount(): Int {
        return baseLists.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.image)
        }
    }

    interface ListAdapterInterface {
        fun onResult(listItem: BaseList, int: Int)
    }


}