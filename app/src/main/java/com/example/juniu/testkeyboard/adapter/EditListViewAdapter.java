package com.example.juniu.testkeyboard.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.juniu.testkeyboard.EditTextScrollViewUtil;
import com.example.juniu.testkeyboard.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author juniu
 * @date 18/4/19
 * @description ListView中有EditText焦点问题测试适配器
 */

public class EditListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    private Map<Integer, String> mTextMap = new HashMap<>();

    private EditText mFocusEt;

    private EditTextScrollViewUtil mEditTextScrollViewUtil;

    public EditListViewAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mEditTextScrollViewUtil = new EditTextScrollViewUtil((Activity) mContext,
                new EditTextScrollViewUtil.CallBackListener() {
                    @Override
                    public void onKeyBoardShow(int height) {

                    }

                    @Override
                    public void onKeyBoardHide(int height) {
                        if (mFocusEt != null) {
                            mFocusEt.clearFocus();
                            mFocusEt = null;
                        }
                    }

                    @Override
                    public EditText onScrollStateChanged(AbsListView absListView, int state) {
                        return mFocusEt;
                    }

                    @Override
                    public EditText onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        return null;
                    }
                });
    }


    public void setListView(ListView listView) {
        if (null != listView) {
            if (mEditTextScrollViewUtil != null) {
                mEditTextScrollViewUtil.setScrollAutoHideKeyboard(listView);
            }
        }
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder mViewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_edit_list, null);
            mViewHolder = new ViewHolder(view);
            // 初始化监听事件
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Object tagObj = mViewHolder.myKeyboard.getTag();
                    if (tagObj == null) {
                        return;
                    }
                    int position = (int) tagObj;
                    mTextMap.put(position, editable.toString());
                }
            };

            // 初始化焦点改变事件
            mViewHolder.myKeyboard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        mFocusEt = (EditText) view;
                    }
                }
            });

            // 第一次初始化的时候才添加，否则会错乱
            mViewHolder.myKeyboard.addTextChangedListener(textWatcher);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        // 初始化完成就要设置tag，否则会出问题
        mViewHolder.myKeyboard.setTag(i);

        String text = mTextMap.get(i);

        mViewHolder.myKeyboard.setHint("数字");

        if (mFocusEt == mViewHolder.myKeyboard) {
            mFocusEt = mViewHolder.myKeyboard;
            mViewHolder.myKeyboard.requestFocus();
        } else {
            mViewHolder.myKeyboard.clearFocus();
        }
        mViewHolder.myKeyboard.setText(text == null ? "" : text);


        return view;
    }

    private class ViewHolder {
        EditText myKeyboard;

        public ViewHolder(View itemView) {
            itemView.setTag(this);
            myKeyboard = itemView.findViewById(R.id.mkb_item_list);
        }
    }

}