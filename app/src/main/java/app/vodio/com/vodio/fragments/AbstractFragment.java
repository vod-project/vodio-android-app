package app.vodio.com.vodio.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import app.vodio.com.vodio.fragments.utils.FragmentCallBack;

public abstract class AbstractFragment extends Fragment implements View.OnClickListener {
    protected FragmentCallBack parent;

    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);
        parent = (FragmentCallBack) act;
    }

}
