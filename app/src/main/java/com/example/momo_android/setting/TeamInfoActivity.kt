package com.example.momo_android.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.databinding.ActivityListBinding
import com.example.momo_android.databinding.ActivityTeamInfoBinding

class TeamInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTeamInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.imagebuttonBack.setOnClickListener {
            finish()
        }
    }
}