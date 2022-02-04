I am Alert Dialog
===================
Android library, enhanced version of SweetAlertDialog (Fork from https://github.com/F0RIS/sweet-alert-dialog)

## Setup
```
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation 'com.github.kasamsun:iam-alert-dialog:1.0.0'
    }
```

## Usage
Basic message：
```
    new IamAlertDialog(this)
            .setContentText("Here's a message")
            .show();

```

Message with title：
```
    new IamAlertDialog(this)
            .setTitleText("Title")
            .setContentText("It's pretty, isn't it?")
            .show();
```

Error message：
```
    new IamAlertDialog(this, IamAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText("Something went wrong!")
            .show();
```

Success message：
```
    new IamAlertDialog(this, IamAlertDialog.SUCCESS_TYPE)
            .setTitleText("Good job!")
            .setContentText("You clicked the button!")
            .show();
```

Warning message with listener：
```
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
```

Message with a custom icon：
```
    new IamAlertDialog(this, IamAlertDialog.CUSTOM_IMAGE_TYPE)
            .setTitleText("Sweet!")
            .setContentText("Here's a custom image.")
            .setCustomImage(R.drawable.custom_img)
            .show();
```

Custom confirm/cancel/neutral button：
```
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

```

Custom button icon：
```
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

```

Vertical button：
```
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

```

Custom button color：
```
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

```

Single selection list：
```
    List<DataItem> selectionItems = new ArrayList<>();
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

```

Custom single selection list：
```
    List<DataItem> selectionItems = new ArrayList<>();
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

```

Custom single selection button：
```
    List<DataItem> selectionItems = new ArrayList<>();
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

```

Number input dialog：
```
    new IamAlertDialog(this, IamAlertDialog.TEXT_INPUT_TYPE)
            .setContentText("Enter number (10 digit integer)")
            .setTextInputListener("100", "Edit number", (dialog, value) -> {
                dialog.dismissWithAnimation();
                new IamAlertDialog(this, IamAlertDialog.NORMAL_TYPE)
                        .setContentText("You have entered " + value)
                        .show();
            })
            .show();

```
