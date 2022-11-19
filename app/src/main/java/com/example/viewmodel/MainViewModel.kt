package com.example.viewmodel

import androidx.lifecycle.*

//ViewModel再Activity被摧毁的时候上面的数据也会摧毁
//所以如何保存数据呢?

class MainViewModel(countReserved:Int): ViewModel() {
    //用MutableLiveData修饰计数
    //永远只暴露不可变的LiveData给外部
    val counter:LiveData<Int>
        get() = _counter
    private val _counter = MutableLiveData<Int>()

    init{
        _counter.value=countReserved
    }
    //这里我们将部分逻辑处理业务代码放到了ViewModel中
    fun plusOne(){
        val count=_counter.value?:0
        _counter.value=count+1
    }
    fun clear(){
        _counter.value=0
    }

    //userIdLiveData对象用来观察userId的变化
    private val userIdLiveData=MutableLiveData<String>()
    //第一个参数是被观察的对象，它观察的是内部的userId
    //----->当内部的userId变化的时候，userIdLiveData观察到
    //----->switchMap观察userIdLiveData的变化
    //----->发生变化返回通过转换函数返回
    //第二个参数是一个转换函数，将转换函数返回的LiveData对象转化成一个可观察的LiveData对象
    //----->外部观察user这个LiveData对象就行
    val user:LiveData<User> =Transformations.switchMap(userIdLiveData){ userId->
        Repository.getUser(userId)
    }
    //我们再MainViewModel中也定一个getUser方法，并且让它调用另外的方法来获取LiveData对象
    fun getUser(userId:String):LiveData<User>{
        return Repository.getUser(userId)
    }


    
    private val userLiveData = MutableLiveData<User>()
    //如果我们再MainActivity中明确指定只会显示用户的名字，而不用关心用户的年龄
    //那么这个时候还将整个User类型的LiveData暴露给外部，就显得不合理了

    //而map()方法就是专门用来解决这个问题，它可以将User类型的LiveData自由转型成任意其他类型
    //map接收两个参数，第一个时原始的LiveData对象
    //第二个是转换函数
    val userName:LiveData<String> =Transformations.map(userLiveData){ user->
        "${user.firstName} ${user.lastName}"
    }
    //外部观察UserName这个LiveData就可以了。
    //当userLiveData的数据发生变化的时候，map()方法会监听到变化并执行转化函数中的逻辑
    //然后再将数据通知给userName这个数据的观察者
}


//这个类可以向ViewModel的构造方法传数据
class MainViewModelFactory(private val countReserved: Int):ViewModelProvider.Factory{
    //在这个必须是实现的接口里面，我们创建了MainViewModel的实例
    //并将countReserved参数传进去
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(countReserved) as T
    }

}
