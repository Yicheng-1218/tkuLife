package com.example.tkulife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class RecyclerAdapter(private val data: JSONArray):RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imgView=itemView.findViewById<ImageView>(R.id.imageView1)
        val uid=itemView.findViewById<TextView>(R.id.textView5)
        val username=itemView.findViewById<TextView>(R.id.textView4)
        val RealName=itemView.findViewById<TextView>(R.id.textView6)
        val email=itemView.findViewById<TextView>(R.id.textView7)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imgView.setImageResource(R.drawable.wm)
        holder.uid.text= "No: ${ data.getJSONObject(position).getString("id") }"
        holder.RealName.text= "Name: ${ data.getJSONObject(position).getString("name") }"
        holder.email.text= "Email: ${ data.getJSONObject(position).getString("email") }"
        holder.username.text= "UserName: ${ data.getJSONObject(position).getString("username") }"
    }

    override fun getItemCount(): Int =data.length()

}