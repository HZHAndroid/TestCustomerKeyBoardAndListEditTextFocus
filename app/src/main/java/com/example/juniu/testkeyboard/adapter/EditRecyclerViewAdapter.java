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
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.juniu.testkeyboard.EditTextScrollViewUtil;
import com.example.juniu.testkeyboard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author juniu
 * @date 18/4/19
 * @description
 */

public class EditRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;

    /**
     * 缓存ViewHolder，如果集合中不存在的ViewHolder,
     * 则调用ViewHolder的相应视图设置监听，然后添加进集合
     */
    private List<ViewHolder> mViewHolderList = new ArrayList<>();

    private Map<Integer, String> mTextMap = new HashMap<>();
    private EditText mFocusEt;
    private EditTextScrollViewUtil mEditTextScrollViewUtil;

    public EditRecyclerViewAdapter(Context context) {
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
                        return null;
                    }

                    @Override
                    public EditText onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        return mFocusEt;
                    }
                });

    }

    public void setScrollAutoHideKeyboard(RecyclerView recyclerView) {
        mEditTextScrollViewUtil.setScrollAutoHideKeyboard(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_edit_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        if (!mViewHolderList.contains(holder)) {
            mViewHolderList.add(holder);
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
                    Object tagObj = holder.myKeyboard.getTag();
                    if (tagObj == null) {
                        return;
                    }
                    int position = (int) tagObj;
                    mTextMap.put(position, editable.toString());
                }
            };

            // 初始化焦点改变事件
            holder.myKeyboard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        mFocusEt = (EditText) view;
                    }
                }
            });

            // 第一次初始化的时候才添加，否则会错乱
            holder.myKeyboard.addTextChangedListener(textWatcher);
        }
        // 初始化完成就要设置tag，否则会出问题
        holder.myKeyboard.setTag(position);

        String text = mTextMap.get(position);

        holder.myKeyboard.setHint("数字");

        if (mFocusEt == holder.myKeyboard) {
            holder.myKeyboard.requestFocus();
            mFocusEt = holder.myKeyboard;
        } else {
            holder.myKeyboard.clearFocus();
        }
        holder.myKeyboard.setText(text == null ? "" : text);
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentLl;
        EditText myKeyboard;

        public ViewHolder(View itemView) {
            super(itemView);
            parentLl = itemView.findViewById(R.id.ll_parent);
            parentLl.setFocusable(true);
            parentLl.setFocusableInTouchMode(true);
            myKeyboard = itemView.findViewById(R.id.mkb_item_list);
        }
    }
}
