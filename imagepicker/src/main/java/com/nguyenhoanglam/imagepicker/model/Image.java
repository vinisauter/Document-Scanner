package com.nguyenhoanglam.imagepicker.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Vinicius Sauter on 7/31/16.
 * ...
 */
public class Image implements Parcelable {
    private long id;
    private String name;
    private String path;
    private boolean isSelected;
    private String url;

    public Image(long id, String name, String path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isSelected = isSelected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    @SuppressWarnings("WeakerAccess")
    protected Image(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.path = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return name != null ? name.equals(image.name) : image.name == null && (path != null ? path.equals(image.path) : image.path == null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    public File getFile() {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists())
                return file;
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
