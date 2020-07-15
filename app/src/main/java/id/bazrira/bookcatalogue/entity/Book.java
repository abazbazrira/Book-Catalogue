package id.bazrira.bookcatalogue.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private int code;
    private String title;
    private String author;

    public Book(int code, String title, String author) {
        this.code = code;
        this.title = title;
        this.author = author;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.title);
        dest.writeString(this.author);
    }

    public Book() {
    }

    private Book(Parcel in) {
        this.code = in.readInt();
        this.title = in.readString();
        this.author = in.readString();
    }

    private static final Parcelable.Creator<Book> CREATOR =
            new Parcelable.Creator<Book>() {

        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
