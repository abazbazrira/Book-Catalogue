package id.bazrira.bookcatalogue.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_NAME = "book";

    static final class BookColumns implements BaseColumns {
        static int CODE = Integer.parseInt("code");
        static String TITLE = "title";
        static String AUTHOR = "author";
    }
}
