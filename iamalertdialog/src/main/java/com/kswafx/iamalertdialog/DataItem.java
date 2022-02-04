package com.kswafx.iamalertdialog;

public class DataItem implements Listable {

    private int iconId = 0;
    private int colorId = 0;
    private String code;
    private String description;

    public DataItem(String code, String description) {
        this.iconId = 0;
        this.colorId = 0;
        this.code = code;
        this.description = description;
    }

    public DataItem(int iconId, int colorId, String code, String description) {
        this.iconId = iconId;
        this.colorId = colorId;
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    @Override
    public String getLabel() {
        return description;
    }

    @Override
    public int getIcon() {
        return iconId;
    }
}
