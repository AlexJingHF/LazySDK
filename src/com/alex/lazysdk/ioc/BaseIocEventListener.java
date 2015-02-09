package com.alex.lazysdk.ioc;

import android.view.View;
import android.widget.AdapterView;
import com.alex.lazysdk.utils.AbLogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by alex on 15-2-6.
 */
public class BaseIocEventListener implements View.OnClickListener,View.OnLongClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener,AdapterView.OnItemSelectedListener {

    /**
     * 定义事件类型
     */
    public static final int CLICK = 0;
    public static final int LONG_CLICK = 1;
    public static final int ITEM_CLICK = 2;
    public static final int ITEM_LONG_CLICK = 3;

    private String clickMethod;
    private String longClickMethod;
    private String itemClickMethod;
    private String itemLongClickMethod;
    private String itemSelectClickMethod;
    private String itemNothingSelectMethod;

    private Object handler = null;

    public BaseIocEventListener(Object handler) {
        this.handler = handler;
    }

    public BaseIocEventListener click(String method)
    {
        this.clickMethod = method;
        return this;
    }

    public BaseIocEventListener longClick(String method)
    {
        this.longClickMethod = method;
        return this;
    }

    public BaseIocEventListener itemClick(String method)
    {
        this.itemClickMethod = method;
        return this;
    }

    public BaseIocEventListener itemLongClick(String method)
    {
        this.itemLongClickMethod = method;
        return this;
    }

    public BaseIocEventListener itemSelectClick(String method)
    {
        this.itemSelectClickMethod = method;
        return this;
    }

    public BaseIocEventListener itemNothingSelectClick(String method)
    {
        this.itemNothingSelectMethod = method;
        return this;
    }

    @Override
    public void onClick(View v) {
        invokeClickMethod(handler,clickMethod,v);
    }

    private void invokeClickMethod(Object handler, String clickMethod, Object... params) {
        if (handler == null) return;
        Method m = null;
        try {
            m = handler.getClass().getDeclaredMethod(clickMethod,View.class);
            if (m != null){
                m.invoke(handler,params);
            }else {
                AbLogUtil.e(handler.getClass(),"no such method :"+clickMethod);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        invokeItemClickMethod(handler,itemClickMethod,parent,view,position,id);
    }

    private void invokeItemClickMethod(Object handler,String methodName,Object... params){
        if (handler == null) return;
        Method m = null;
        try {
            m = handler.getClass().getDeclaredMethod(methodName,AdapterView.class,View.class,int.class,long.class);
            if (m != null){
                m.invoke(handler,params);
            }else {
                AbLogUtil.e(handler.getClass(),"no such method :"+methodName);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return invokeItemLongClick(handler,itemLongClickMethod,parent,view,position,id);
    }

    private boolean invokeItemLongClick(Object handler,String methodName,Object... params){
        if(handler == null){
            return false;
        }
        Method m = null;
        try {
            m = handler.getClass().getDeclaredMethod(methodName,AdapterView.class,View.class,int.class,long.class);
            if (m != null){
                try {
                    Object o =  m.invoke(handler,params);
                    return o == null? false:Boolean.valueOf(o.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        invokeItemSelected(handler,itemSelectClickMethod,parent,view,position,id);
    }

    private void invokeItemSelected(Object handler,String methodName,Object... params){
        if (handler == null) return;
        Method m = null;
        try {
            m = handler.getClass().getDeclaredMethod(methodName,AdapterView.class,View.class,int.class,long.class);
            if (m != null){
                m.invoke(handler,params);
            }else {
                AbLogUtil.e(handler.getClass(),"no such method :"+methodName);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        invokeNothingSeleted(handler,itemNothingSelectMethod,parent);
    }

    private void invokeNothingSeleted(Object handler,String methodName,Object... params){
        if (handler == null) return;
        Method m = null;
        try {
            m = handler.getClass().getDeclaredMethod(methodName,AdapterView.class);
            if (m != null){
                m.invoke(handler,params);
            }else {
                AbLogUtil.e(handler.getClass(),"no such method :"+methodName);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return invokeLongClick(handler,longClickMethod,v);
    }

    private boolean invokeLongClick(Object handler,String methodName,Object... params){
        if (handler == null) return false;
        Method m = null;
        try {
            m = handler.getClass().getDeclaredMethod(methodName,View.class);
            if (m != null)
            {
                Object o = m.invoke(handler, params);
                return o == null? false:Boolean.valueOf(o.toString());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
