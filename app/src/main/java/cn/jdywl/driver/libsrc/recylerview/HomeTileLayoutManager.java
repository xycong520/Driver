package cn.jdywl.driver.libsrc.recylerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wuwantao on 15/10/6.
 */
public class HomeTileLayoutManager extends GridLayoutManager {

    public HomeTileLayoutManager(Context context, int spanCount) {
        super(context,spanCount);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec,
                          int heightSpec) {
        View view = recycler.getViewForPosition(0);
        if(view != null){
            measureChild(view, widthSpec, heightSpec);
            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
            int measuredHeight = view.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }
}
