# TestCustomerKeyBoardAndListEditTextFocus
自定义键盘，可以自动向上顶起，解决ListView/RecyclerView使用系统键盘时候的焦点和值错位的问题，滚动视图自动隐藏键盘。

### 操作(EditTextScrollViewUtil的说明)
/**
     * 重点：a. 为EditText设置tag的时候，放在视图生成完成后的第一行<br />
     *
     *      b. 在列表中，视图都是做缓存操作的，所有视图的监听都建议写在视图第一次生成时候；<br />
     *         如果列表是AbsListView或者AbsListView的子类，在它的适配器中的getView方法进行如下操作<br />
     *         <br />
     *         public View getView(final int i, View view, ViewGroup viewGroup) {<br />
     *              if (view == null) {<br />
     *                  // 在这里设置所有视图的监听<br />
     *              }<br />
     *         }<br />
     *         <br />
     *         如果是RecyclerView或者子类，在它的适配器中的onBindViewHolder方法进行如下操作,<br />
     *         // 编写一个集合，用于缓存ViewHolder,也是用来区分视图的新建或者缓存<br />
     *         private List<ViewHolder> mViewHolderList = new ArrayList<>();<br />
     *         public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {<br />
     *              if (!mViewHolderList.contains(holder)) {<br />
     *                  mViewHolderList.add(holder);<br />
     *                  // 在这里设置所有视图的监听<br />
     *              }<br />
     *         }<br />
     *         <br />
     *      c. 如果调用setScrollAutoHideKeyboard，会为滚动视图（ListView）设置setOnScrollListener，<br />
     *      如果没有调用，则需要自己设置，然后调用CallBackListener对象的同名方法<br />
     *      <br />
     * 1.为列表中的EditText设置addTextChangedListener<br />
     * <br />
     * 2.在addTextChangedListener的afterTextChanged进行数据存储操作<br />
     *    Object tagObj = editText.getTag();<br />
     *    if (tabOjb != null) {<br />
     *        int position = (int) tagObj;<br />
     *        // 通过角标操作数据<br />
     *    }<br />
     *  <br />
     * 3.为列表中的EditText设置setOnFocusChangeListener，用于获取当前的焦点视图<br />
     *  <br />
     * 4.在列表初始化或者滑动刷新的过程中，用当前的焦点视图（EditTExt）与列表中的EditText<br />
     *   做对比。<br />
     *   if (currentFocusView == editText) {<br />
     *        currentFocusView = editText;<br />
     *        editText.requestFocus();<br />
     *   } else {<br />
     *        editText.clearFocus();<br />
     *   }<br />
     *  <br />
     * 5.在Activity/Fragment销毁的时候调用onDestroy方法<br />
     * <br />
     */
### 提示
** 1.重点查看列表的适配器(EditListViewAdapter: ListView适配器 EditRecyclerViewAdapter: RecyclerView适配器),<br />
    因为有些逻辑性的细节，跟具体的业务有关系，本项目的适配器相当于一个模版例子，可以查看适配器的具体源码。
