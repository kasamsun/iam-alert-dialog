package com.kswafx.iamalertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

public class IamAlertDialog extends Dialog implements View.OnClickListener {
    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mSuccessBowAnim;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private FrameLayout mCustomViewContainer;
    private View mCustomView;
    private String mTitleText;
    private String mContentText;
    private boolean mShowCancel;
    private boolean mShowContent;
    private String mCancelText;
    private String mConfirmText;
    private String mNeutralText;
    private int mAlertType;
    private FrameLayout mErrorFrame;
    private FrameLayout mSuccessFrame;
    private FrameLayout mProgressFrame;
    private SuccessTickView mSuccessTick;
    private ImageView mErrorX;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private Drawable mCustomImgDrawable;
    private ImageView mCustomImage;
    private LinearLayout mButtonsContainer;
    private Button mConfirmButton;
    private boolean mHideConfirmButton = false;
    private Button mCancelButton;
    private Button mNeutralButton;
    private Integer mConfirmButtonBackgroundColor;
    private Integer mConfirmButtonTextColor;
    private Integer mNeutralButtonBackgroundColor;
    private Integer mNeutralButtonTextColor;
    private Integer mCancelButtonBackgroundColor;
    private Integer mCancelButtonTextColor;
    private ProgressHelper mProgressHelper;
    private FrameLayout mWarningFrame;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private OnSweetClickListener mNeutralClickListener;
    private boolean mCloseFromCancel;
    private boolean mHideKeyBoardOnDismiss = true;
    private int contentTextSize = 0;
    private int mButtonOrientation = LinearLayout.HORIZONTAL;
    private ArrayList<DataItem> mListItems = new ArrayList<>();
    private ListView mListView;
    private FrameLayout mListViewContainer;
    private static ArrayAdapter listAdapter;
    private OnSingleSelectionClickListener mSingleSelectionClickListener;
    private boolean mShowListSelection = false;
    private int mSelectedItem;
    private FrameLayout mInputContainer;
    private TextInputEditText mEditText;
    private TextInputLayout mInputLayout;
    private OnSweetTextInputListener mTextInputListener;
    private String mHintText;
    private String mDefaultText;

    private int iconSize = 32;
    private boolean tintFlag = true;
    private Integer mConfirmButtonDrawableLeft = 0;
    private Integer mCancelButtonDrawableLeft = 0;
    private Integer mNeutralButtonDrawableLeft = 0;

    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;
    public static final int TEXT_SELECTION_TYPE = 6;
    public static final int BUTTON_SELECTION_TYPE = 7;
    public static final int TEXT_INPUT_TYPE = 8;
    public static final int DATE_PICKER_TYPE = 9;

    public static boolean DARK_STYLE = false;

    //aliases
    public final static int BUTTON_CONFIRM = DialogInterface.BUTTON_POSITIVE;
    public final static int BUTTON_CANCEL = DialogInterface.BUTTON_NEGATIVE;

    private final float defStrokeWidth;
    private float strokeWidth = 0;

    public IamAlertDialog hideConfirmButton() {
        this.mHideConfirmButton = true;
        return this;
    }

    public interface OnSweetClickListener {
        void onClick(IamAlertDialog sweetAlertDialog);
    }

    public interface OnSingleSelectionClickListener {
        void onClick(IamAlertDialog sweetAlertDialog, int position, DataItem data);
    }

    public interface OnSweetTextInputListener {
        void onTextInput(IamAlertDialog sweetAlertDialog, String value);
    }

    public IamAlertDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    public IamAlertDialog(Context context, int alertType) {
        super(context, DARK_STYLE ? R.style.alert_dialog_dark : R.style.alert_dialog_light);
        setCancelable(true);
        setCanceledOnTouchOutside(true); //TODO was false

        defStrokeWidth = getContext().getResources().getDimension(R.dimen.buttons_stroke_width);
        strokeWidth = defStrokeWidth;

        mProgressHelper = new ProgressHelper(context);
        mAlertType = alertType;
        mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);

        mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mAlertType==TEXT_INPUT_TYPE) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mEditText, 0);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                if (mHideKeyBoardOnDismiss) {
                    hideSoftKeyboard();
                }
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            IamAlertDialog.super.cancel();
                        } else {
                            IamAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);

        //

        // Default text
        switch(mAlertType) {
            case NORMAL_TYPE:
            case ERROR_TYPE:
            case SUCCESS_TYPE:
                mConfirmText = getContext().getString(R.string.action_confirm);
                break;
            case WARNING_TYPE:
            case TEXT_INPUT_TYPE:
                mConfirmText = getContext().getString(R.string.action_confirm);
                mCancelText = getContext().getString(R.string.action_cancel);
                break;
            case PROGRESS_TYPE:
            case TEXT_SELECTION_TYPE:
            case BUTTON_SELECTION_TYPE:
                break;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mysweetalert);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTextView = findViewById(R.id.title_text);
        mContentTextView = findViewById(R.id.content_text);
        mCustomViewContainer = findViewById(R.id.custom_view_container);
        mErrorFrame = findViewById(R.id.error_frame);
        mErrorX = mErrorFrame.findViewById(R.id.error_x);
        mSuccessFrame = findViewById(R.id.success_frame);
        mProgressFrame = findViewById(R.id.progress_dialog);
        mSuccessTick = mSuccessFrame.findViewById(R.id.success_tick);
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
        mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        mCustomImage = findViewById(R.id.custom_image);
        mWarningFrame = findViewById(R.id.warning_frame);
        mButtonsContainer = findViewById(R.id.buttons_container);
        mButtonsContainer.setOrientation(mButtonOrientation);
        mConfirmButton = findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(this);
        mConfirmButton.setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
        mCancelButton = findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(this);
        mCancelButton.setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
        mNeutralButton = findViewById(R.id.neutral_button);
        mNeutralButton.setOnClickListener(this);
        mNeutralButton.setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
        mProgressHelper.setProgressWheel((ProgressWheel) findViewById(R.id.progressWheel));

        if (mConfirmButtonDrawableLeft>0) {
            mConfirmButton.setCompoundDrawablesWithIntrinsicBounds(mConfirmButtonDrawableLeft, 0, 0, 0);
        }
        if (mCancelButtonDrawableLeft>0) {
            mCancelButton.setCompoundDrawablesWithIntrinsicBounds(mCancelButtonDrawableLeft, 0, 0, 0);
        }
        if (mNeutralButtonDrawableLeft>0) {
            mNeutralButton.setCompoundDrawablesWithIntrinsicBounds(mNeutralButtonDrawableLeft, 0, 0, 0);
        }

        mListView = findViewById(R.id.list);
        mListViewContainer = findViewById(R.id.list_container);
        mInputContainer = findViewById(R.id.input_container);
        mEditText = findViewById(R.id.input_text);
        mInputLayout = findViewById(R.id.input_layout);

        setTitleText(mTitleText);
        setContentText(mContentText);
        setCustomView(mCustomView);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        setNeutralText(mNeutralText);
        applyStroke();
        setConfirmButtonBackgroundColor(mConfirmButtonBackgroundColor);
        setConfirmButtonTextColor(mConfirmButtonTextColor);
        setCancelButtonBackgroundColor(mCancelButtonBackgroundColor);
        setCancelButtonTextColor(mCancelButtonTextColor);
        setNeutralButtonBackgroundColor(mNeutralButtonBackgroundColor);
        setNeutralButtonTextColor(mNeutralButtonTextColor);
        changeAlertType(mAlertType, true);

        getWindow().setDimAmount(0.5f);
    }

    private void restore() {
        mCustomImage.setVisibility(View.GONE);
        mErrorFrame.setVisibility(View.GONE);
        mSuccessFrame.setVisibility(View.GONE);
        mWarningFrame.setVisibility(View.GONE);
        mProgressFrame.setVisibility(View.GONE);

        mConfirmButton.setVisibility(mHideConfirmButton ? View.GONE : View.VISIBLE);
        mCancelButton.setVisibility(View.GONE);
        mNeutralButton.setVisibility(View.GONE);

        // clear change form previous used
        setConfirmText(getContext().getString(R.string.action_confirm));
        setCancelText(getContext().getString(R.string.action_cancel));
        setNeutralText("");
        mConfirmButtonDrawableLeft = 0;
        mCancelButtonDrawableLeft = 0;
        mNeutralButtonDrawableLeft = 0;
        mConfirmButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mCancelButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mNeutralButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        adjustButtonContainerVisibility();

        //mConfirmButton.setBackgroundResource(R.drawable.green_button_background);
        mErrorFrame.clearAnimation();
        mErrorX.clearAnimation();
        mSuccessTick.clearAnimation();
        mSuccessLeftMask.clearAnimation();
        mSuccessRightMask.clearAnimation();
        mListViewContainer.setVisibility(View.GONE);
        mInputContainer.setVisibility(View.GONE);
    }

    /**
     * Hides buttons container if all buttons are invisible or gone.
     * This deletes useless margins
     */
    private void adjustButtonContainerVisibility() {
        boolean showButtonsContainer = false;
        for (int i = 0; i < mButtonsContainer.getChildCount(); i++) {
            View view = mButtonsContainer.getChildAt(i);
            if (view instanceof Button && view.getVisibility() == View.VISIBLE) {
                showButtonsContainer = true;
                break;
            }
        }
        mButtonsContainer.setVisibility(showButtonsContainer ? View.VISIBLE : View.GONE);
    }

    private void playAnimation() {
        if (mAlertType == ERROR_TYPE) {
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == SUCCESS_TYPE) {
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        }
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore();
            }
            mConfirmButton.setVisibility(mHideConfirmButton ? View.GONE : View.VISIBLE);
            if (mCustomImgDrawable!=null) {
                setCustomImage(mCustomImgDrawable);
            }
            switch (mAlertType) {
                case ERROR_TYPE:
                    mErrorFrame.setVisibility(View.VISIBLE);

                    mConfirmButton.setVisibility(View.VISIBLE);
                    mCancelButton.setVisibility(View.GONE);
                    break;
                case SUCCESS_TYPE:
                    mSuccessFrame.setVisibility(View.VISIBLE);
                    // initial rotate layout of success mask
                    mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                    mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));

                    mConfirmButton.setVisibility(View.VISIBLE);
                    mCancelButton.setVisibility(View.GONE);
                    break;
                case WARNING_TYPE:
                    mWarningFrame.setVisibility(View.VISIBLE);
                    break;
                case CUSTOM_IMAGE_TYPE:
                    setCustomImage(mCustomImgDrawable);
                    break;
                case PROGRESS_TYPE:
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.GONE);
//                    mButtonsContainer.setVisibility(View.GONE);
                    break;
            }
            adjustButtonContainerVisibility();
            if (!fromCreate) {
                playAnimation();
            }

            if(fromCreate) {
                if (mAlertType == TEXT_SELECTION_TYPE) {
                    mListViewContainer.setVisibility(View.VISIBLE);
                    listAdapter = new TextSelectionAdapter(mListItems, getContext());
                    mListView.setAdapter(listAdapter);
                    // can show clear by cancel button
                    if (mCancelClickListener != null) {
                        mButtonsContainer.setVisibility(View.VISIBLE);
                        mConfirmButton.setVisibility(View.GONE);
                    } else {
                        mButtonsContainer.setVisibility(View.GONE);
                    }
                } else if (mAlertType == BUTTON_SELECTION_TYPE) {
                    mListViewContainer.setVisibility(View.VISIBLE);
                    listAdapter = new ButtonSelectionAdapter(mListItems, getContext());
                    mListView.setAdapter(listAdapter);
                    // can show clear by cancel button
                    if (mCancelClickListener != null) {
                        mButtonsContainer.setVisibility(View.VISIBLE);
                        mConfirmButton.setVisibility(View.GONE);
                    } else {
                        mButtonsContainer.setVisibility(View.GONE);
                    }
                } else if (mAlertType == TEXT_INPUT_TYPE) {
                    mInputContainer.setVisibility(View.VISIBLE);
                    mEditText.setText((mDefaultText != null) ? mDefaultText : "");
                    mInputLayout.setHint((mHintText != null) ? mHintText : "กรุณาใส่จำนวน");
                    mEditText.requestFocus();
                }
            }
        }
    }

    public int getAlertType() {
        return mAlertType;
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);
    }


    public String getTitleText() {
        return mTitleText;
    }

    public IamAlertDialog setTitleText(String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            if (text.isEmpty()) {
                mTitleTextView.setVisibility(View.GONE);
            } else {
                mTitleTextView.setVisibility(View.VISIBLE);
                mTitleTextView.setText(Html.fromHtml(mTitleText));
            }
        }
        return this;
    }

    public IamAlertDialog setTitleText(int resId) {
        return setTitleText(getContext().getResources().getString(resId));
    }

    public IamAlertDialog setCustomImage(Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    public IamAlertDialog setCustomImage(int resourceId) {
        return setCustomImage(getContext().getResources().getDrawable(resourceId, null));
    }

    public String getContentText() {
        return mContentText;
    }

    /**
     * @param text text which can contain html tags.
     */
    public IamAlertDialog setContentText(String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            if (contentTextSize != 0) {
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, spToPx(contentTextSize, getContext()));
            }
            mContentTextView.setText(Html.fromHtml(mContentText));
            mContentTextView.setVisibility(View.VISIBLE);
            mCustomViewContainer.setVisibility(View.GONE);
        }
        return this;
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * @param width in SP
     */
    public IamAlertDialog setStrokeWidth(float width) {
        this.strokeWidth = spToPx(width, getContext());
        return this;
    }

    private void applyStroke() {
        if (Float.compare(defStrokeWidth, strokeWidth) != 0) {
            Resources r = getContext().getResources();
            setButtonBackgroundColor(mConfirmButton, r.getColor(R.color.blue, null));
            setButtonBackgroundColor(mNeutralButton, r.getColor(R.color.gray, null));
            setButtonBackgroundColor(mCancelButton, r.getColor(R.color.red, null));
        }
    }

    public boolean isShowCancelButton() {
        return mShowCancel;
    }

    public IamAlertDialog showCancelButton(boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public boolean isShowContentText() {
        return mShowContent;
    }

    public IamAlertDialog showContentText(boolean isShow) {
        mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getCancelText() {
        return mCancelText;
    }

    public IamAlertDialog setCancelText(String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getConfirmText() {
        return mConfirmText;
    }

    public IamAlertDialog setConfirmText(String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public IamAlertDialog setConfirmButtonBackgroundColor(Integer color) {
        mConfirmButtonBackgroundColor = color;
        setButtonBackgroundColor(mConfirmButton, color);
        return this;
    }

    public Integer getConfirmButtonBackgroundColor() {
        return mConfirmButtonBackgroundColor;
    }

    public IamAlertDialog setNeutralButtonBackgroundColor(Integer color) {
        mNeutralButtonBackgroundColor = color;
        setButtonBackgroundColor(mNeutralButton, color);
        return this;
    }

    public Integer getNeutralButtonBackgroundColor() {
        return mNeutralButtonBackgroundColor;
    }

    public IamAlertDialog setCancelButtonBackgroundColor(Integer color) {
        mCancelButtonBackgroundColor = color;
        setButtonBackgroundColor(mCancelButton, color);
        return this;
    }

    public Integer getCancelButtonBackgroundColor() {
        return mCancelButtonBackgroundColor;
    }

    private void setButtonBackgroundColor(Button btn, Integer color) {
        if (btn != null && color != null) {
            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius(ViewUtils.dpToPx(24));
            shape.setColor(ContextCompat.getColor(getContext(), color));
            btn.setBackground(shape);
        }
    }

    public IamAlertDialog setConfirmButtonTextColor(Integer color) {
        mConfirmButtonTextColor = color;
        if (mConfirmButton != null && color != null) {
            mConfirmButton.setTextColor(mConfirmButtonTextColor);
        }
        return this;
    }

    public Integer getConfirmButtonTextColor() {
        return mConfirmButtonTextColor;
    }

    public IamAlertDialog setNeutralButtonTextColor(Integer color) {
        mNeutralButtonTextColor = color;
        if (mNeutralButton != null && color != null) {
            mNeutralButton.setTextColor(mNeutralButtonTextColor);
        }
        return this;
    }

    public Integer getNeutralButtonTextColor() {
        return mNeutralButtonTextColor;
    }

    public IamAlertDialog setCancelButtonTextColor(Integer color) {
        mCancelButtonTextColor = color;
        if (mCancelButton != null && color != null) {
            mCancelButton.setTextColor(mCancelButtonTextColor);
        }
        return this;
    }

    public Integer getCancelButtonTextColor() {
        return mCancelButtonTextColor;
    }

    public IamAlertDialog setCancelClickListener(IamAlertDialog.OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public IamAlertDialog setConfirmClickListener(IamAlertDialog.OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public IamAlertDialog setNeutralText(String text) {
        mNeutralText = text;
        if (mNeutralButton != null && mNeutralText != null && !text.isEmpty()) {
            mNeutralButton.setVisibility(View.VISIBLE);
            mNeutralButton.setText(mNeutralText);
        } else {
            if (mNeutralButton != null) {
                mNeutralButton.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public IamAlertDialog setNeutralClickListener(IamAlertDialog.OnSweetClickListener listener) {
        mNeutralClickListener = listener;
        return this;
    }

    public Integer getConfirmButtonIcon() {
        return mConfirmButtonDrawableLeft;
    }

    public IamAlertDialog setConfirmButtonIcon(Integer drawable) {
        this.mConfirmButtonDrawableLeft = drawable;
        return this;
    }

    public Integer getCancelButtonIcon() {
        return mCancelButtonDrawableLeft;
    }

    public IamAlertDialog setCancelButtonIcon(Integer drawable) {
        this.mCancelButtonDrawableLeft = drawable;
        return this;
    }

    public Integer getNeutralButtonIcon() {
        return mNeutralButtonDrawableLeft;
    }

    public IamAlertDialog setNeutralButtonIcon(Integer drawable) {
        this.mNeutralButtonDrawableLeft = drawable;
        return this;
    }

    public IamAlertDialog showDefaultButtonIcon() {
        this.mConfirmButtonDrawableLeft = R.drawable.ic_baseline_done_24;
        this.mCancelButtonDrawableLeft = R.drawable.ic_baseline_close_24;
        this.mNeutralButtonDrawableLeft = R.drawable.ic_radio_button_unchecked_24;
        return this;
    }

    @Override
    public void setTitle(CharSequence title) {
        this.setTitleText(title.toString());
    }

    @Override
    public void setTitle(int titleId) {
        this.setTitleText(getContext().getResources().getString(titleId));
    }

    public Button getButton(int buttonType) {
        switch (buttonType) {
            default:
            case BUTTON_CONFIRM:
                return mConfirmButton;
            case BUTTON_CANCEL:
                return mCancelButton;
            case BUTTON_NEUTRAL:
                return mNeutralButton;
        }
    }

    public IamAlertDialog setConfirmButton(String text, IamAlertDialog.OnSweetClickListener listener) {
        this.setConfirmText(text);
        this.setConfirmClickListener(listener);
        return this;
    }

    public IamAlertDialog setConfirmButton(int resId, IamAlertDialog.OnSweetClickListener listener) {
        String text = getContext().getResources().getString(resId);
        setConfirmButton(text, listener);
        return this;
    }


    public IamAlertDialog setCancelButton(String text, IamAlertDialog.OnSweetClickListener listener) {
        this.setCancelText(text);
        this.setCancelClickListener(listener);
        return this;
    }

    public IamAlertDialog setCancelButton(int resId, IamAlertDialog.OnSweetClickListener listener) {
        String text = getContext().getResources().getString(resId);
        setCancelButton(text, listener);
        return this;
    }

    public IamAlertDialog setNeutralButton(String text, IamAlertDialog.OnSweetClickListener listener) {
        this.setNeutralText(text);
        this.setNeutralClickListener(listener);
        return this;
    }

    public IamAlertDialog setNeutralButton(int resId, IamAlertDialog.OnSweetClickListener listener) {
        String text = getContext().getResources().getString(resId);
        setNeutralButton(text, listener);
        return this;
    }

    /**
     * Set content text size
     *
     * @param value text size in sp
     */
    public IamAlertDialog setContentTextSize(int value) {
        this.contentTextSize = value;
        return this;
    }

    public int getContentTextSize() {
        return contentTextSize;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }

    /**
     * set custom view instead of message
     *
     * @param view
     */
    public IamAlertDialog setCustomView(View view) {
        mCustomView = view;
        if (mCustomView != null && mCustomViewContainer != null) {
            mCustomViewContainer.addView(view);
            mCustomViewContainer.setVisibility(View.VISIBLE);
            mContentTextView.setVisibility(View.GONE);
        }
        return this;
    }

    public IamAlertDialog setButtonOrientation(int orientation) {
        mButtonOrientation = orientation;
        return this;
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        //several view animations can't be launched at one view, that's why apply alpha animation on child
        ((ViewGroup) mDialogView).getChildAt(0).startAnimation(mOverlayOutAnim); //alpha animation
        mDialogView.startAnimation(mModalOutAnim); //scale animation
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(IamAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null || mTextInputListener != null) {
                if (mConfirmClickListener != null) {
                    mConfirmClickListener.onClick(IamAlertDialog.this);
                }
                if (mTextInputListener != null) {
                    mTextInputListener.onTextInput(IamAlertDialog.this, mEditText.getText().toString());
                }
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.neutral_button) {
            if (mNeutralClickListener != null) {
                mNeutralClickListener.onClick(IamAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }

    public ProgressHelper getProgressHelper() {
        return mProgressHelper;
    }

    public IamAlertDialog setHideKeyBoardOnDismiss(boolean hide) {
        this.mHideKeyBoardOnDismiss = hide;
        return this;
    }

    public boolean isHideKeyBoardOnDismiss() {
        return this.mHideKeyBoardOnDismiss;
    }

    private void hideSoftKeyboard() {
        Activity activity = getOwnerActivity();
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public IamAlertDialog setLargeIcon() {
        // default 32
        this.iconSize = 48;
        return this;
    }

    public IamAlertDialog setNoTintIcon() {
        this.tintFlag = false;
        return this;
    }

    public class TextSelectionAdapter extends ArrayAdapter<DataItem> implements View.OnClickListener{

        private ArrayList<DataItem> dataSet;
        Context mContext;

        // View lookup cache
        private class ViewHolder {
            TextView tvTitle;
            ImageView ivIcon;
            ImageView ivSelected;
            ConstraintLayout layoutItem;
        }

        public TextSelectionAdapter(ArrayList<DataItem> data, Context context) {
            super(context, R.layout.dialog_mysweetalert_listitem, data);
            this.dataSet = data;
            this.mContext=context;
        }

        @Override
        public void onClick(View v) {

            int position=(Integer) v.getTag();
            Object object= getItem(position);
            DataItem data=(DataItem)object;

            if (v.getId() == R.id.layoutItem) {
                if (mSingleSelectionClickListener != null) {
                    mSingleSelectionClickListener.onClick(IamAlertDialog.this, position, data);
                }
            }
        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DataItem data = getItem(position);
            ViewHolder viewHolder;

            final View result;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.dialog_mysweetalert_listitem, parent, false);
                viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
                viewHolder.ivIcon = convertView.findViewById(R.id.ivIcon);

//                if (iconSize != 32) {
//                    // set size
//                    int px = ViewUtils.dpToPx(iconSize);
//                    ViewGroup.LayoutParams params = viewHolder.ivIcon.getLayoutParams();
//                    params.width = px;
//                    params.height = px;
//                    // set margin
////                    px = ViewUtils.dpToPx(64-iconSize);
////                    params.setMargins(px, px, px, px);
//                    viewHolder.ivIcon.setLayoutParams(params);
//                }
                if (data.getIconId()!=0) {
                    viewHolder.ivIcon.setVisibility(View.VISIBLE);
                    if (tintFlag) {
                        viewHolder.ivIcon.setImageTintList(
                                androidx.databinding.adapters.Converters.convertColorToColorStateList(
                                        getContext().getResources().getColor(R.color.gray, null)
                                )
                        );
                    }
                } else {
                    viewHolder.ivIcon.setVisibility(View.GONE);
                }
                viewHolder.ivSelected = convertView.findViewById(R.id.ivSelected);
                viewHolder.layoutItem = convertView.findViewById(R.id.layoutItem);
                result=convertView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            lastPosition = position;

            viewHolder.tvTitle.setText(data.getDescription());
            if (data.getIconId()>0) {
                viewHolder.ivIcon.setImageDrawable(getContext().getDrawable(data.getIconId()));
            }
            viewHolder.layoutItem.setOnClickListener(this);
            viewHolder.layoutItem.setTag(position);
            if (mShowListSelection) {
                viewHolder.ivSelected.setVisibility(View.VISIBLE);
                if (position == mSelectedItem) {
                    viewHolder.ivSelected.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_radio_button_checked_24, null));
                    viewHolder.ivSelected.setImageTintList(
                            androidx.databinding.adapters.Converters.convertColorToColorStateList(
                                    getContext().getColor(R.color.blue)
                            )
                    );
                } else {
                    viewHolder.ivSelected.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_radio_button_unchecked_24, null));
                    viewHolder.ivSelected.setImageTintList(
                            androidx.databinding.adapters.Converters.convertColorToColorStateList(
                                    getContext().getColor(R.color.light_gray)
                            )
                    );
                }
            } else {
                viewHolder.ivSelected.setVisibility(View.GONE);
            }
            return result;
        }
    }

    public class ButtonSelectionAdapter extends ArrayAdapter<DataItem> implements View.OnClickListener{

        private ArrayList<DataItem> dataSet;
        Context mContext;

        // View lookup cache
        private class ViewHolder {
            Button buttonSelection;
        }

        public ButtonSelectionAdapter(ArrayList<DataItem> data, Context context) {
            super(context, R.layout.dialog_mysweetalert_button_listitem, data);
            this.dataSet = data;
            this.mContext=context;
        }

        @Override
        public void onClick(View v) {

            int position=(Integer) v.getTag();
            Object object= getItem(position);
            DataItem data=(DataItem)object;

            if (v.getId() == R.id.buttonSelection) {
                if (mSingleSelectionClickListener != null) {
                    mSingleSelectionClickListener.onClick(IamAlertDialog.this, position, data);
                }
            }
        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DataItem data = getItem(position);
            ViewHolder viewHolder;

            final View result;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.dialog_mysweetalert_button_listitem, parent, false);
                viewHolder.buttonSelection = convertView.findViewById(R.id.buttonSelection);

                if (data.getIconId()!=0) {
                    viewHolder.buttonSelection.setCompoundDrawablesWithIntrinsicBounds(data.getIconId(), 0, 0, 0);
                } else {
                    viewHolder.buttonSelection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (data.getColorId()!=0) {
                    GradientDrawable shape =  new GradientDrawable();
                    shape.setCornerRadius(ViewUtils.dpToPx(24));
                    shape.setColor(ContextCompat.getColor(getContext(), data.getColorId()));
                    viewHolder.buttonSelection.setBackground(shape);
                }
                result=convertView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            lastPosition = position;

            viewHolder.buttonSelection.setText(data.getDescription());
            viewHolder.buttonSelection.setOnClickListener(this);
            viewHolder.buttonSelection.setTag(position);
            if (mShowListSelection) {
//                viewHolder.ivSelected.setVisibility(View.VISIBLE);
//                if (position == mSelectedItem) {
//                    viewHolder.ivSelected.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_radio_button_checked_24, null));
//                    viewHolder.ivSelected.setImageTintList(
//                            androidx.databinding.adapters.Converters.convertColorToColorStateList(
//                                    getContext().getColor(R.color.blue)
//                            )
//                    );
//                } else {
//                    viewHolder.ivSelected.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_radio_button_unchecked_24, null));
//                    viewHolder.ivSelected.setImageTintList(
//                            androidx.databinding.adapters.Converters.convertColorToColorStateList(
//                                    getContext().getColor(R.color.light_gray)
//                            )
//                    );
//                }
//            } else {
//                viewHolder.ivSelected.setVisibility(View.GONE);
            }
            return result;
        }
    }

    public IamAlertDialog setListItems(List<DataItem> items, OnSingleSelectionClickListener listener) {
        return setListItems(items, listener, false, -1);
    }

    public IamAlertDialog setListItems(List<DataItem> items, OnSingleSelectionClickListener listener, boolean showListSelection, int selected) {
        mListItems.clear();
        mListItems.addAll(items);
        mSingleSelectionClickListener = listener;
        mShowListSelection = showListSelection;
        mSelectedItem = selected;
        return this;
    }

    public IamAlertDialog setTextInputListener(String defaultText, String hintText, IamAlertDialog.OnSweetTextInputListener listener) {
        mTextInputListener = listener;
        mDefaultText = defaultText;
        mHintText = hintText;
        return this;
    }
}