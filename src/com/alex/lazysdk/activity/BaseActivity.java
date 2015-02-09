package com.alex.lazysdk.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import com.alex.lazysdk.ioc.BaseIocEventListener;
import com.alex.lazysdk.ioc.BaseIocSelect;
import com.alex.lazysdk.ioc.BaseIocView;
import com.alex.lazysdk.utils.AbLogUtil;

import java.lang.reflect.Field;

/**
 * 创建Activity的框架，提供简便方法调用
 * Created by alex on 15-2-6.
 */
public abstract class BaseActivity extends FragmentActivity {

    /** 最底层布局 */
    private RelativeLayout mRootRlyt = null;
    /** 内容层布局 */
    private RelativeLayout mContentRlyt = null;
    /** 标题布局 */
    private RelativeLayout mTitleRlty = null;
    /** 底部布局 */
    private RelativeLayout mBottomRlty = null;

    public static final RelativeLayout.LayoutParams RLMM = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    public static final RelativeLayout.LayoutParams RLMW = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    public static final RelativeLayout.LayoutParams RLWM = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    public static final RelativeLayout.LayoutParams RLWW = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootRlyt = new RelativeLayout(this);

        mContentRlyt = new RelativeLayout(this);
        mContentRlyt.setPadding(0,0,0,0);

        mTitleRlty = new RelativeLayout(this);
        mTitleRlty.setId(1);
        mBottomRlty = new RelativeLayout(this);
        mBottomRlty.setId(2);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);

        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);

        mRootRlyt.addView(mTitleRlty,titleParams);
        mRootRlyt.addView(mBottomRlty,bottomParams);

        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,MODE_APPEND);
        contentParams.addRule(RelativeLayout.BELOW,mTitleRlty.getId());
        contentParams.addRule(RelativeLayout.ABOVE,mBottomRlty.getId());

        mRootRlyt.addView(mContentRlyt,contentParams);

        layoutInflater = LayoutInflater.from(this);

        setContentView(mRootRlyt,RLMM);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initIocView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initIocView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initIocView();
    }

    public void setBaseContentView(View view)
    {
        mContentRlyt.removeAllViews();
        mContentRlyt.addView(view, RLMM);
        initIocView();
    }

    public void setBaseContentView(int resId)
    {
        setBaseContentView(layoutInflater.inflate(resId,null));
    }

    private void initIocView()
    {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            try {
                if (field.get(this) != null)
                {
                    AbLogUtil.d(this, "" + field.get(this));
                    continue;
                }
                BaseIocView viewInject = field.getAnnotation(BaseIocView.class);
                if (viewInject != null)
                {
                    int injectId = viewInject.id();
                    field.set(this,findViewById(injectId));

                    setListener(field,viewInject.click(),BaseIocEventListener.CLICK);
                    setListener(field,viewInject.longClick(),BaseIocEventListener.LONG_CLICK);
                    setListener(field,viewInject.itemClick(),BaseIocEventListener.ITEM_CLICK);
                    setListener(field,viewInject.itemLongClick(),BaseIocEventListener.ITEM_LONG_CLICK);

                    BaseIocSelect selectInject = viewInject.select();
                    if (!TextUtils.isEmpty(selectInject.seleted()))
                    {
                        setViewSelectListener(field,selectInject.seleted(),selectInject.noSeleted());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setListener(Field field,String methodName,int method)throws Exception{
        if(methodName == null || methodName.trim().length() == 0)
            return;

        Object obj = field.get(this);

        switch (method) {
            case BaseIocEventListener.CLICK:
                if(obj instanceof View){
                    ((View)obj).setOnClickListener(new BaseIocEventListener(this).click(methodName));
                }
                break;
            case BaseIocEventListener.ITEM_CLICK:
                if(obj instanceof AbsListView){
                    ((AbsListView)obj).setOnItemClickListener(new BaseIocEventListener(this).itemClick(methodName));
                }
                break;
            case BaseIocEventListener.LONG_CLICK:
                if(obj instanceof View){
                    ((View)obj).setOnLongClickListener(new BaseIocEventListener(this).longClick(methodName));
                }
                break;
            case BaseIocEventListener.ITEM_LONG_CLICK:
                if(obj instanceof AbsListView){
                    ((AbsListView)obj).setOnItemLongClickListener(new BaseIocEventListener(this).itemLongClick(methodName));
                }
                break;
            default:
                break;
        }
    }

    private void setViewSelectListener(Field field,String select,String noSelect)throws Exception{
        Object obj = field.get(this);
        if(obj instanceof View){
            ((AbsListView)obj).setOnItemSelectedListener(new BaseIocEventListener(this).itemSelectClick(select).itemNothingSelectClick(noSelect));
        }
    }
}
