package com.arcltd.staff.utility;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.appcompat.widget.AppCompatImageView;

public class ZoomImageView extends AppCompatImageView {

    private Matrix matrix = new Matrix();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    private PointF lastPoint = new PointF();
    private PointF startPoint = new PointF();
    private float minScale = 1f;
    private float maxScale = 5f;
    private float[] mValues;

    private int viewWidth, viewHeight;
    private static final int CLICK = 3;
    private float saveScale = 1f;
    private float origWidth, origHeight;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;

    public ZoomImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        post(this::resetMatrix);
    }

    public void resetMatrix() {
        if (getDrawable() == null) return;

        viewWidth = getWidth();
        viewHeight = getHeight();

        if (viewWidth <= 0 || viewHeight <= 0) return;

        int bmWidth = getDrawable().getIntrinsicWidth();
        int bmHeight = getDrawable().getIntrinsicHeight();

        if (bmWidth > 0 && bmHeight > 0) {
            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            float scale = Math.min(scaleX, scaleY);

            matrix.reset();
            matrix.setScale(scale, scale);

            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= 2f;
            redundantXSpace /= 2f;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            saveScale = 1f;
            setImageMatrix(matrix);
            invalidate();
        }
    }

    private void init(Context context) {
        super.setClickable(true);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float targetScale = (saveScale == 1f) ? 2.5f : 1f;
                float factor = targetScale / saveScale;
                matrix.postScale(factor, factor, viewWidth / 2f, viewHeight / 2f);
                saveScale = targetScale;
                fixTranslation();
                setImageMatrix(matrix);
                return true;
            }
        });

        mValues = new float[9];
        setScaleType(ScaleType.MATRIX);
        setImageMatrix(matrix);

        setOnTouchListener((v, event) -> {
            mScaleDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);

            PointF currentPoint = new PointF(event.getX(), event.getY());

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    lastPoint.set(currentPoint);
                    startPoint.set(lastPoint);
                    mode = DRAG;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        float deltaX = currentPoint.x - lastPoint.x;
                        float deltaY = currentPoint.y - lastPoint.y;
                        float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                        float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
                        matrix.postTranslate(fixTransX, fixTransY);
                        fixTranslation();
                        lastPoint.set(currentPoint.x, currentPoint.y);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    mode = NONE;
                    int xDiff = (int) Math.abs(currentPoint.x - startPoint.x);
                    int yDiff = (int) Math.abs(currentPoint.y - startPoint.y);
                    if (xDiff < CLICK && yDiff < CLICK) performClick();
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
            }

            setImageMatrix(matrix);
            invalidate();
            return true;
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) {
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2f, viewHeight / 2f);
            } else {
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
            }

            fixTranslation();
            return true;
        }
    }

    private void fixTranslation() {
        matrix.getValues(mValues);
        float transX = mValues[Matrix.MTRANS_X];
        float transY = mValues[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);

        if (fixTransX != 0 || fixTransY != 0) {
            matrix.postTranslate(fixTransX, fixTransY);
        }
    }

    private float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans) return -trans + minTrans;
        if (trans > maxTrans) return -trans + maxTrans;
        return 0;
    }

    private float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            resetMatrix();
        }
    }
}