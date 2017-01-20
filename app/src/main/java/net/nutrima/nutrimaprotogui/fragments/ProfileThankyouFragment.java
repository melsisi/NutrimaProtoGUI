package net.nutrima.nutrimaprotogui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import net.nutrima.nutrimaprotogui.R;


public class ProfileThankyouFragment extends Fragment {

    public ProfileThankyouFragment() { }

    public static ProfileThankyouFragment newInstance() {
        ProfileThankyouFragment fragment = new ProfileThankyouFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_thankyou, container, false);
        runAnimation(rootView);
        mCallback.onNextClicked(5);
        return rootView;
    }

    OnNextClickedListener mCallback;

    // Container Activity must implement this interface
    public interface OnNextClickedListener {
        public void onNextClicked(int myOrder);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnNextClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextClickedListener");
        }
    }

    private void runAnimation(View rootView)
    {
        Animation textEnter = AnimationUtils.loadAnimation(getActivity(), R.anim.enter_from_left);
        textEnter.reset();
        Animation tickRotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        tickRotate.reset();
        Animation tickExit = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        tickExit.reset();
        tickExit.setStartOffset(tickRotate.getDuration()*4);
        tickExit.setDuration(1000);

        AnimationSet s = new AnimationSet(false);
        s.addAnimation(tickRotate);
        s.addAnimation(tickExit);
        s.setFillAfter(true);

        TextView thankYouTextView = (TextView) rootView.findViewById(R.id.thankyou_textView);
        thankYouTextView.clearAnimation();

        TextView thankYouStatementTextView = (TextView) rootView.findViewById(R.id.thankyou_statement_textView);
        thankYouStatementTextView.clearAnimation();

        ImageView tickImageView = (ImageView) rootView.findViewById(R.id.thankyou_imageView);
        tickImageView.clearAnimation();

        thankYouStatementTextView.startAnimation(textEnter);
        thankYouTextView.startAnimation(textEnter);
        tickImageView.startAnimation(s);
    }

}
