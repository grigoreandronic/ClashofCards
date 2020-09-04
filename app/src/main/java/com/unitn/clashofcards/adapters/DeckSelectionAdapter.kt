package com.unitn.clashofcards.adapters


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unitn.clashofcards.R
import com.unitn.clashofcards.matcher.CreateGame
import com.unitn.clashofcards.matcher.WaitingActivity
import com.unitn.clashofcards.model.Deck
import kotlinx.android.synthetic.main.activity_gamemode.*


class DeckSelectionAdapter(var context: Context, var arrayList: ArrayList<Deck>) :
  RecyclerView.Adapter<DeckSelectionAdapter.ItemHolder>() {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
    context= parent.context
    val viewHolder = LayoutInflater.from(parent.context)
      .inflate(R.layout.grid_view_deck_selection, parent, false)
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
      var dialog = Dialog(context)
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
      dialog.setCancelable(true)
      dialog.setContentView(R.layout.activity_gamemode)
      dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
      dialog.show()
      dialog.join.setOnClickListener {
        val intent = Intent(context, WaitingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        var id = arrayList.get(holder.layoutPosition)

        intent.putExtra("idDeck","${id.id}")
        dialog.dismiss()
        context.startActivity(intent)
      }

      dialog.create.setOnClickListener {
        val intent = Intent(context, CreateGame::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        var id = arrayList.get(holder.layoutPosition)
        dialog.dismiss()
        intent.putExtra("idDeck","${id.id}")
        context.startActivity(intent)
      }

    }
    holder.titles.setOnClickListener {


    }

  }

  class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var icons = itemView.findViewById<ImageView>(R.id.icon_image_view)
    var titles = itemView.findViewById<TextView>(R.id.title_text_view)

  }
}