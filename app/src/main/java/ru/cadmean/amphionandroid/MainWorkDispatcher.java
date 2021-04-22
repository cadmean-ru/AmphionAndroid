package ru.cadmean.amphionandroid;

import android.os.Handler;
import ru.cadmean.amphion.android.dispatch.WorkDispatcher;
import ru.cadmean.amphion.android.dispatch.WorkItem;

public class MainWorkDispatcher implements WorkDispatcher {

    private final Handler mainHandler;

    public MainWorkDispatcher(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    @Override
    public void execute(WorkItem workItem) {
        mainHandler.post(workItem::execute);
    }
}
