package edu.uw.multitouchlab;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AnimationSet;

import static android.R.attr.radius;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private DrawingSurfaceView view;
    private AnimatorSet radiusAnimator;
    private GestureDetectorCompat mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (DrawingSurfaceView)findViewById(R.id.drawingView);
        radiusAnimator = (AnimatorSet)AnimatorInflater.loadAnimator(this, R.animator.animations);
        mGestureDetector = new GestureDetectorCompat(this, new MyGestureListener());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY() - getSupportActionBar().getHeight(); //closer to center...

        int action = event.getActionMasked();
        mGestureDetector.onTouchEvent(event);
        int pointerIndex = MotionEventCompat.getActionIndex(event);
        int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
        int count = event.getPointerCount();
        switch(action) {
            case (MotionEvent.ACTION_DOWN) : //put finger down
                //Log.v(TAG, "finger down");
//                view.ball.cx = x;
//                view.ball.cy = y;
                ObjectAnimator animX = ObjectAnimator.ofFloat(view.ball, "x",x);
                ObjectAnimator animY = ObjectAnimator.ofFloat(view.ball, "y", y);
                animX.setDuration(1000);
                animY.setDuration(1500);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animX, animY);
                animatorSet.start();
//                view.addTouch(pointerId, event.getX(pointerIndex), event.getY(pointerIndex) - getSupportActionBar().getHeight());

                return true;

            case (MotionEvent.ACTION_POINTER_DOWN):
                Log.v(TAG, "Pointer down");
                view.addTouch(pointerId,event.getX(pointerIndex), event.getY(pointerIndex)-getSupportActionBar().getHeight());

                return true;

            case (MotionEvent.ACTION_POINTER_UP):
                Log.v(TAG, "Pointer up");
                view.removeTouch(pointerId);
                return true;

            case (MotionEvent.ACTION_MOVE) : //move finger
                //Log.v(TAG, "finger move");
//                view.ball.cx = x;
//                view.ball.cy = y;
                for (int i = 0; i<count; i++){
                    int id = event.getPointerId(i);
                    float moveX = event.getX(i);
                    float moveY = event.getY(i)-getSupportActionBar().getHeight();
                    view.moveTouch(id,moveX,moveY);
                }
                return true;
            case (MotionEvent.ACTION_UP) : //lift finger up
                view.removeTouch(pointerId);
            case (MotionEvent.ACTION_CANCEL) : //aborted gesture
            case (MotionEvent.ACTION_OUTSIDE) : //outside bounds
            default :
                return super.onTouchEvent(event);
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true; //recommended practice
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float scaleFactor = .03f;

            //fling!
            Log.v(TAG, "Fling! "+ velocityX + ", " + velocityY);
            view.ball.dx = -1*velocityX*scaleFactor;
            view.ball.dy = -1*velocityY*scaleFactor;

            return true; //we got this
        }
    }
}