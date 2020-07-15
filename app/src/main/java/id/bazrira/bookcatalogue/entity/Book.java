package id.bazrira.bookcatalogue.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private int id;
    private String code;
    private String title;
    private String author;

    public Book(int id, String code, String title, String author) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
        dest.writeInt(this.id);
        dest.writeString(this.code);
        dest.writeString(this.title);
        dest.writeString(this.author);
    }

    public Book() {
    }

    private Book(Parcel in) {
        this.id = in.readInt();
        this.code = in.readString();
        this.title = in.readString();
        this.author = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {

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
