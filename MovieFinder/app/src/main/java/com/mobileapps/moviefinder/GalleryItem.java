package com.mobileapps.moviefinder;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/* Modeled after Adam Champion's GalleryItem class from his TicTacToeNew application */

public class GalleryItem {
	@SerializedName("title")
	private String mTitle = "";

	@SerializedName("id")
	private String mId = "";

	@SerializedName("url_s")
	private String mUrl = "";

	@SerializedName("overview")
	private String mOverview = "";

	@SerializedName("releaseDate")
	private String mReleaseDate = "";

	public GalleryItem(String title, String id, String url, String overview, String releaseDate) {
		mTitle = title;
		mId = id;
		mUrl = url;

		mOverview = overview;
		mReleaseDate = releaseDate;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public String getOverview() {
		return mOverview;
	}

	public void setOverview(String overview) {
		this.mOverview = overview;
	}

	public String getReleaseDate() {
		return mReleaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.mReleaseDate = releaseDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GalleryItem that = (GalleryItem) o;
		return mTitle.equals(that.mTitle) && mId.equals(that.mId) && Objects.equals(mUrl, that.mUrl);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mTitle, mId, mUrl);
	}

	@Override
	public String toString() {
		return "GalleryItem{" +
				"mTitle='" + mTitle + '\'' +
				", mId='" + mId + '\'' +
				", mUrl='" + mUrl + '\'' +
				", mOverview='" + mOverview + '\'' +
				", mReleaseDate='" + mReleaseDate + '\'' +
				'}';
	}
}
