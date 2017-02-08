package com.framgia.soundcloud_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tri on 02/02/2017.
 */
public class Category implements Parcelable {
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    private int mCategoryImage;
    private String mCategoryTitle;
    private String mCategoryParam;

    public Category() {
    }

    protected Category(Parcel in) {
        mCategoryImage = in.readInt();
        mCategoryTitle = in.readString();
        mCategoryParam = in.readString();
    }

    public int getCategoryImage() {
        return mCategoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        mCategoryImage = categoryImage;
    }

    public String getCategoryTitle() {
        return mCategoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        mCategoryTitle = categoryTitle;
    }

    public String getCategoryParam() {
        return mCategoryParam;
    }

    public void setCategoryParam(String categoryParam) {
        mCategoryParam = categoryParam;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mCategoryImage);
        parcel.writeString(mCategoryTitle);
        parcel.writeString(mCategoryParam);
    }
}
