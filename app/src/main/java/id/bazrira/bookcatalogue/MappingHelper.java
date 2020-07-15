package id.bazrira.bookcatalogue;

import android.database.Cursor;

import java.util.ArrayList;

import id.bazrira.bookcatalogue.db.DatabaseContract;
import id.bazrira.bookcatalogue.entity.Book;

public class MappingHelper {

    public static ArrayList<Book> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<Book> notesList = new ArrayList<>();
        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.BookColumns._ID));
            String code = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.BookColumns.CODE));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.BookColumns.TITLE));
            String author = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.BookColumns.AUTHOR));
            notesList.add(new Book(id, code, title, author));
        }
        return notesList;
    }
}
