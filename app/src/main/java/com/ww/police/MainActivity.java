package com.ww.police;



import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.chaquo.python.Kwarg;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.python.Python;
import com.ww.testpython.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "PythonOnAndroid";
    private static String[] PERMISSIONS = {"android.permission.CAMERA" ,"android.permission.RECORD_AUDIO","android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAllPermission();
        initPython();
        Log.e("wangwei",""+getCount());
//        callPythonCode();
    }
    // 初始化Python环境
    void initPython(){
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
    }
    // 调用python代码
    void callPythonCode(){
        Python py = Python.getInstance();
        // 调用hello.py模块中的greet函数，并传一个参数
        // 等价用法：py.getModule("hello").get("greet").call("Android");
        py.getModule("hello").callAttr("greet", "Android");

        // 调用python内建函数help()，输出了帮助信息
        py.getBuiltins().get("help").call();

        PyObject obj1 = py.getModule("hello").callAttr("add", 2,3);
        // 将Python返回值换为Java中的Integer类型
        Integer sum = obj1.toJava(Integer.class);
        Log.d(TAG,"add = "+sum.toString());

        // 调用python函数，命名式传参，等同 sub(10,b=1,c=3)
        PyObject obj2 = py.getModule("hello").callAttr("sub", 10,new Kwarg("b", 1), new Kwarg("c", 3));
        Integer result = obj2.toJava(Integer.class);
        Log.d(TAG,"sub = "+result.toString());

        // 调用Python函数，将返回的Python中的list转为Java的list
        PyObject obj3 = py.getModule("hello").callAttr("get_list", 10,"xx",5.6,'c');
        List<PyObject> pyList = obj3.asList();
        Log.d(TAG,"get_list = "+pyList.toString());

        // 将Java的ArrayList对象传入Python中使用
        List<PyObject> params = new ArrayList<PyObject>();
        params.add(PyObject.fromJava("alex"));
        params.add(PyObject.fromJava("bruce"));
        py.getModule("hello").callAttr("print_list", params);

        // Python中调用Java类
        PyObject obj4 = py.getModule("hello").callAttr("get_java_bean");
        JavaBean data = obj4.toJava(JavaBean.class);
        data.print();
    }

    int getCount(){
        String fileName = getSDPath()+"/PoliceInfo/20209251449.xls";
        Python py = Python.getInstance();
        PyObject o = py.getModule("judge").callAttr("getCount",fileName);
        int count = o.toJava(Integer.class);
        return count;
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }

        return null;
    }
    private void requestAllPermission() {
        int permission = ActivityCompat.checkSelfPermission(this,"android.permission.CAMERA");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有写的权限，去申请写的权限，会弹出对话框
            ActivityCompat.requestPermissions(this, PERMISSIONS,1);
        }
    }
}

