///**
// * Copyright 2013 Joan Zapata
// *
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.bobchouwb.ui.test
//
//import android.animation.Animator
//import android.content.Context
//import android.support.annotation.IntDef
//import android.support.annotation.IntRange
//import android.support.v7.widget.GridLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.support.v7.widget.RecyclerView.LayoutParams
//import android.support.v7.widget.StaggeredGridLayoutManager
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.Interpolator
//import android.view.animation.LinearInterpolator
//import android.widget.FrameLayout
//import android.widget.LinearLayout
//
//import com.chad.library.adapter.base.animation.AlphaInAnimation
//import com.chad.library.adapter.base.animation.BaseAnimation
//import com.chad.library.adapter.base.animation.ScaleInAnimation
//import com.chad.library.adapter.base.animation.SlideInBottomAnimation
//import com.chad.library.adapter.base.animation.SlideInLeftAnimation
//import com.chad.library.adapter.base.animation.SlideInRightAnimation
//import com.chad.library.adapter.base.entity.IExpandable
//import com.chad.library.adapter.base.loadmore.LoadMoreView
//import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
//
//import java.lang.annotation.Retention
//import java.lang.annotation.RetentionPolicy
//import java.lang.reflect.Constructor
//import java.lang.reflect.InvocationTargetException
//import java.lang.reflect.Modifier
//import java.lang.reflect.ParameterizedType
//import java.lang.reflect.Type
//import java.util.ArrayList
//
//import android.view.ViewGroup.LayoutParams.MATCH_PARENT
//import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
//
//
///**
// * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
// */
//abstract class BaseQuickAdapter<T, K : BaseViewHolder>
///**
// * Same as QuickAdapter#QuickAdapter(Context,int) but with
// * some initialization data.
//
// * @param layoutResId The layout resource id of each item.
// * *
// * @param data        A new list is created out of this one to avoid mutable list
// */
//@JvmOverloads constructor(layoutResId: Int, data: List<T>? = null) : RecyclerView.Adapter<K>() {
//
//    //load more
//    private var mNextLoadEnable = false
//    /**
//     * Returns the enabled status for load more.
//
//     * @return True if load more is enabled, false otherwise.
//     */
//    var isLoadMoreEnable = false
//        private set
//    /**
//     * @return Whether the Adapter is actively showing load
//     * * progress.
//     */
//    var isLoading = false
//        private set
//    private var mLoadMoreView: LoadMoreView = SimpleLoadMoreView()
//    private var mRequestLoadMoreListener: RequestLoadMoreListener? = null
//
//    @IntDef(ALPHAIN.toLong(), SCALEIN.toLong(), SLIDEIN_BOTTOM.toLong(), SLIDEIN_LEFT.toLong(), SLIDEIN_RIGHT.toLong())
//    @Retention(RetentionPolicy.SOURCE)
//    annotation class AnimationType
//
//    private var mFirstOnlyEnable = true
//    private var mOpenAnimationEnable = false
//    private val mInterpolator = LinearInterpolator()
//    private var mDuration = 300
//    private var mLastPosition = -1
//
//    private var mCustomAnimation: BaseAnimation? = null
//    private var mSelectAnimation: BaseAnimation = AlphaInAnimation()
//    //header footer
//    /**
//     * Return root layout of header
//     */
//
//    var headerLayout: LinearLayout? = null
//        private set
//    /**
//     * Return root layout of footer
//     */
//    var footerLayout: LinearLayout? = null
//        private set
//    //empty
//    private var mEmptyLayout: FrameLayout? = null
//    private var mIsUseEmpty = true
//    private var mHeadAndEmptyEnable: Boolean = false
//    private var mFootAndEmptyEnable: Boolean = false
//    protected var mContext: Context
//    protected var mLayoutResId: Int = 0
//    protected var mLayoutInflater: LayoutInflater
//    protected var mData: MutableList<T>? = null
//
//    fun setOnLoadMoreListener(requestLoadMoreListener: RequestLoadMoreListener) {
//        this.mRequestLoadMoreListener = requestLoadMoreListener
//        mNextLoadEnable = true
//        isLoadMoreEnable = true
//        isLoading = false
//    }
//
//    fun setNotDoAnimationCount(count: Int) {
//        mLastPosition = count
//    }
//
//    /**
//     * Set custom load more
//
//     * @param loadingView
//     */
//    fun setLoadMoreView(loadingView: LoadMoreView) {
//        this.mLoadMoreView = loadingView
//    }
//
//    /**
//     * Load more view count
//
//     * @return 0 or 1
//     */
//    val loadMoreViewCount: Int
//        get() {
//            if (mRequestLoadMoreListener == null || !isLoadMoreEnable) {
//                return 0
//            }
//            if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone) {
//                return 0
//            }
//            if (mData!!.size == 0) {
//                return 0
//            }
//            return 1
//        }
//
//    /**
//     * Refresh end, no more data
//
//     * @param gone if true gone the load more view
//     */
//    @JvmOverloads fun loadMoreEnd(gone: Boolean = false) {
//        if (loadMoreViewCount == 0) {
//            return
//        }
//        isLoading = false
//        mNextLoadEnable = false
//        mLoadMoreView.setLoadMoreEndGone(gone)
//        if (gone) {
//            notifyItemRemoved(headerLayoutCount + mData!!.size + footerLayoutCount)
//        } else {
//            mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_END
//            notifyItemChanged(headerLayoutCount + mData!!.size + footerLayoutCount)
//        }
//    }
//
//    /**
//     * Refresh complete
//     */
//    fun loadMoreComplete() {
//        if (loadMoreViewCount == 0) {
//            return
//        }
//        isLoading = false
//        mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
//        notifyItemChanged(headerLayoutCount + mData!!.size + footerLayoutCount)
//    }
//
//    /**
//     * Refresh failed
//     */
//    fun loadMoreFail() {
//        if (loadMoreViewCount == 0) {
//            return
//        }
//        isLoading = false
//        mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_FAIL
//        notifyItemChanged(headerLayoutCount + mData!!.size + footerLayoutCount)
//    }
//
//    /**
//     * Set the enabled state of load more.
//
//     * @param enable True if load more is enabled, false otherwise.
//     */
//    fun setEnableLoadMore(enable: Boolean) {
//        val oldLoadMoreCount = loadMoreViewCount
//        isLoadMoreEnable = enable
//        val newLoadMoreCount = loadMoreViewCount
//
//        if (oldLoadMoreCount == 1) {
//            if (newLoadMoreCount == 0) {
//                notifyItemRemoved(headerLayoutCount + mData!!.size + footerLayoutCount)
//            }
//        } else {
//            if (newLoadMoreCount == 1) {
//                mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
//                notifyItemInserted(headerLayoutCount + mData!!.size + footerLayoutCount)
//            }
//        }
//    }
//
//    /**
//     * Sets the duration of the animation.
//
//     * @param duration The length of the animation, in milliseconds.
//     */
//    fun setDuration(duration: Int) {
//        mDuration = duration
//    }
//
//
//    init {
//        this.mData = data ?: ArrayList<T>()
//        if (layoutResId != 0) {
//            this.mLayoutResId = layoutResId
//        }
//    }
//
//    constructor(data: List<T>) : this(0, data) {}
//
//    /**
//     * setting up a new instance to data;
//
//     * @param data
//     */
//    fun setNewData(data: List<T>?) {
//        this.mData = data ?: ArrayList<T>()
//        if (mRequestLoadMoreListener != null) {
//            mNextLoadEnable = true
//            isLoadMoreEnable = true
//            isLoading = false
//            mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
//        }
//        mLastPosition = -1
//        notifyDataSetChanged()
//    }
//
//
//    /**
//     * insert  a item associated with the specified position of adapter
//
//     * @param position
//     * *
//     * @param item
//     * *
//     */
//    @Deprecated("")
//    @Deprecated("use {@link #addData(int, Object)} instead")
//    fun add(position: Int, item: T) {
//        addData(position, item)
//    }
//
//    /**
//     * add one new data in to certain location
//
//     * @param position
//     */
//    fun addData(position: Int, data: T) {
//        mData!!.add(position, data)
//        notifyItemInserted(position + headerLayoutCount)
//        compatibilityDataSizeChanged(1)
//    }
//
//    /**
//     * add one new data
//     */
//    fun addData(data: T) {
//        mData!!.add(data)
//        notifyItemInserted(mData!!.size + headerLayoutCount)
//        compatibilityDataSizeChanged(1)
//    }
//
//    /**
//     * remove the item associated with the specified position of adapter
//
//     * @param position
//     */
//    fun remove(position: Int) {
//        mData!!.removeAt(position)
//        notifyItemRemoved(position + headerLayoutCount)
//        compatibilityDataSizeChanged(0)
//    }
//
//    /**
//     * change data
//     */
//    fun setData(index: Int, data: T) {
//        mData!![index] = data
//        notifyItemChanged(index + headerLayoutCount)
//    }
//
//    /**
//     * add new data in to certain location
//
//     * @param position
//     */
//    fun addData(position: Int, data: List<T>) {
//        mData!!.addAll(position, data)
//        notifyItemRangeInserted(position + headerLayoutCount, data.size)
//        compatibilityDataSizeChanged(data.size)
//    }
//
//    /**
//     * additional data;
//
//     * @param newData
//     */
//    fun addData(newData: List<T>) {
//        this.mData!!.addAll(newData)
//        notifyItemRangeInserted(mData!!.size - newData.size + headerLayoutCount, newData.size)
//        compatibilityDataSizeChanged(newData.size)
//    }
//
//    /**
//     * compatible getLoadMoreViewCount and getEmptyViewCount may change
//
//     * @param size Need compatible data size
//     */
//    private fun compatibilityDataSizeChanged(size: Int) {
//        val dataSize = if (mData == null) 0 else mData!!.size
//        if (dataSize == size) {
//            notifyDataSetChanged()
//        }
//    }
//
//    /**
//     * Get the data of list
//
//     * @return
//     */
//    val data: List<T>
//        get() = mData
//
//    /**
//     * Get the data item associated with the specified position in the data set.
//
//     * @param position Position of the item whose data we want within the adapter's
//     * *                 data set.
//     * *
//     * @return The data at the specified position.
//     */
//    fun getItem(position: Int): T? {
//        if (position != -1)
//            return mData!![position]
//        else
//            return null
//    }
//
//    /**
//     * if setHeadView will be return 1 if not will be return 0.
//     * notice: Deprecated! Use [ViewGroup.getChildCount] of [.getHeaderLayout] to replace.
//
//     * @return
//     */
//    val headerViewsCount: Int
//        @Deprecated("")
//        get() = headerLayoutCount
//
//    /**
//     * if mFooterLayout will be return 1 or not will be return 0.
//     * notice: Deprecated! Use [ViewGroup.getChildCount] of [.getFooterLayout] to replace.
//
//     * @return
//     */
//    val footerViewsCount: Int
//        @Deprecated("")
//        get() = footerLayoutCount
//
//    /**
//     * if addHeaderView will be return 1, if not will be return 0
//     */
//    val headerLayoutCount: Int
//        get() {
//            if (headerLayout == null || headerLayout!!.childCount == 0) {
//                return 0
//            }
//            return 1
//        }
//
//    /**
//     * if addFooterView will be return 1, if not will be return 0
//     */
//    val footerLayoutCount: Int
//        get() {
//            if (footerLayout == null || footerLayout!!.childCount == 0) {
//                return 0
//            }
//            return 1
//        }
//
//    /**
//     * if show empty view will be return 1 or not will be return 0
//
//     * @return
//     */
//    val emptyViewCount: Int
//        get() {
//            if (mEmptyLayout == null || mEmptyLayout!!.childCount == 0) {
//                return 0
//            }
//            if (!mIsUseEmpty) {
//                return 0
//            }
//            if (mData!!.size != 0) {
//                return 0
//            }
//            return 1
//        }
//
//    override fun getItemCount(): Int {
//        var count: Int
//        if (emptyViewCount == 1) {
//            count = 1
//            if (mHeadAndEmptyEnable && headerLayoutCount != 0) {
//                count++
//            }
//            if (mFootAndEmptyEnable && footerLayoutCount != 0) {
//                count++
//            }
//        } else {
//            count = headerLayoutCount + mData!!.size + footerLayoutCount + loadMoreViewCount
//        }
//        return count
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        if (emptyViewCount == 1) {
//            val header = mHeadAndEmptyEnable && headerLayoutCount != 0
//            when (position) {
//                0 -> {
//                    if (header) {
//                        return HEADER_VIEW
//                    } else {
//                        return EMPTY_VIEW
//                    }
//                    if (header) {
//                        return EMPTY_VIEW
//                    } else {
//                        return FOOTER_VIEW
//                    }
//                    return FOOTER_VIEW
//                }
//                1 -> {
//                    if (header) {
//                        return EMPTY_VIEW
//                    } else {
//                        return FOOTER_VIEW
//                    }
//                    return FOOTER_VIEW
//                }
//                2 -> return FOOTER_VIEW
//                else -> return EMPTY_VIEW
//            }
//        }
//        autoLoadMore(position)
//        val numHeaders = headerLayoutCount
//        if (position < numHeaders) {
//            return HEADER_VIEW
//        } else {
//            var adjPosition = position - numHeaders
//            val adapterCount = mData!!.size
//            if (adjPosition < adapterCount) {
//                return getDefItemViewType(adjPosition)
//            } else {
//                adjPosition = adjPosition - adapterCount
//                val numFooters = footerLayoutCount
//                if (adjPosition < numFooters) {
//                    return FOOTER_VIEW
//                } else {
//                    return LOADING_VIEW
//                }
//            }
//        }
//    }
//
//    protected fun getDefItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {
//        var baseViewHolder: K? = null
//        this.mContext = parent.context
//        this.mLayoutInflater = LayoutInflater.from(mContext)
//        when (viewType) {
//            LOADING_VIEW -> baseViewHolder = getLoadingView(parent)
//            HEADER_VIEW -> baseViewHolder = createBaseViewHolder(headerLayout)
//            EMPTY_VIEW -> baseViewHolder = createBaseViewHolder(mEmptyLayout)
//            FOOTER_VIEW -> baseViewHolder = createBaseViewHolder(footerLayout)
//            else -> baseViewHolder = onCreateDefViewHolder(parent, viewType)
//        }
//        return baseViewHolder
//
//    }
//
//
//    private fun getLoadingView(parent: ViewGroup): K {
//        val view = getItemView(mLoadMoreView.layoutId, parent)
//        val holder = createBaseViewHolder(view)
//        holder.itemView.setOnClickListener(View.OnClickListener {
//            if (mLoadMoreView.loadMoreStatus == LoadMoreView.STATUS_FAIL) {
//                mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
//                notifyItemChanged(headerLayoutCount + mData!!.size + footerLayoutCount)
//            }
//        })
//        return holder
//    }
//
//    /**
//     * Called when a view created by this adapter has been attached to a window.
//     * simple to solve item will layout using all
//     * [.setFullSpan]
//
//     * @param holder
//     */
//    override fun onViewAttachedToWindow(holder: K?) {
//        super.onViewAttachedToWindow(holder)
//        val type = holder!!.getItemViewType()
//        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW) {
//            setFullSpan(holder)
//        } else {
//            addAnimation(holder)
//        }
//    }
//
//    /**
//     * When set to true, the item will layout using all span area. That means, if orientation
//     * is vertical, the view will have full width; if orientation is horizontal, the view will
//     * have full height.
//     * if the hold view use StaggeredGridLayoutManager they should using all span area
//
//     * @param holder True if this item should traverse all spans.
//     */
//    protected fun setFullSpan(holder: RecyclerView.ViewHolder) {
//        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
//            val params = holder
//                    .itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
//            params.isFullSpan = true
//        }
//    }
//
//    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
//        super.onAttachedToRecyclerView(recyclerView)
//        val manager = recyclerView!!.layoutManager
//        if (manager is GridLayoutManager) {
//            val gridManager = manager
//            gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    val type = getItemViewType(position)
//                    if (mSpanSizeLookup == null)
//                        return if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW)
//                            gridManager.spanCount
//                        else
//                            1
//                    else
//                        return if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW)
//                            gridManager.spanCount
//                        else
//                            mSpanSizeLookup!!.getSpanSize(gridManager,
//                                    position - headerLayoutCount)
//                }
//            }
//        }
//    }
//
//    private val flag = true
//    private var mSpanSizeLookup: SpanSizeLookup? = null
//
//    interface SpanSizeLookup {
//        fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int
//    }
//
//    /**
//     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
//     */
//    fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup) {
//        this.mSpanSizeLookup = spanSizeLookup
//    }
//
//    /**
//     * To bind different types of holder and solve different the bind events
//
//     * @param holder
//     * *
//     * @param positions
//     * *
//     * @see .getDefItemViewType
//     */
//    override fun onBindViewHolder(holder: K, positions: Int) {
//        val viewType = holder.getItemViewType()
//
//        when (viewType) {
//            0 -> convert(holder, mData!![holder.getLayoutPosition() - headerLayoutCount])
//            LOADING_VIEW -> mLoadMoreView.convert(holder)
//            HEADER_VIEW -> {
//            }
//            EMPTY_VIEW -> {
//            }
//            FOOTER_VIEW -> {
//            }
//            else -> convert(holder, mData!![holder.getLayoutPosition() - headerLayoutCount])
//        }
//    }
//
//    protected fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): K {
//        return createBaseViewHolder(parent, mLayoutResId)
//    }
//
//    protected fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): K {
//        return createBaseViewHolder(getItemView(layoutResId, parent))
//    }
//
//    /**
//     * if you want to use subclass of BaseViewHolder in the adapter,
//     * you must override the method to create new ViewHolder.
//
//     * @param view view
//     * *
//     * @return new ViewHolder
//     */
//    protected fun createBaseViewHolder(view: View): K {
//        var temp: Class<*>? = javaClass
//        var z: Class<*>? = null
//        while (z == null && null != temp) {
//            z = getInstancedGenericKClass(temp)
//            temp = temp.superclass
//        }
//        val k = createGenericKInstance(z, view)
//        return k ?: BaseViewHolder(view) as K
//    }
//
//    /**
//     * try to create Generic K instance
//
//     * @param z
//     * *
//     * @param view
//     * *
//     * @return
//     */
//    private fun createGenericKInstance(z: Class<*>, view: View): K? {
//        try {
//            val constructor: Constructor<*>
//            val buffer = Modifier.toString(z.modifiers)
//            val className = z.name
//            // inner and unstatic class
//            if (className.contains("$") && !buffer.contains("static")) {
//                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
//                return constructor.newInstance(this, view) as K
//            } else {
//                constructor = z.getDeclaredConstructor(View::class.java)
//                return constructor.newInstance(view) as K
//            }
//        } catch (e: NoSuchMethodException) {
//            e.printStackTrace()
//        } catch (e: IllegalAccessException) {
//            e.printStackTrace()
//        } catch (e: InstantiationException) {
//            e.printStackTrace()
//        } catch (e: InvocationTargetException) {
//            e.printStackTrace()
//        }
//
//        return null
//    }
//
//    /**
//     * get generic parameter K
//
//     * @param z
//     * *
//     * @return
//     */
//    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
//        val type = z.genericSuperclass
//        if (type is ParameterizedType) {
//            val types = type.actualTypeArguments
//            for (temp in types) {
//                if (temp is Class<*>) {
//                    val tempClass = temp
//                    if (BaseViewHolder::class.java!!.isAssignableFrom(tempClass)) {
//                        return tempClass
//                    }
//                }
//            }
//        }
//        return null
//    }
//
//    /**
//     * @param header
//     * *
//     * @param index
//     * *
//     * @param orientation
//     */
//    @JvmOverloads fun addHeaderView(header: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL) {
//        var index = index
//        if (headerLayout == null) {
//            headerLayout = LinearLayout(header.context)
//            if (orientation == LinearLayout.VERTICAL) {
//                headerLayout!!.orientation = LinearLayout.VERTICAL
//                headerLayout!!.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
//            } else {
//                headerLayout!!.orientation = LinearLayout.HORIZONTAL
//                headerLayout!!.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
//            }
//        }
//        index = if (index >= headerLayout!!.childCount) -1 else index
//        headerLayout!!.addView(header, index)
//        if (headerLayout!!.childCount == 1) {
//            val position = headerViewPosition
//            if (position != -1) {
//                notifyItemInserted(position)
//            }
//        }
//    }
//
//    @JvmOverloads fun setHeaderView(header: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL) {
//        if (headerLayout == null || headerLayout!!.childCount <= index) {
//            addHeaderView(header, index, orientation)
//        } else {
//            headerLayout!!.removeViewAt(index)
//            headerLayout!!.addView(header, index)
//        }
//    }
//
//    /**
//     * Add footer view to mFooterLayout and set footer view position in mFooterLayout.
//     * When index = -1 or index >= child count in mFooterLayout,
//     * the effect of this method is the same as that of [.addFooterView].
//
//     * @param footer
//     * *
//     * @param index  the position in mFooterLayout of this footer.
//     * *               When index = -1 or index >= child count in mFooterLayout,
//     * *               the effect of this method is the same as that of [.addFooterView].
//     */
//    @JvmOverloads fun addFooterView(footer: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL) {
//        var index = index
//        if (footerLayout == null) {
//            footerLayout = LinearLayout(footer.context)
//            if (orientation == LinearLayout.VERTICAL) {
//                footerLayout!!.orientation = LinearLayout.VERTICAL
//                footerLayout!!.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
//            } else {
//                footerLayout!!.orientation = LinearLayout.HORIZONTAL
//                footerLayout!!.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
//            }
//        }
//        index = if (index >= footerLayout!!.childCount) -1 else index
//        footerLayout!!.addView(footer, index)
//        if (footerLayout!!.childCount == 1) {
//            val position = footerViewPosition
//            if (position != -1) {
//                notifyItemInserted(position)
//            }
//        }
//    }
//
//    @JvmOverloads fun setFooterView(header: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL) {
//        if (footerLayout == null || footerLayout!!.childCount <= index) {
//            addFooterView(header, index, orientation)
//        } else {
//            footerLayout!!.removeViewAt(index)
//            footerLayout!!.addView(header, index)
//        }
//    }
//
//    /**
//     * remove header view from mHeaderLayout.
//     * When the child count of mHeaderLayout is 0, mHeaderLayout will be set to null.
//
//     * @param header
//     */
//    fun removeHeaderView(header: View) {
//        if (headerLayoutCount == 0) return
//
//        headerLayout!!.removeView(header)
//        if (headerLayout!!.childCount == 0) {
//            val position = headerViewPosition
//            if (position != -1) {
//                notifyItemRemoved(position)
//            }
//        }
//    }
//
//    /**
//     * remove footer view from mFooterLayout,
//     * When the child count of mFooterLayout is 0, mFooterLayout will be set to null.
//
//     * @param footer
//     */
//    fun removeFooterView(footer: View) {
//        if (footerLayoutCount == 0) return
//
//        footerLayout!!.removeView(footer)
//        if (footerLayout!!.childCount == 0) {
//            val position = footerViewPosition
//            if (position != -1) {
//                notifyItemRemoved(position)
//            }
//        }
//    }
//
//    /**
//     * remove all header view from mHeaderLayout and set null to mHeaderLayout
//     */
//    fun removeAllHeaderView() {
//        if (headerLayoutCount == 0) return
//
//        headerLayout!!.removeAllViews()
//        val position = headerViewPosition
//        if (position != -1) {
//            notifyItemRemoved(position)
//        }
//    }
//
//    /**
//     * remove all footer view from mFooterLayout and set null to mFooterLayout
//     */
//    fun removeAllFooterView() {
//        if (footerLayoutCount == 0) return
//
//        footerLayout!!.removeAllViews()
//        val position = footerViewPosition
//        if (position != -1) {
//            notifyItemRemoved(position)
//        }
//    }
//
//    private //Return to header view notify position
//    val headerViewPosition: Int
//        get() {
//            if (emptyViewCount == 1) {
//                if (mHeadAndEmptyEnable) {
//                    return 0
//                }
//            } else {
//                return 0
//            }
//            return -1
//        }
//
//    private //Return to footer view notify position
//    val footerViewPosition: Int
//        get() {
//            if (emptyViewCount == 1) {
//                var position = 1
//                if (mHeadAndEmptyEnable && headerLayoutCount != 0) {
//                    position++
//                }
//                if (mFootAndEmptyEnable) {
//                    return position
//                }
//            } else {
//                return headerLayoutCount + mData!!.size
//            }
//            return -1
//        }
//
//    fun setEmptyView(layoutResId: Int, viewGroup: ViewGroup) {
//        val view = LayoutInflater.from(viewGroup.context).inflate(layoutResId, viewGroup, false)
//        emptyView = view
//    }
//
//    /**
//     * Call before [RecyclerView.setAdapter]
//
//     * @param isHeadAndEmpty false will not show headView if the data is empty true will show emptyView and headView
//     */
//    fun setHeaderAndEmpty(isHeadAndEmpty: Boolean) {
//        setHeaderFooterEmpty(isHeadAndEmpty, false)
//    }
//
//    /**
//     * set emptyView show if adapter is empty and want to show headview and footview
//     * Call before [RecyclerView.setAdapter]
//
//     * @param isHeadAndEmpty
//     * *
//     * @param isFootAndEmpty
//     */
//    fun setHeaderFooterEmpty(isHeadAndEmpty: Boolean, isFootAndEmpty: Boolean) {
//        mHeadAndEmptyEnable = isHeadAndEmpty
//        mFootAndEmptyEnable = isFootAndEmpty
//    }
//
//    /**
//     * Set whether to use empty view
//
//     * @param isUseEmpty
//     */
//    fun isUseEmpty(isUseEmpty: Boolean) {
//        mIsUseEmpty = isUseEmpty
//    }
//
//    /**
//     * When the current adapter is empty, the BaseQuickAdapter can display a special view
//     * called the empty view. The empty view is used to provide feedback to the user
//     * that no data is available in this AdapterView.
//
//     * @return The view to show if the adapter is empty.
//     */
//    var emptyView: View
//        get() = mEmptyLayout
//        set(emptyView) {
//            var insert = false
//            if (mEmptyLayout == null) {
//                mEmptyLayout = FrameLayout(emptyView.context)
//                val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//                val lp = emptyView.layoutParams
//                if (lp != null) {
//                    layoutParams.width = lp.width
//                    layoutParams.height = lp.height
//                }
//                mEmptyLayout!!.layoutParams = layoutParams
//                insert = true
//            }
//            mEmptyLayout!!.removeAllViews()
//            mEmptyLayout!!.addView(emptyView)
//            mIsUseEmpty = true
//            if (insert) {
//                if (emptyViewCount == 1) {
//                    var position = 0
//                    if (mHeadAndEmptyEnable && headerLayoutCount != 0) {
//                        position++
//                    }
//                    notifyItemInserted(position)
//                }
//            }
//        }
//
//    private var mAutoLoadMoreSize = 1
//
//    fun setAutoLoadMoreSize(autoLoadMoreSize: Int) {
//        if (autoLoadMoreSize > 1) {
//            mAutoLoadMoreSize = autoLoadMoreSize
//        }
//    }
//
//    private fun autoLoadMore(position: Int) {
//        if (loadMoreViewCount == 0) {
//            return
//        }
//        if (position < itemCount - mAutoLoadMoreSize) {
//            return
//        }
//        if (mLoadMoreView.loadMoreStatus != LoadMoreView.STATUS_DEFAULT) {
//            return
//        }
//        mLoadMoreView.loadMoreStatus = LoadMoreView.STATUS_LOADING
//        if (!isLoading) {
//            isLoading = true
//            mRequestLoadMoreListener!!.onLoadMoreRequested()
//        }
//    }
//
//
//    /**
//     * add animation when you want to show time
//
//     * @param holder
//     */
//    private fun addAnimation(holder: RecyclerView.ViewHolder) {
//        if (mOpenAnimationEnable) {
//            if (!mFirstOnlyEnable || holder.layoutPosition > mLastPosition) {
//                var animation: BaseAnimation? = null
//                if (mCustomAnimation != null) {
//                    animation = mCustomAnimation
//                } else {
//                    animation = mSelectAnimation
//                }
//                for (anim in animation!!.getAnimators(holder.itemView)) {
//                    startAnim(anim, holder.layoutPosition)
//                }
//                mLastPosition = holder.layoutPosition
//            }
//        }
//    }
//
//    /**
//     * set anim to start when loading
//
//     * @param anim
//     * *
//     * @param index
//     */
//    protected fun startAnim(anim: Animator, index: Int) {
//        anim.setDuration(mDuration.toLong()).start()
//        anim.interpolator = mInterpolator
//    }
//
//    /**
//     * @param layoutResId ID for an XML layout resource to load
//     * *
//     * @param parent      Optional view to be the parent of the generated hierarchy or else simply an object that
//     * *                    provides a set of LayoutParams values for root of the returned
//     * *                    hierarchy
//     * *
//     * @return view will be return
//     */
//    protected fun getItemView(layoutResId: Int, parent: ViewGroup): View {
//        return mLayoutInflater.inflate(layoutResId, parent, false)
//    }
//
//
//    interface RequestLoadMoreListener {
//
//        fun onLoadMoreRequested()
//
//    }
//
//
//    /**
//     * Set the view animation type.
//
//     * @param animationType One of [.ALPHAIN], [.SCALEIN], [.SLIDEIN_BOTTOM],
//     * *                      [.SLIDEIN_LEFT], [.SLIDEIN_RIGHT].
//     */
//    fun openLoadAnimation(@AnimationType animationType: Int) {
//        this.mOpenAnimationEnable = true
//        mCustomAnimation = null
//        when (animationType) {
//            ALPHAIN -> mSelectAnimation = AlphaInAnimation()
//            SCALEIN -> mSelectAnimation = ScaleInAnimation()
//            SLIDEIN_BOTTOM -> mSelectAnimation = SlideInBottomAnimation()
//            SLIDEIN_LEFT -> mSelectAnimation = SlideInLeftAnimation()
//            SLIDEIN_RIGHT -> mSelectAnimation = SlideInRightAnimation()
//            else -> {
//            }
//        }
//    }
//
//    /**
//     * Set Custom ObjectAnimator
//
//     * @param animation ObjectAnimator
//     */
//    fun openLoadAnimation(animation: BaseAnimation) {
//        this.mOpenAnimationEnable = true
//        this.mCustomAnimation = animation
//    }
//
//    /**
//     * To open the animation when loading
//     */
//    fun openLoadAnimation() {
//        this.mOpenAnimationEnable = true
//    }
//
//    /**
//     * [.addAnimation]
//
//     * @param firstOnly true just show anim when first loading false show anim when load the data every time
//     */
//    fun isFirstOnly(firstOnly: Boolean) {
//        this.mFirstOnlyEnable = firstOnly
//    }
//
//    /**
//     * Implement this method and use the helper to adapt the view to the given item.
//
//     * @param helper A fully initialized helper.
//     * *
//     * @param item   The item that needs to be displayed.
//     */
//    protected abstract fun convert(helper: K, item: T)
//
//    /**
//     * Get the row id associated with the specified position in the list.
//
//     * @param position The position of the item within the adapter's data set whose row id we want.
//     * *
//     * @return The id of the item at the specified position.
//     */
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    private fun recursiveExpand(position: Int, list: List<*>): Int {
//        var count = 0
//        var pos = position + list.size - 1
//        var i = list.size - 1
//        while (i >= 0) {
//            if (list[i] is IExpandable<*>) {
//                val item = list[i] as IExpandable<*>
//                if (item.isExpanded && hasSubItems(item)) {
//                    val subList = item.subItems
//                    mData!!.addAll(pos + 1, subList)
//                    val subItemCount = recursiveExpand(pos + 1, subList)
//                    count += subItemCount
//                }
//            }
//            i--
//            pos--
//        }
//        return count
//
//    }
//
//    /**
//     * Expand an expandable item
//
//     * @param position     position of the item
//     * *
//     * @param animate      expand items with animation
//     * *
//     * @param shouldNotify notify the RecyclerView to rebind items, **false** if you want to do it
//     * *                     yourself.
//     * *
//     * @return the number of items that have been added.
//     */
//    @JvmOverloads fun expand(@IntRange(from = 0) position: Int, animate: Boolean = true, shouldNotify: Boolean = true): Int {
//        var position = position
//        position -= headerLayoutCount
//
//        val expandable = getExpandableItem(position) ?: return 0
//        if (!hasSubItems(expandable)) {
//            expandable.isExpanded = false
//            return 0
//        }
//        var subItemCount = 0
//        if (!expandable.isExpanded) {
//            val list = expandable.subItems
//            mData!!.addAll(position + 1, list)
//            subItemCount += recursiveExpand(position + 1, list)
//
//            expandable.isExpanded = true
//            subItemCount += list.size
//        }
//        val parentPos = position + headerLayoutCount
//        if (shouldNotify) {
//            if (animate) {
//                notifyItemChanged(parentPos)
//                notifyItemRangeInserted(parentPos + 1, subItemCount)
//            } else {
//                notifyDataSetChanged()
//            }
//        }
//        return subItemCount
//    }
//
//    fun expandAll(position: Int, animate: Boolean, notify: Boolean): Int {
//        var position = position
//        position -= headerLayoutCount
//
//        var endItem: T? = null
//        if (position + 1 < this.mData!!.size) {
//            endItem = getItem(position + 1)
//        }
//
//        val expandable = getExpandableItem(position)
//        if (!hasSubItems(expandable)) {
//            return 0
//        }
//
//        var count = expand(position + headerLayoutCount, false, false)
//        for (i in position + 1..this.mData!!.size - 1) {
//            val item = getItem(i)
//
//            if (item === endItem) {
//                break
//            }
//            if (isExpandable(item)) {
//                count += expand(i + headerLayoutCount, false, false)
//            }
//        }
//
//        if (notify) {
//            if (animate) {
//                notifyItemRangeInserted(position + headerLayoutCount + 1, count)
//            } else {
//                notifyDataSetChanged()
//            }
//        }
//        return count
//    }
//
//    /**
//     * expand the item and all its subItems
//
//     * @param position position of the item, which includes the header layout count.
//     * *
//     * @param init     whether you are initializing the recyclerView or not.
//     * *                 if **true**, it won't notify recyclerView to redraw UI.
//     * *
//     * @return the number of items that have been added to the adapter.
//     */
//    fun expandAll(position: Int, init: Boolean): Int {
//        return expandAll(position, true, !init)
//    }
//
//    private fun recursiveCollapse(@IntRange(from = 0) position: Int): Int {
//        val item = getItem(position)
//        if (!isExpandable(item)) {
//            return 0
//        }
//        val expandable = item as IExpandable<*>?
//        var subItemCount = 0
//        if (expandable!!.isExpanded) {
//            val subItems = expandable.subItems
//            for (i in subItems.indices.reversed()) {
//                val subItem = subItems[i]
//                val pos = getItemPosition(subItem)
//                if (pos < 0) {
//                    continue
//                }
//                if (subItem is IExpandable<*>) {
//                    subItemCount += recursiveCollapse(pos)
//                }
//                mData!!.removeAt(pos)
//                subItemCount++
//            }
//        }
//        return subItemCount
//    }
//
//    /**
//     * Collapse an expandable item that has been expanded..
//
//     * @param position the position of the item, which includes the header layout count.
//     * *
//     * @param animate  collapse with animation or not.
//     * *
//     * @param notify   notify the recyclerView refresh UI or not.
//     * *
//     * @return the number of subItems collapsed.
//     */
//    @JvmOverloads fun collapse(@IntRange(from = 0) position: Int, animate: Boolean = true, notify: Boolean = true): Int {
//        var position = position
//        position -= headerLayoutCount
//
//        val expandable = getExpandableItem(position) ?: return 0
//        val subItemCount = recursiveCollapse(position)
//        expandable.isExpanded = false
//        val parentPos = position + headerLayoutCount
//        if (notify) {
//            if (animate) {
//                notifyItemChanged(parentPos)
//                notifyItemRangeRemoved(parentPos + 1, subItemCount)
//            } else {
//                notifyDataSetChanged()
//            }
//        }
//        return subItemCount
//    }
//
//    private fun getItemPosition(item: T?): Int {
//        return if (item != null && mData != null && !mData!!.isEmpty()) mData!!.indexOf(item) else -1
//    }
//
//    private fun hasSubItems(item: IExpandable<*>): Boolean {
//        val list = item.subItems
//        return list != null && list.size > 0
//    }
//
//    fun isExpandable(item: T?): Boolean {
//        return item != null && item is IExpandable<*>
//    }
//
//    private fun getExpandableItem(position: Int): IExpandable<*>? {
//        val item = getItem(position)
//        if (isExpandable(item)) {
//            return item as IExpandable<*>?
//        } else {
//            return null
//        }
//    }
//
//    /**
//     * Get the parent item position of the IExpandable item
//
//     * @return return the closest parent item position of the IExpandable.
//     * * if the IExpandable item's level is 0, return itself position.
//     * * if the item's level is negative which mean do not implement this, return a negative
//     * * if the item is not exist in the data list, return a negative.
//     */
//    fun getParentPosition(item: T): Int {
//        val position = getItemPosition(item)
//        if (position == -1) {
//            return -1
//        }
//
//        // if the item is IExpandable, return a closest IExpandable item position whose level smaller than this.
//        // if it is not, return the closest IExpandable item position whose level is not negative
//        val level: Int
//        if (item is IExpandable<*>) {
//            level = item.level
//        } else {
//            level = Integer.MAX_VALUE
//        }
//        if (level == 0) {
//            return position
//        } else if (level == -1) {
//            return -1
//        }
//
//        for (i in position downTo 0) {
//            val temp = mData!![i]
//            if (temp is IExpandable<*>) {
//                val expandable = temp
//                if (expandable.level >= 0 && expandable.level < level) {
//                    return i
//                }
//            }
//        }
//        return -1
//    }
//
//    companion object {
//
//        //Animation
//        /**
//         * Use with [.openLoadAnimation]
//         */
//        val ALPHAIN = 0x00000001
//        /**
//         * Use with [.openLoadAnimation]
//         */
//        val SCALEIN = 0x00000002
//        /**
//         * Use with [.openLoadAnimation]
//         */
//        val SLIDEIN_BOTTOM = 0x00000003
//        /**
//         * Use with [.openLoadAnimation]
//         */
//        val SLIDEIN_LEFT = 0x00000004
//        /**
//         * Use with [.openLoadAnimation]
//         */
//        val SLIDEIN_RIGHT = 0x00000005
//
//        protected val TAG = BaseQuickAdapter<*, *>::class.java.simpleName
//        val HEADER_VIEW = 0x00000111
//        val LOADING_VIEW = 0x00000222
//        val FOOTER_VIEW = 0x00000333
//        val EMPTY_VIEW = 0x00000555
//    }
//}
///**
// * Refresh end, no more data
// */
///**
// * Append header to the rear of the mHeaderLayout.
//
// * @param header
// */
///**
// * Add header view to mHeaderLayout and set header view position in mHeaderLayout.
// * When index = -1 or index >= child count in mHeaderLayout,
// * the effect of this method is the same as that of [.addHeaderView].
//
// * @param header
// * *
// * @param index  the position in mHeaderLayout of this header.
// * *               When index = -1 or index >= child count in mHeaderLayout,
// * *               the effect of this method is the same as that of [.addHeaderView].
// */
///**
// * Append footer to the rear of the mFooterLayout.
//
// * @param footer
// */
///**
// * Expand an expandable item
//
// * @param position position of the item, which includes the header layout count.
// * *
// * @param animate  expand items with animation
// * *
// * @return the number of items that have been added.
// */
///**
// * Expand an expandable item with animation.
//
// * @param position position of the item, which includes the header layout count.
// * *
// * @return the number of items that have been added.
// */
///**
// * Collapse an expandable item that has been expanded..
//
// * @param position the position of the item, which includes the header layout count.
// * *
// * @return the number of subItems collapsed.
// */
///**
// * Collapse an expandable item that has been expanded..
//
// * @param position the position of the item, which includes the header layout count.
// * *
// * @return the number of subItems collapsed.
// */
