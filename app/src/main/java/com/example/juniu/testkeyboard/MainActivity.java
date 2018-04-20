package com.example.juniu.testkeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.example.juniu.testkeyboard.adapter.EditListViewAdapter;
import com.example.juniu.testkeyboard.adapter.EditRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private ListView mListView;
    private RecyclerView mRecyclerView;
    private MyKeyboard mMyKeyboard;

    private void init() {
        mListView = this.findViewById(R.id.lv_list);
        mRecyclerView = this.findViewById(R.id.rv_list);
        mMyKeyboard = this.findViewById(R.id.mkb_customer);


        EditListViewAdapter editListAdapter = new EditListViewAdapter(this);
        mListView.setAdapter(editListAdapter);
        editListAdapter.setListView(mListView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        EditRecyclerViewAdapter editRecyclerViewAdapter = new EditRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(editRecyclerViewAdapter);
        editRecyclerViewAdapter.setScrollAutoHideKeyboard(mRecyclerView);


        this.findViewById(R.id.btn_myKeyboard).setOnClickListener(this);
        this.findViewById(R.id.btn_listview).setOnClickListener(this);
        this.findViewById(R.id.btn_recyclerView).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_myKeyboard: {
                mMyKeyboard.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
            }
            break;
            case R.id.btn_listview: {
                mMyKeyboard.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            break;
            case R.id.btn_recyclerView: {
                mMyKeyboard.setVisibility(View.GONE);
                mListView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            break;
            default:
                break;
        }
    }


//    private class EditListAdapter extends BaseAdapter {
//        private Context mContext;
//        private LayoutInflater mInflater;
//
//        private Map<Integer, String> mTextMap = new HashMap<>();
//
//        private EditText mCurrentEditText;
//
//        private EditTextScrollViewUtil mEditTextScrollViewUtil;
//
//        public EditListAdapter(Context context) {
//            this.mContext = context;
//            this.mInflater = LayoutInflater.from(context);
//            mEditTextScrollViewUtil = new EditTextScrollViewUtil((Activity) mContext, new EditTextScrollViewUtil.CallBackListener() {
//                @Override
//                public void onKeyBoardShow(int height) {
//
//                }
//
//                @Override
//                public void onKeyBoardHide(int height) {
//                    if (mCurrentEditText != null) {
//                        mCurrentEditText.clearFocus();
//                        mCurrentEditText = null;
//                    }
//                }
//
//                @Override
//                public EditText onScrollStateChanged(AbsListView absListView, int state) {
//                    return mCurrentEditText;
//                }
//            });
//        }
//
//
//        public void setListView(ListView listView) {
//            if (null != listView) {
//                if (mEditTextScrollViewUtil != null) {
//                    mEditTextScrollViewUtil.setScrollAutoHideKeyboard(listView);
//                }
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 100;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//
//        @Override
//        public View getView(final int i, View view, ViewGroup viewGroup) {
//            final ViewHolder mViewHolder;
//            if (view == null) {
//                view = mInflater.inflate(R.layout.item_edit_list, null);
//                mViewHolder = new ViewHolder(view);
//                TextWatcher textWatcher = new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//                        Object tagObj = mViewHolder.myKeyboard.getTag();
//                        if (tagObj == null) {
//                            return;
//                        }
//                        int position = (int) tagObj;
//                        mTextMap.put(position, editable.toString());
//                    }
//                };
//
//
//                mViewHolder.myKeyboard.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View view, MotionEvent motionEvent) {
//                        mCurrentEditText = (EditText) view;
//                        return false;
//                    }
//                });
//                // 第一次初始化的时候才添加，否则会错乱
//                mViewHolder.myKeyboard.addTextChangedListener(textWatcher);
//            } else {
//                mViewHolder = (ViewHolder) view.getTag();
//            }
//            // 初始化完成就要设置tag，否则会出问题
//            mViewHolder.myKeyboard.setTag(i);
//
//            String text = mTextMap.get(i);
//
//            mViewHolder.myKeyboard.setHint("数字");
//
//            if (mCurrentEditText == mViewHolder.myKeyboard) {
//                mCurrentEditText = mViewHolder.myKeyboard;
//                mViewHolder.myKeyboard.requestFocus();
//            } else {
//                mViewHolder.myKeyboard.clearFocus();
//            }
//            mViewHolder.myKeyboard.setText(text == null ? "" : text);
//
//
//            return view;
//        }
//
//        private class ViewHolder {
//            EditText myKeyboard;
//
//            public ViewHolder(View itemView) {
//                itemView.setTag(this);
//                myKeyboard = itemView.findViewById(R.id.mkb_item_list);
//            }
//        }
//
//    }

}
