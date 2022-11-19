package com.example.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var sp:SharedPreferences
    lateinit var btnPlus:Button
    lateinit var textView :TextView
    lateinit var btnClear:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //获取实例
        btnClear=findViewById(R.id.button2)
        btnPlus=findViewById(R.id.button)
        textView=findViewById(R.id.textView)
        //创建数据库
        sp=getPreferences(Context.MODE_PRIVATE)
        val countReserved=sp.getInt("count_reserved",0)
        //就是用数据库保存了数据，然后再创建ViewModel的时候把我们的数据通过构造函数
        //再次传递给ViewModel本身，然后显示出来
        viewModel = ViewModelProvider(this,MainViewModelFactory(countReserved)).get(MainViewModel::class.java)
        btnPlus.setOnClickListener {
            viewModel.plusOne()
        }
        btnClear.setOnClickListener {
            viewModel.clear()
        }
        //观察ViewModel的数据变化
        //第一个参数时LifecycleOwner对象，Activity本身就是一个LifecycleOwner对象，因此可以直接传this
        //第二个是Observer接口，当counter中包含的数据发生变化时，就会回调到这里
        viewModel.counter.observe(this, Observer { count->
            textView.text=count.toString()
        })
    }
    private fun refreshCounter(){
        textView.text=viewModel.counter.toString()
    }
    override fun onPause() {
        super.onPause()
        sp.edit{
            putInt("count_reserved",viewModel.counter.value?:0)
        }
    }
}