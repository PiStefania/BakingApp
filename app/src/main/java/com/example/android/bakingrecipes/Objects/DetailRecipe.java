package com.example.android.bakingrecipes.Objects;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailRecipe implements Parcelable{
    private String detailTitle;
    private String detailVideo;
    private String detailInstructions;

    public DetailRecipe(String detailTitle, String detailVideo, String detailInstructions) {
        this.detailTitle = detailTitle;
        this.detailVideo = detailVideo;
        this.detailInstructions = detailInstructions;
    }

    public static final Creator<DetailRecipe> CREATOR = new Creator<DetailRecipe>() {
        @Override
        public DetailRecipe createFromParcel(Parcel in) {
            return new DetailRecipe(in);
        }

        @Override
        public DetailRecipe[] newArray(int size) {
            return new DetailRecipe[size];
        }
    };

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    public String getDetailVideo() {
        return detailVideo;
    }

    public void setDetailVideo(String detailVideo) {
        this.detailVideo = detailVideo;
    }

    public String getDetailInstructions() {
        return detailInstructions;
    }

    public void setDetailInstructions(String detailInstructions) {
        this.detailInstructions = detailInstructions;
    }

    public DetailRecipe(Parcel in) {
        detailTitle = in.readString();
        detailVideo = in.readString();
        detailInstructions = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(detailTitle);
        parcel.writeString(detailVideo);
        parcel.writeString(detailInstructions);
    }
}
