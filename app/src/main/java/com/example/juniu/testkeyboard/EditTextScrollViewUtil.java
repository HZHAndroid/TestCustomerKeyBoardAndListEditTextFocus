package com.example.juniu.testkeyboard;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;

/**
 * @author juniu
 * @date 18/4/19
 * @description EditText和ScrollView/ListView/RecyclerView等的焦点问题工具类
 */

public class EditTextScrollViewUtil {
    /**
     * 重点：a. 为EditText设置tag的时候，放在视图生成完成后的第一行
     *
     *      b. 在列表中，视图都是做缓存操作的，所有视图的监听都建议写在视图第一次生成时候；
     *         如果列表是AbsListView或者AbsListView的子类，在它的适配器中的getView方法进行如下操作
     *
     *         public View getView(final int i, View view, ViewGroup viewGroup) {
     *              if (view == null) {
     *                  // 在这里设置所有视图的监听
     *              }
     *         }
     *
     *         如果是RecyclerView或者子类，在它的适配器中的onBindViewHolder方法进行如下操作,
     *         // 编写一个集合，用于缓存ViewHolder,也是用来区分视图的新建或者缓存
     *         private List<ViewHolder> mViewHolderList = new ArrayList<>();
     *         public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
     *              if (!mViewHolderList.contains(holder)) {
     *                  mViewHolderList.add(holder);
     *                  // 在这里设置所有视图的监听
     *              }
     *         }
     *
     *      c. 如果调用setScrollAutoHideKeyboard，会为滚动视图（ListView）设置setOnScrollListener，
     *      如果没有调用，则需要自己设置，然后调用CallBackListener对象的同名方法
     *
     * 1.为列表中的EditText设置addTextChangedListener
     *
     * 2.在addTextChangedListener的afterTextChanged进行数据存储操作
     *    Object tagObj = editText.getTag();
     *    if (tabOjb != null) {
     *        int position = (int) tagObj;
     *        // 通过角标操作数据
     *    }
     *
     * 3.为列表中的EditText设置setOnFocusChangeListener，用于获取当前的焦点视图
     *
     * 4.在列表初始化或者滑动刷新的过程中，用当前的焦点视图（EditTExt）与列表中的EditText
     *   做对比。
     *   if (currentFocusView == editText) {
     *        currentFocusView = editText;
     *        editText.requestFocus();
     *   } else {
     *        editText.clearFocus();
     *   }
     *
     * 5.在Activity/Fragment销毁的时候调用onDestroy方法
     *
     */


    /**
     * Activity
     */
    private Activity mActivity;

    /**
     * 监听回调
     */
    private CallBackListener mCallBackListener;

    /**
     * 键盘监听事件
     */
    private SoftKeyBoardListener mSoftKeyBoardListener;

    /**
     * AbsListView的监听
     */
    private AbsListView.OnScrollListener mAbsListViewScrollListener;

    /**
     * RecyclerView的滚动监听方法
     */
    private RecyclerView.OnScrollListener mRecyclerViewOnScrollListener;


    public EditTextScrollViewUtil(@NonNull Activity activity,
                                  @NonNull CallBackListener callBackListener) {
        this.mActivity = activity;
        this.mCallBackListener = callBackListener;
        initDefault();
    }

    /**
     * 初始化默认
     */
    private void initDefault() {
        this.mSoftKeyBoardListener = SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mCallBackListener.onKeyBoardShow(height);
            }

            @Override
            public void keyBoardHide(int height) {
                mCallBackListener.onKeyBoardHide(height);
            }
        });

        this.mAbsListViewScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                EditText editText = mCallBackListener.onScrollStateChanged(absListView, i);
                if (editText != null) {
                    hideSoftInput(mActivity, editText);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        };


        this.mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                EditText editText = mCallBackListener.onScrollStateChanged(recyclerView, newState);
                if (editText != null) {
                    hideSoftInput(mActivity, editText);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };

    }

    /**
     * 滚动列表时自动隐藏键盘(如果没有设置，则需要自己设置setOnScrollListener，
     * 调用本类的CallBackListener对象的同名方法,否则滚动没有监听)
     *
     * @param absListView 滚动视图
     */
    public void setScrollAutoHideKeyboard(AbsListView absListView) {
        if (absListView != null) {
            absListView.setOnScrollListener(this.mAbsListViewScrollListener);
        }
    }

    /**
     * 滚动列表时自动隐藏键盘(如果没有设置，则需要自己设置setOnScrollChangeListener或者setOnScrollListener
     * 调用本类的CallBackListener对象的同名方法,否则滚动没有监听)
     *
     * @param recyclerView 滚动视图
     */
    public void setScrollAutoHideKeyboard(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.addOnScrollListener(this.mRecyclerViewOnScrollListener);
    }

    /**
     * 获取回调事件
     *
     * @return
     */
    public CallBackListener getCallBackListener() {
        return mCallBackListener;
    }

    /**
     * 动态隐藏软键盘
     *
     * @param context 上下文
     * @param edit    输入框
     */
    public void hideSoftInput(Context context, EditText edit) {
        if (edit == null) {
            return;
        }
        try {
            edit.clearFocus();
            InputMethodManager inputmanger = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    public void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 销毁方法
     */
    public void onDestroy() {
        if (mSoftKeyBoardListener != null) {
            mSoftKeyBoardListener.onDestroy();
        }
    }

    public interface CallBackListener {
        /**
         * 键盘弹出
         *
         * @param height 键盘(弹出／隐藏)时候视图变化的值
         */
        void onKeyBoardShow(int height);

        /**
         * 键盘隐藏(可以进行去除焦点和置空等操作)
         *
         * @param height 键盘(弹出／隐藏)时候视图变化的值
         */
        void onKeyBoardHide(int height);

        /**
         * AbsListView.OnScrollListener的同名方法(onScrollStateChanged),滚动状态改变时候触发
         *
         * @param absListView AbsListView视图对象
         * @param state       滚动状态
         * @return 返回当前拥有焦点的EditText，有返回值会隐藏键盘
         */
        EditText onScrollStateChanged(AbsListView absListView, int state);

        /**
         * RecyclerView.OnScrollListener的同名方法(onScrollStateChanged),滚动状态改变时候触发
         *
         * @param recyclerView RecyclerView视图对象
         * @param newState     新的状态
         * @return
         */
        EditText onScrollStateChanged(RecyclerView recyclerView, int newState);
    }
}
