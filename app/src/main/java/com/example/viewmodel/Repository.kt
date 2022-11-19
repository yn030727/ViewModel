package com.example.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object Repository {
    //我们在这个类中添加了一个getUser()方法，这个方法接收一个userId参数
    //正常来说，我们是通过userId去服务器请求或者数据库中查找相应的User对象，这里是模拟，所以我们就创建一个新的即可
    fun getUser(userId:String):LiveData<User>{
        //getUser方法返回的是一个包含User数据的LiveData对象，并且每次调用getUser()方法都会返回一个新的LiveData实例
        val liveData= MutableLiveData<User>()
        liveData.value=User(userId,userId,0)
        return liveData
    }
}