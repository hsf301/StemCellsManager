package com.huashengfu.StemCellsManager.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageViewAdapter extends PagerAdapter {

    private List<ImageView> imageViews = new ArrayList<>();

    public ImageViewAdapter(List<ImageView> imageViews){
        this.imageViews = imageViews;
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if(view == object){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position));
        return imageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }
}
