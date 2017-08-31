package net.mavenmobile.bakingtime.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by bembengcs on 8/30/2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new ListRemoteViewFactory(this.getApplicationContext()));
    }
}
