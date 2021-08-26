package com.augurit.agmobile.mapengine.map.graphic;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;


/**
 * 图标覆盖物
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.intro.maps
 * @createTime 创建时间 ：2017-01-18
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-18 9:55
 */
public final class PictureMarkerOverlay extends Overlay implements Parcelable {
    public static final Creator<PictureMarkerOverlay> CREATOR =
            new Creator<PictureMarkerOverlay>() {
                public PictureMarkerOverlay createFromParcel(Parcel in) {
                    return new PictureMarkerOverlay(in);
                }

                public PictureMarkerOverlay[] newArray(int size) {
                    return new PictureMarkerOverlay[size];
                }
            };
    protected LatLng position;
    protected String snippet;
    protected String title;
    protected Bitmap icon;

    /**
     * Defines options for a Symbol.
     */
    public PictureMarkerOverlay() {
    }

    protected PictureMarkerOverlay(Parcel in) {
        position((LatLng) in.readParcelable(LatLng.class.getClassLoader()));
        snippet(in.readString());
        title(in.readString());
        if (in.readByte() != 0) {
            // this means we have an icon
            Bitmap iconBitmap = in.readParcelable(Bitmap.class.getClassLoader());
            icon(icon);
        }
    }

    /**
     * Set the geographical location of the Symbol.
     *
     * @param position the location to position the Symbol.
     * @return the object for which the method was called.
     */
    public PictureMarkerOverlay position(LatLng position) {
        this.position = position;
        return getThis();
    }

    /**
     * Set the snippet of the Symbol.
     *
     * @param snippet the snippet of the Symbol.
     * @return the object for which the method was called.
     */
    public PictureMarkerOverlay snippet(String snippet) {
        this.snippet = snippet;
        return getThis();
    }

    /**
     * Set the title of the Symbol.
     *
     * @param title the title of the Symbol.
     * @return the object for which the method was called.
     */
    public PictureMarkerOverlay title(String title) {
        this.title = title;
        return getThis();
    }

    /**
     * Set the icon of the Symbol.
     *
     * @param icon the icon of the Symbol.
     * @return the object for which the method was called.
     */
    public PictureMarkerOverlay icon(Bitmap icon) {
        this.icon = icon;
        return getThis();
    }

    public PictureMarkerOverlay getThis() {
        return this;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return integer 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param out   The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or
     *              {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(getPosition(), flags);
        out.writeString(getSnippet());
        out.writeString(getTitle());
        Bitmap icon = getIcon();
        out.writeByte((byte) (icon != null ? 1 : 0));
        if (icon != null) {
            out.writeParcelable(icon, flags);
        }
    }

    /**
     * Returns the position set for this {@link PictureMarkerOverlay} object.
     *
     * @return A {@link LatLng} object specifying the marker's current position.
     */
    public LatLng getPosition() {
        return position;
    }

    /**
     * Set the geographical location of the Symbol.
     *
     * @param position the location to position the Symbol.
     * @return the object for which the method was called.
     */
    public PictureMarkerOverlay setPosition(LatLng position) {
        return position(position);
    }

    /**
     * Gets the snippet set for this {@link PictureMarkerOverlay} object.
     *
     * @return A string containing the marker's snippet.
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     * Set the snippet of the Symbol.
     *
     * @param snippet the snippet of the Symbol.
     * @return the object for which the method was called.
     */
    public PictureMarkerOverlay setSnippet(String snippet) {
        return snippet(snippet);
    }

    /**
     * Gets the title set for this {@link PictureMarkerOverlay} object.
     *
     * @return A string containing the marker's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the Symbol.
     *
     * @param title the title of the Symbol.
     * @return the object for which the method was called.
     */
    public PictureMarkerOverlay setTitle(String title) {
        return title(title);
    }

    /**
     * Gets the custom icon set for this {@link PictureMarkerOverlay} object.
     *
     * @return A {@link Bitmap} object that the marker is using. If the icon wasn't set, default icon
     * will return.
     */
    public Bitmap getIcon() {
        return icon;
    }

    /**
     * Set the icon of the Symbol.
     *
     * @param icon the icon of the Symbol.
     * @return the object for which the method was called.
     */
    public PictureMarkerOverlay setIcon(Bitmap icon) {
        return icon(icon);
    }

    /**
     * Compares this {@link PictureMarkerOverlay} object with another {@link PictureMarkerOverlay} and
     * determines if their properties match.
     *
     * @param o Another {@link PictureMarkerOverlay} to compare with this object.
     * @return True if marker properties match this {@link PictureMarkerOverlay} object.
     * Else, false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PictureMarkerOverlay marker = (PictureMarkerOverlay) o;

        if (getPosition() != null ? !getPosition().equals(marker.getPosition()) : marker.getPosition() != null) {
            return false;
        }
        if (getSnippet() != null ? !getSnippet().equals(marker.getSnippet()) : marker.getSnippet() != null) {
            return false;
        }
        if (getIcon() != null ? !getIcon().equals(marker.getIcon()) : marker.getIcon() != null) {
            return false;
        }
        return !(getTitle() != null ? !getTitle().equals(marker.getTitle()) : marker.getTitle() != null);
    }

    /**
     * Gives an integer which can be used as the bucket number for storing elements of the set/map.
     * This bucket number is the address of the element inside the set/map. There's no guarantee
     * that this hash value will be consistent between different Java implementations, or even
     * between different execution runs of the same program.
     *
     * @return integer value you can use for storing element.
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (getPosition() != null ? getPosition().hashCode() : 0);
        result = 31 * result + (getSnippet() != null ? getSnippet().hashCode() : 0);
        result = 31 * result + (getIcon() != null ? getIcon().hashCode() : 0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        return result;
    }
}
