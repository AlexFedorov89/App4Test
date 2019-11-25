package com.fedorov.alex.app4test.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fedorov.alex.app4test.R
import com.fedorov.alex.app4test.viewModels.PlaylistViewModel
import kotlinx.android.synthetic.main.activity_playlist.*

class PlaylistActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PlaylistActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        val viewModel = ViewModelProviders.of(this).get(PlaylistViewModel::class.java)

        val adapter = MainAdapter {
            viewModel.playRecord(it)
        }
        mainRecycler.adapter = adapter
        mainRecycler.layoutManager = LinearLayoutManager(this)

        viewModel.viewState().observe(this, Observer<MainViewState> {
            it?.let { adapter.records = it.records }
        })
    }
}