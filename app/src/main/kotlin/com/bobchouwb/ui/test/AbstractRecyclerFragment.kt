//package com.bobchouwb.ui.test
//
//import android.graphics.Bitmap
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import android.os.Handler
//import android.support.v7.widget.DividerItemDecoration
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//
//import com.bz.netdiary.R
//import com.bz.netdiary.base.ClientApp
//import com.bz.netdiary.ui.weight.MallLoadMoreView
//import com.bz.netdiary.ui.weight.SpaceItemDecoration
//import com.chad.library.adapter.base.BaseQuickAdapter
//import com.chad.library.adapter.base.BaseViewHolder
//import com.mall.android.common.utils.Logger
//import com.mall.android.common.utils.MemoryUtil
//import com.mall.android.common.utils.imageloader.PicassoHelper
//import com.mall.android.ui.fragment.BaseFragment
//
//import java.lang.ref.WeakReference
//import java.util.HashMap
//
//import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
//
///**
// * Created by zhoubo on 2017/3/2.
// */
//
//abstract class AbstractRecyclerFragment : BaseFragment() {
//
//    var recyclerView: RecyclerView? = null
//        private set
//    var recyclerAdapter: BaseQuickAdapter<*, *>? = null
//        private set
//
//    private var notDataView: View? = null
//    private var errorView: View? = null
//
//    protected fun inflateContentView(): Int {
//        return R.layout.comm_ui_recyclerview
//    }
//
//    private val viewCache = HashMap<String, WeakReference<View>>()
//
//    fun onViewCreated(view: View, savedInstanceState: Bundle) {
//        super.onViewCreated(view, savedInstanceState)
//        recyclerView = view.findViewById(R.id.recyclerview) as RecyclerView
//        recyclerView!!.setHasFixedSize(true)
//        recyclerAdapter = configRecyclerViewAdapter()
//        recyclerView!!.layoutManager = configRecyclerViewLayoutManager() // 配置适配器
//
//        /************************分割线 */
//        var itemDecoration = configDividerItemDecoration()
//        if (itemDecoration == null) {
//            itemDecoration = DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL)
//        }
//        recyclerView!!.addItemDecoration(itemDecoration) // 分割线
//
//        //        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin_small); // 分割间距
//        //        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
//        /** */
//
//        //        mRecyclerView.setItemViewCacheSize(5);
//        recyclerAdapter!!.setLoadMoreView(MallLoadMoreView())
//        recyclerView!!.adapter = recyclerAdapter
//        recyclerView!!.addOnScrollListener(RecyclerScollListener())
//        recyclerView!!.setRecyclerListener { holder ->
//            //                Logger.d(TAG, holder.toString());
//            if (holder is BaseViewHolder) {
//                if (!viewCache.containsKey(holder.itemView.toString())) {
//                    //                        Log.d(TAG, holder.itemView.toString() + "保存一个View到Cache");
//                    viewCache.put(holder.itemView.toString(), WeakReference(holder.itemView))
//                }
//            }
//        }
//
//        notDataView = getLayoutInflater().inflate(R.layout.empty_view, recyclerView!!.parent as ViewGroup, false)
//        errorView = getLayoutInflater().inflate(R.layout.error_view, recyclerView!!.parent as ViewGroup, false)
//    }
//
//    fun setRecycledViewPool(pool: RecyclerView.RecycledViewPool?) {
//        if (pool != null) {
//            recyclerView!!.recycledViewPool = pool
//        }
//    }
//
//    /**
//     * RecyclerView 滑动监听
//     */
//    internal inner class RecyclerScollListener : RecyclerView.OnScrollListener() {
//
//        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            Logger.d(TAG, "onScrollStateChanged newState: " + newState)
//            when (newState) {
//                SCROLL_STATE_IDLE // The RecyclerView is not currently scrolling.
//                ->
//                    //                    Logger.d("RecyclerScollListener", "not scrolling");
//                    PicassoHelper.getSingleton().getPicasso().resumeTag(ClientApp.getInstance())
//            //                case SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
//            //                    ClientApp.getInstance().getPicassoHelper().getPicasso().pauseTag(ClientApp.getInstance());
//            //                    Logger.d("RecyclerScollListener", "scrolling...");
//            //                    break;
//            //                case SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under
//            //                    ClientApp.getInstance().getPicassoHelper().getPicasso().pauseTag(ClientApp.getInstance());
//            //                    Logger.d("RecyclerScollListener", "scrolling...");
//            //                    break;
//                else -> {
//                    //                    Logger.d(TAG, "memory: " + memoryUtil.getCurrentMemorySize());
//                    //                    Logger.d(TAG, "PicassoHelper : " + memoryUtil.getCurrentMemorySize());
//                    Logger.d(TAG, "calculateMemoryCacheSize : " + PicassoHelper.getSingleton().calculateMemoryCacheSize() / (1024 * 1024))
//                    PicassoHelper.getSingleton().getPicasso().pauseTag(ClientApp.getInstance())
//                }
//            }
//        }
//    }
//
//    internal var memoryUtil = MemoryUtil()
//
//    fun requestData() {
//        super.requestData()
//    }
//
//    abstract fun configRecyclerViewLayoutManager(): LinearLayoutManager
//    abstract fun configRecyclerViewAdapter(): BaseQuickAdapter<*, *>
//
//    fun onDetach() {
//        super.onDetach()
//        Log.d(TAG, "onDetach()")
//        //        PicassoHelper.getSingleton().clearCache(); // 清理所有图片缓存
//        recyclerView!!.recycledViewPool.clear()
//    }
//
//
//    /****************************释放ImageView */
//    internal var mHandler = Handler()
//    internal var releaseRunnable: Runnable = Runnable { releaseImageViewByIds() }
//
//    private fun releaseImageViewByIds() {
//        if (recyclerView != null) {
//            val childSize = recyclerView!!.childCount
//            for (i in 0..childSize - 1) {
//                val view = recyclerView!!.getChildAt(i)
//                releaseImageView(view)
//                if (viewCache.containsKey(view.toString())) {
//                    //                    Logger.d(TAG, "已经释放了，从Cache中移除");
//                    viewCache.remove(view.toString())
//                }
//            }
//            if (viewCache.size > 0) {
//                val keySet = viewCache.keys
//                for (key in keySet) {
//                    val view = viewCache[key].get()
//                    if (view != null) {
//                        //                        Logger.d(TAG, "从Cache中释放一个View");
//                        releaseImageView(view)
//                    }
//                }
//                viewCache.clear()
//            }
//        }
//    }
//
//    private fun releaseImageView(parentView: View) {
//        if (configCanReleaseIds() != null) {
//            for (imgId in configCanReleaseIds()!!) {
//                val imgView = parentView.findViewById(imgId) as ImageView
//                imgView?.setImageDrawable(null)
//            }
//        }
//    }
//
//    fun releaseImageViewResouce(imageView: ImageView?) {
//        if (imageView == null) return
//        val drawable = imageView.drawable
//        if (drawable != null && drawable is BitmapDrawable) {
//            val bitmap = drawable.bitmap
//            if (bitmap != null && !bitmap.isRecycled) {
//                bitmap.recycle()
//            }
//        }
//    }
//
//    fun onPause() {
//        Logger.d(TAG, "onPause()")
//        super.onPause()
//        mHandler.postDelayed(releaseRunnable, (1 * 1000).toLong())
//    }
//
//    fun onResume() {
//        Logger.d(TAG, "onResume()")
//        super.onResume()
//        mHandler.removeCallbacks(releaseRunnable)
//        recyclerAdapter.notifyDataSetChanged()
//    }
//
//    //    product_imageView
//    protected fun configCanReleaseIds(): IntArray? {
//        return null
//    }
//
//    fun configDividerItemDecoration(): RecyclerView.ItemDecoration? {
//        return null
//    }
//
//    fun onLowMemory() {
//        super.onLowMemory()
//    }
//
//    companion object {
//
//        private val TAG = AbstractRecyclerFragment::class.java.simpleName
//    }
//}
