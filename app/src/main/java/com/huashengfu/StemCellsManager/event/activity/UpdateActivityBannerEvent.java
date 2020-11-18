package com.huashengfu.StemCellsManager.event.activity;

import com.huashengfu.StemCellsManager.entity.activity.Activity;

public class UpdateActivityBannerEvent {

    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public UpdateActivityBannerEvent setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }
}
