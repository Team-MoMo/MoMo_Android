package com.example.momo_android.upload

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityUploadSentenceBinding
import com.example.momo_android.upload.data.UploadSentenceData
import com.example.momo_android.util.ItemClickListener

class UploadSentenceAdapter (private val context: Context) : RecyclerView.Adapter<UploadSentenceViewHolder>(){
    var data= mutableListOf<UploadSentenceData>()
    private lateinit var binding: ActivityUploadSentenceBinding//뷰바인딩

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadSentenceViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.layout_select_sentence_card,parent,false)
        return UploadSentenceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size //ppt에서 :Int =data.size랑 같은 의미. 줄일수 있음.
    }
    override fun onBindViewHolder(holder: UploadSentenceViewHolder, position: Int) {
        holder.onBind(data[position])
        holder.itemView.setOnClickListener{
            itemClickListener.onClickItem(it,position)
        }
    }

    //클릭리스너 선언
    private lateinit var itemClickListener: ItemClickListener

    //클릭리스너 등록 메소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    /*뷰바인딩
    binding = ActivityUploadFeelingBinding.inflate(layoutInflater) // 2
    val view = binding.root // 3
    setContentView(view)

    cardview.setOnClickListener(object : View.OnClickListener {
        override fun onClick(v: View?) {
            val context: Context = v!!.context
            val intent = Intent(v!!.context, UploadWriteActivity::class.java)
            intent.putExtra("author", data.author)
            intent.putExtra("book", data.book)
            intent.putExtra("publisher", data.publisher)
            intent.putExtra("sentence", data.sentence)
            context.startActivity(intent)
        }
    })*/




}