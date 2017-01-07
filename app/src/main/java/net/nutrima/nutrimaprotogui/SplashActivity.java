package net.nutrima.nutrimaprotogui;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.vision.Frame;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 4500;
    private final int MY_PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        animations();

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST);
            return;
        }
        else {
            handleSignUpButton();

            handleEmailSubmitButton();

            handleSubmitButton();

            final CardView card = (CardView) findViewById(R.id.referralId_cardView);
            final Animation enterCardFromRight = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
            enterCardFromRight.setDuration(1000);
            enterCardFromRight.setFillAfter(true);

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    card.setVisibility(View.VISIBLE);
                    card.startAnimation(enterCardFromRight);
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    handleSignUpButton();

                    handleEmailSubmitButton();

                    handleSubmitButton();

                    final CardView card = (CardView) findViewById(R.id.referralId_cardView);
                    final Animation enterCardFromRight = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
                    enterCardFromRight.setDuration(1000);
                    enterCardFromRight.setFillAfter(true);

                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            card.setVisibility(View.VISIBLE);
                            card.startAnimation(enterCardFromRight);
                        }
                    }, SPLASH_DISPLAY_LENGTH);


                } else {
                    // TODO: Show an apology message
                    // Exit app.
                }
                return;
            }
        }
    }

    public void animations(){
        fadeNameIn();
        //moveLogoToScreenCenterThenTopLeft();
        moveLogoToScreenCenterThenUpAgain();
    }

    private void handleSubmitButton() {
        Button clickButton = (Button) findViewById(R.id.submit_button);
        final Intent activityChangeIntent = new Intent(this,
                LoginActivity.class);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add checkers
                startActivity(activityChangeIntent);
            }
        });
    }

    private void handleEmailSubmitButton() {
        Button clickButton = (Button) findViewById(R.id.email_submit_button);
        // Old Card //////
        final CardView oldCard = (CardView) findViewById(R.id.enter_email_cardView);
        final Animation fadeCardOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeCardOut.setDuration(1000);
        fadeCardOut.setFillAfter(true);
        //////////////////

        // New Card //////
        final CardView newCard = (CardView) findViewById(R.id.thankyou_cardView);
        final Animation enterCardFromRight = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
        enterCardFromRight.setDuration(1000);
        enterCardFromRight.setFillAfter(true);
        //////////////////

        final Animation tickRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        tickRotate.reset();

        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldCard.startAnimation(fadeCardOut);
                newCard.setVisibility(View.VISIBLE);
                newCard.startAnimation(enterCardFromRight);
                findViewById(R.id.thankyou_imageView).startAnimation(tickRotate);
            }
        });
    }

    private void handleSignUpButton() {
        Button clickButton = (Button) findViewById(R.id.signup_button);
        // Old Card //////
        final CardView oldCard = (CardView) findViewById(R.id.referralId_cardView);
        final Animation fadeCardOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeCardOut.setDuration(1000);
        fadeCardOut.setFillAfter(true);
        //////////////////

        // New Card //////
        final CardView newCard = (CardView) findViewById(R.id.enter_email_cardView);
        final Animation enterCardFromRight = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
        enterCardFromRight.setDuration(1000);
        enterCardFromRight.setFillAfter(true);
        //////////////////

        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldCard.startAnimation(fadeCardOut);
                newCard.setVisibility(View.VISIBLE);
                newCard.startAnimation(enterCardFromRight);
            }
        });
    }

    private void fadeNameIn() {
        ImageView nameImageView = (ImageView)findViewById(R.id.splash_name_imageView);

        Animation nameAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        nameAnimation.setDuration(2000);

        nameImageView.startAnimation(nameAnimation);
    }

    private void moveLogoToScreenCenterThenUpAgain() {
        final ImageView logoImageView = (ImageView)findViewById(R.id.splash_logo_imageView);
        final ImageView nameImageView = (ImageView)findViewById(R.id.splash_name_imageView);

        final DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics( dm );

        int originalPos[] = new int[2];
        logoImageView.getLocationOnScreen( originalPos );

        Animation logoFadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        logoFadeIn.setDuration(2000);
        logoFadeIn.setFillAfter(true);

        TranslateAnimation slideDownAnimation = new TranslateAnimation( 0, 0, 0, 600);
        slideDownAnimation.setDuration(2000);
        slideDownAnimation.setFillAfter(true);

        TranslateAnimation slideUpAnimation = new TranslateAnimation( 0, 0, 0, -600);
        slideUpAnimation.setDuration(1000);
        slideUpAnimation.setFillAfter(true);
        slideUpAnimation.setStartOffset(4000);

        AnimationSet s = new AnimationSet(false);
        s.addAnimation(logoFadeIn);
        s.addAnimation(slideDownAnimation);
        s.addAnimation(slideUpAnimation);

        logoImageView.startAnimation(s);

        TranslateAnimation slideNameUp = new TranslateAnimation(0, 0, 0, -600);
        slideNameUp.setDuration(1000);
        slideNameUp.setFillAfter(true);
        slideNameUp.setStartOffset(4000);
        nameImageView.startAnimation(slideNameUp);
    }

    private void moveLogoToScreenCenterThenTopLeft()
    {
        final ImageView logoImageView = (ImageView)findViewById(R.id.splash_logo_imageView);
        final ImageView nameImageView = (ImageView)findViewById(R.id.splash_name_imageView);

        final DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics( dm );

        int originalPos[] = new int[2];
        logoImageView.getLocationOnScreen( originalPos );

        final int xDest = -1* dm.widthPixels/2 - ((int)getResources().getDimension(R.dimen.splash_logo_width)/4);
        final int yDest = dm.heightPixels/2 - ((int)getResources().getDimension(R.dimen.splash_logo_height));

        TranslateAnimation slideDownAnimation = new TranslateAnimation( 0, 0, 0, yDest );
        slideDownAnimation.setDuration(2000);
        slideDownAnimation.setFillAfter(true);

        TranslateAnimation slideDiagonalAnimation = new TranslateAnimation( 0, xDest, 0, -1*yDest );
        slideDiagonalAnimation.setDuration(1000);
        slideDiagonalAnimation.setFillAfter(true);
        slideDiagonalAnimation.setStartOffset(4000);

        ScaleAnimation shrinkAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f);
        shrinkAnimation.setDuration(1000);
        shrinkAnimation.setFillAfter(true);
        shrinkAnimation.setStartOffset(4000);

        AnimationSet s = new AnimationSet(false);
        s.addAnimation(slideDownAnimation);
        s.addAnimation(slideDiagonalAnimation);
        s.addAnimation(shrinkAnimation);

        logoImageView.startAnimation(s);

        TranslateAnimation slideNameUp = new TranslateAnimation(0, 0, 0,
                -1*dm.heightPixels/2 + ((int)getResources().getDimension(R.dimen.splash_name_height)));
        slideNameUp.setDuration(1000);
        slideNameUp.setFillAfter(true);
        slideNameUp.setStartOffset(4000);
        nameImageView.startAnimation(slideNameUp);

        s.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FrameLayout.LayoutParams layoutParams1 =
                        (FrameLayout.LayoutParams) logoImageView.getLayoutParams();
                layoutParams1.gravity = 0;
                layoutParams1.leftMargin = 0;
                layoutParams1.topMargin = 0;
                layoutParams1.height *= 0.5;
                layoutParams1.width *= 0.5;
                logoImageView.setLayoutParams(layoutParams1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
