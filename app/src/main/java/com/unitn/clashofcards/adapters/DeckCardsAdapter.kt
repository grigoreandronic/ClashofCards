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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.unitn.clashofcards.R
import com.unitn.clashofcards.model.Card
import com.unitn.clashofcards.R.layout.cardlayout
import com.unitn.clashofcards.model.Deck


class DeckCardsAdapter(var context: Context, var arrayList: ArrayList<Card>) :
    RecyclerView.Adapter<DeckCardsAdapter.ItemHolder>() {


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
        val options2: RequestOptions = RequestOptions()
            .skipMemoryCache(true)
            .centerInside()
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
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


            val cardimage = dialog.findViewById<ImageView>(R.id.cardImage)
            val cardtext  = dialog.findViewById<TextView>(R.id.cardTitle)
            var attributename1 = dialog.findViewById<TextView>(R.id.attributename1)
            var attributevalue1 = dialog.findViewById<TextView>(R.id.attributevalue1)
            var attributename2 = dialog.findViewById<TextView>(R.id.attributename2)
            var attributevalue2 = dialog.findViewById<TextView>(R.id.attributevalue2)
            var attributename3 = dialog.findViewById<TextView>(R.id.attributename3)
            var attributevalue3 = dialog.findViewById<TextView>(R.id.attributevalue3)
            var attributename4 = dialog.findViewById<TextView>(R.id.attributename4)
            var attributevalue4 = dialog.findViewById<TextView>(R.id.attributevalue4)
            var attributename5 = dialog.findViewById<TextView>(R.id.attributename5)
            var attributevalue5 = dialog.findViewById<TextView>(R.id.attributevalue5)


            cardtext.text=holder.titles.text.toString()
            attributename1.text=charItem.attributename1!!
            attributevalue1.text=charItem.attributevalue1!!
            attributename2.text=charItem.attributename2!!
            attributevalue2.text=charItem.attributevalue2!!
            attributename3.text=charItem.attributename3!!
            attributevalue3.text=charItem.attributevalue3!!
            attributename4.text=charItem.attributename4!!
            attributevalue4.text=charItem.attributevalue4!!
            attributename5.text=charItem.attributename5!!
            attributevalue5.text=charItem.attributevalue5!!



            Glide.with(context)
                .load(charItem.icons)
                .apply(options2)
                .into(cardimage)
            dialog.show()
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