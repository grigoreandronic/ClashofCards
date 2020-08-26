package com.unitn.clashofcards.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.unitn.clashofcards.R
import com.unitn.clashofcards.model.Card
import com.unitn.clashofcards.R.layout.cardlayout
import com.unitn.clashofcards.model.Deck


class DeckCardsMarketAdapter(var context: Context, var arrayList: ArrayList<Card>) :
    RecyclerView.Adapter<DeckCardsMarketAdapter.ItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_view_layout_deckcards, parent, false)
        context= parent.context
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val options: RequestOptions = RequestOptions()
            .skipMemoryCache(true)
            .centerInside()

        val charItem: Card = arrayList[position]
        Glide.with(context)
            .load(charItem.icons!!)
            .apply(options)
            .into(holder.icons)

        holder.titles.text = charItem.alpha

        holder.icons.setOnClickListener {
            var dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(cardlayout)
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);


            val cardimage = dialog.findViewById<ImageView>(R.id.cardImage)
            val cardtext    = dialog.findViewById<TextView>(R.id.cardTitle)
            cardtext.setText(holder.titles.text.toString())
            Glide.with(context)
                .load(charItem.icons)
                .apply(options)
                .into(cardimage)
            dialog.show()
        }
        holder.titles.setOnClickListener {
            Toast.makeText(context, charItem.alpha, Toast.LENGTH_LONG).show()
        }

    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var icons = itemView.findViewById<ImageView>(R.id.market_icon_image_viewcards)
        var titles = itemView.findViewById<TextView>(R.id.market_title_text_viewcards)

    }
}