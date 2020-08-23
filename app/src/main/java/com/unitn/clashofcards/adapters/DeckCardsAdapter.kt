package com.unitn.clashofcards.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.unitn.clashofcards.R
import com.unitn.clashofcards.model.Deck


class DeckCardsAdapter(var context: Context, var arrayList: ArrayList<Deck>) :
    RecyclerView.Adapter<DeckCardsAdapter.ItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_view_layout_deckcards, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val options: RequestOptions = RequestOptions()
            .skipMemoryCache(true)
            .centerInside()

        val charItem: Deck = arrayList[position]
        Glide.with(context)
            .load(charItem.icons!!)
            .apply(options)
            .into(holder.icons)

        holder.titles.text = charItem.alpha

        holder.icons.setOnClickListener {

        }
        holder.titles.setOnClickListener {
            Toast.makeText(context, charItem.alpha, Toast.LENGTH_LONG).show()
        }

    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var icons = itemView.findViewById<ImageView>(R.id.icon_image_viewcards)
        var titles = itemView.findViewById<TextView>(R.id.title_text_viewcards)

    }
}