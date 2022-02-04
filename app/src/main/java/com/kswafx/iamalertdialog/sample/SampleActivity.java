package com.kswafx.iamalertdialog.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import com.kswafx.iamalertdialog.DataItem;
import com.kswafx.iamalertdialog.Constants;
import com.kswafx.iamalertdialog.IamAlertDialog;

public class SampleActivity extends Activity implements View.OnClickListener {

    private int i = -1;

    public static int selectedConsole = 0;

    public static List<DataItem> selectionItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);


        int[] btnIds = {
                R.id.basic_test, R.id.under_text_test,
                R.id.error_text_test, R.id.success_text_test, R.id.warning_confirm_test, R.id.warning_cancel_test,
                R.id.custom_img_test, R.id.progress_dialog, R.id.neutral_btn_test, R.id.vertical_btn_test,
                R.id.single_selection_test, R.id.custom_single_selection_test, R.id.button_selection_test,
                R.id.number_input_test, R.id.button_icon_test,
                R.id.custom_btn_colors_test
        };
        for (Integer id : btnIds) {
            findViewById(id).setOnClickListener(this);
            findViewById(id).setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basic_test:
                new IamAlertDialog(this)
                        .setContentText("Here's a message")
                        .show();
                break;
            case R.id.under_text_test:
                new IamAlertDialog(this)
                        .setTitleText("Title")
                        .setContentText("It's pretty, isn't it?")
                        .show();
                break;
            case R.id.error_text_test:
                new IamAlertDialog(this, IamAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .show();
                break;
            case R.id.success_text_test:
                new IamAlertDialog(this, IamAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Good job!")
                        .setContentText("You clicked the button!")
                        .show();
                break;
            case R.id.warning_confirm_test:
                new IamAlertDialog(this, IamAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setConfirmText("Later")
                        .setCancelButton("Delete", IamAlertDialog -> {
                            // reuse previous dialog instance
                            IamAlertDialog.setTitleText("Deleted!")
                                    .setContentText("Your imaginary file has been deleted!")
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(null)
                                    .changeAlertType(IamAlertDialog.SUCCESS_TYPE);
                        })
                        .show();
                break;
            case R.id.warning_cancel_test:
                new IamAlertDialog(this, IamAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .showCancelButton(true)
                        .setCancelClickListener(sDialog -> {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText("Cancelled!")
                                    .setContentText("Your imaginary file is safe :)")
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(null)
                                    .changeAlertType(IamAlertDialog.ERROR_TYPE);
                        })
                        .setConfirmClickListener(sDialog -> sDialog.setTitleText("Deleted!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmClickListener(null)
                                .setCancelClickListener(null)
                                .changeAlertType(IamAlertDialog.SUCCESS_TYPE))
                        .show();
                break;
            case R.id.custom_img_test:
                new IamAlertDialog(this, IamAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Sweet!")
                        .setContentText("Here's a custom image.")
                        .setCustomImage(R.drawable.social)
                        .show();
                break;
            case R.id.progress_dialog:
                final IamAlertDialog pDialog = new IamAlertDialog(this, IamAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                new CountDownTimer(800 * 3, 800) {
                    public void onTick(long millisUntilFinished) {
                        // you can change the progress bar color by ProgressHelper every 800 millis
                        i++;
                        switch (i) {
                            case 0:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                break;
                            case 1:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                break;
                            case 2:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                            case 3:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                break;
                            case 4:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                break;
                            case 5:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                break;
                            case 6:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                        }
                    }

                    public void onFinish() {
                        i = -1;
                        pDialog.setTitleText("Success!")
                                .changeAlertType(IamAlertDialog.SUCCESS_TYPE);
                    }
                }.start();
                break;

            case R.id.neutral_btn_test:
                new IamAlertDialog(this, IamAlertDialog.NORMAL_TYPE)
                        .setTitleText("Title")
                        .setContentText("Three buttons dialog")
                        .setConfirmText("Ok")
                        .setCancelText("Stop")
                        .setNeutralText("Later")
                        .setConfirmClickListener(IamAlertDialog::dismissWithAnimation)
                        .setNeutralClickListener(IamAlertDialog::dismissWithAnimation)
                        .setCancelClickListener(IamAlertDialog::dismissWithAnimation)
                        .show();
                break;

            case R.id.button_icon_test:
                new IamAlertDialog(this, IamAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirm to delete")
                        .setContentText("It Won't be able to recover this file!")
                        .setConfirmText("Cancel")
                        .setConfirmButtonIcon(R.drawable.ic_baseline_close_24)
                        .setCancelButtonIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setCancelButton("Delete", new IamAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(IamAlertDialog IamAlertDialog) {
                                // reuse previous dialog instance
                                IamAlertDialog.setTitleText("Deleted!")
                                        .setContentText("Your imaginary file has been deleted!")
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(null)
                                        .changeAlertType(IamAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
                break;

            case R.id.vertical_btn_test:
                new IamAlertDialog(this, IamAlertDialog.NORMAL_TYPE)
                        .setTitleText("Title")
                        .setContentText("Vertical button dialog")
                        .setConfirmText("Vertical #1")
                        .setCancelText("Vertical #2")
                        .setNeutralText("Vertical #3")
                        .setConfirmClickListener(IamAlertDialog::dismissWithAnimation)
                        .setNeutralClickListener(IamAlertDialog::dismissWithAnimation)
                        .setCancelClickListener(IamAlertDialog::dismissWithAnimation)
                        .setButtonOrientation(LinearLayout.VERTICAL)
                        .show();
                break;

            case R.id.custom_btn_colors_test:
                new IamAlertDialog(this, IamAlertDialog.NORMAL_TYPE)
                        .setTitleText("Custom button color")
                        .setButtonOrientation(LinearLayout.VERTICAL)
                        .setCancelButton("brown darken", null)
                        .setCancelButtonBackgroundColor(R.color.brown_darken_3)
                        .setNeutralButton("brown", null)
                        .setNeutralButtonBackgroundColor(R.color.brown)
                        .setConfirmButton("brown lighten", null)
                        .setConfirmButtonBackgroundColor(R.color.brown_lighten_2)
                        .show();
                break;
            case R.id.single_selection_test:
                selectionItems.clear();
                selectionItems.add(new DataItem("1", "Cash"));
                selectionItems.add(new DataItem("2", "Credit Card"));
                selectionItems.add(new DataItem("3", "Bank Transfer"));
                selectionItems.add(new DataItem("4", "Paypal"));
                new IamAlertDialog(this, IamAlertDialog.TEXT_SELECTION_TYPE)
                        .setContentText("Select payment method")
                        .setListItems(selectionItems, (dialog, position, data) -> {
                            dialog.dismissWithAnimation();
                            new IamAlertDialog(this, IamAlertDialog.NORMAL_TYPE)
                                    .setContentText("You select " + data.getDescription())
                                    .show();
                        })
                        .show();
                break;
            case R.id.custom_single_selection_test:
                selectionItems.clear();
                selectionItems.add(new DataItem(R.drawable.ic_baseline_favorite_24, 0, "1", "Atari 2600"));
                selectionItems.add(new DataItem(R.drawable.ic_baseline_favorite_24, 0, "2", "Sega Megadrive"));
                selectionItems.add(new DataItem(R.drawable.ic_baseline_favorite_24, 0, "3", "NEC PC Engine"));
                selectionItems.add(new DataItem(R.drawable.ic_baseline_favorite_24, 0, "4", "Sony Playstation"));
                selectionItems.add(new DataItem(R.drawable.ic_close_24dp, 0, "0", "Not interested"));
                selectionItems.add(new DataItem(0, 0, "9", "I like every console"));
                new IamAlertDialog(this, IamAlertDialog.TEXT_SELECTION_TYPE)
                        .setCustomImage(R.drawable.game_controller)
                        .setContentText("Select console you love")
                        .setListItems(selectionItems, (dialog, position, data) -> {
                            selectedConsole = position;
                            dialog.dismissWithAnimation();
                            new IamAlertDialog(this, IamAlertDialog.NORMAL_TYPE)
                                    .setContentText("You select " + data.getDescription())
                                    .show();
                        }, true, selectedConsole)
                        .show();
                break;
            case R.id.button_selection_test:
                selectionItems.clear();
                selectionItems.add(new DataItem(R.drawable.ic_social_facebook, R.color.blue_darken_3, "1", "Facebook"));
                selectionItems.add(new DataItem(R.drawable.ic_social_github, R.color.green_darken_3, "2", "Github"));
                selectionItems.add(new DataItem(R.drawable.ic_social_twitter, R.color.light_blue, "3", "Twitter"));
                selectionItems.add(new DataItem(R.drawable.ic_social_instagram, R.color.purple_lighten_1, "4", "Instagram"));
                selectionItems.add(new DataItem(R.drawable.ic_social_pinterest, R.color.red_darken_4, "5", "Pinterest"));
                new IamAlertDialog(this, IamAlertDialog.BUTTON_SELECTION_TYPE)
                        .setCustomImage(R.drawable.social)
                        .setContentText("Choose your favorite social")
                        .setListItems(selectionItems, (dialog, position, data) -> {
                            selectedConsole = position;
                            dialog.dismissWithAnimation();
                            new IamAlertDialog(this, IamAlertDialog.NORMAL_TYPE)
                                    .setContentText("You select " + data.getDescription())
                                    .show();
                        })
                        .show();
                break;
            case R.id.number_input_test:
                new IamAlertDialog(this, IamAlertDialog.TEXT_INPUT_TYPE)
                        .setContentText("Enter number (10 digit integer)")
                        .setTextInputListener("100", "Edit number", (dialog, value) -> {
                            dialog.dismissWithAnimation();
                            new IamAlertDialog(this, IamAlertDialog.NORMAL_TYPE)
                                    .setContentText("You have entered " + value)
                                    .show();
                        })
                        .show();
                break;
        }
    }
}
