package com.stoyanov.developer.goevent.utill.transition;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class DetailTransition extends TransitionSet {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DetailTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform());
    }
}
