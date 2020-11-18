package com.huashengfu.StemCellsManager.event.activity;

import com.huashengfu.StemCellsManager.entity.activity.Activity;

public class UpdateActivityEvent {

    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public UpdateActivityEvent setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }
}
