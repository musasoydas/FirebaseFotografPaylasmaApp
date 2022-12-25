package com.example.fotografpaylasmauygulamasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*
import java.util.ArrayList
//67
class FeedRecyclerViewAdapter(val postlist: ArrayList<Post>) :RecyclerView.Adapter<FeedRecyclerViewAdapter.PostHolder>()  {
    class PostHolder(itemView: View):RecyclerView.ViewHolder(itemView)
//69
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        var inflater=LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        return PostHolder(inflater)
    }
//70
    override fun onBindViewHolder(holder: PostHolder, position: Int) {
    holder.itemView.recycler_row_kullanici_email.text=postlist[position].kullaniciEmail
    holder.itemView.recycler_row_kullanici_yorum.text=postlist[position].kullaniciYorum
   //71 feed activyde görünmesini sağlamak için oraya gidiyoruz

    //77gorselin urlsini indirip bize resim olarak gösterecek
    Picasso.get().load(postlist[position].gorselUrl).into(holder.itemView.recycler_row_imageView)

    }
//68
    override fun getItemCount(): Int {
        return postlist.size
    }
}