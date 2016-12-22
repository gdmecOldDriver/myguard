package edu.gdmec.s07150815.myguard.m2theftguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import edu.gdmec.s07150815.myguard.R;

/**
 * Created by D on 2016/12/20.
 */
public abstract class BaseSetUpActivity extends AppCompatActivity{
    public SharedPreferences sp;
    private GestureDetector mGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        //初始化手势识别器
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            //e1表示手指第一次触摸屏幕的事件
            //e2表示手指离开屏幕一瞬间的事件
            //velocityX 水平方向的速度 单位 pix/s
            //velocityY 竖直方向的速度

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX)<200){
                    Toast.makeText(getApplicationContext(),"无效动作，移动太慢",Toast.LENGTH_SHORT).show();
                    return true;
                }
                if ((e2.getRawX() - e1.getX())>200){
                    //从左向右滑动屏幕，显示上一个界面
                    showPre();
                    overridePendingTransition(R.anim.pre_in,R.anim.pre_out);
                    return true;
                }
                if ((e1.getRawX() - e2.getX())>200){
                    //从右向左滑动屏幕，显示上一个界面
                    showNext();
                    overridePendingTransition(R.anim.next_in,R.anim.next_out);
                    return true;
                }

                return  super.onFling(e1,e2,velocityX,velocityY);
            }


        });
    }
    public abstract void showNext();

    public abstract void showPre();

    //用手势识别器去识别事件

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //分析手势事件
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 开启新的activity并且关闭自己
     *
     * @param cls
     *             新的activity的字节码
     */
    public void startActivityAndFinishSelf(Class<?> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
        finish();
    }
}
