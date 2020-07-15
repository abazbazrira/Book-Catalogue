package id.bazrira.bookcatalogue.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_NAME = "book";

    public static final class BookColumns implements BaseColumns {
        public static String CODE = "code";
        public static String TITLE = "title";
        public static String AUTHOR = "author";
    }
}
