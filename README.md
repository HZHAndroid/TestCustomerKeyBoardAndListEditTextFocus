# TestCustomerKeyBoardAndListEditTextFocus
自定义键盘，可以自动向上顶起，解决ListView/RecyclerView使用系统键盘时候的焦点和值错位的问题，滚动视图自动隐藏键盘。

# 操作
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
