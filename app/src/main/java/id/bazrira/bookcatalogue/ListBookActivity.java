package id.bazrira.bookcatalogue;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import id.bazrira.bookcatalogue.adapter.BookAdapter;
import id.bazrira.bookcatalogue.db.BookHelper;
import id.bazrira.bookcatalogue.entity.Book;

public class ListBookActivity extends AppCompatActivity implements LoadBooksCallback{

    private ProgressBar progressBar;
    private RecyclerView rvBooks;
    private BookAdapter adapter;
    private FloatingActionButton fabAdd;
    private BookHelper noteHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Books");

        progressBar = findViewById(R.id.progressbar);
        rvBooks = findViewById(R.id.rv_notes);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        rvBooks.setHasFixedSize(true);
        adapter = new BookAdapter(this);
        rvBooks.setAdapter(adapter);

        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListBookActivity.this, MainActivity.class);
                startActivityForResult(intent, MainActivity.REQUEST_ADD);
            }
        });

        noteHelper = BookHelper.getInstance(getApplicationContext());
        noteHelper.open();

        if (savedInstanceState == null) {
            new LoadBooksAsync(noteHelper, this).execute();
        } else {
            ArrayList<Book> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListBooks(list);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListBooks());
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Book> notes) {
        progressBar.setVisibility(View.INVISIBLE);
        if (notes.size() > 0) {
            adapter.setListBooks(notes);
        } else {
            adapter.setListBooks(new ArrayList<Book>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

    private static class LoadBooksAsync extends AsyncTask<Void, Void, ArrayList<Book>> {

        private final WeakReference<BookHelper> weakBookHelper;
        private final WeakReference<LoadBooksCallback> weakCallback;

        private LoadBooksAsync(BookHelper noteHelper, LoadBooksCallback callback) {
            weakBookHelper = new WeakReference<>(noteHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Book> doInBackground(Void... voids) {
            Cursor dataCursor = weakBookHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Book> notes) {
            super.onPostExecute(notes);

            weakCallback.get().postExecute(notes);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == MainActivity.REQUEST_ADD) {
                if (resultCode == MainActivity.RESULT_ADD) {
                    Book note = data.getParcelableExtra(MainActivity.EXTRA_BOOK);

                    adapter.addItem(note);
                    rvBooks.smoothScrollToPosition(adapter.getItemCount() - 1);

                    showSnackbarMessage("Satu item berhasil ditambahkan");
                }
            } else if (requestCode == MainActivity.REQUEST_UPDATE) {
                if (resultCode == MainActivity.RESULT_UPDATE) {

                    Book note = data.getParcelableExtra(MainActivity.EXTRA_BOOK);
                    int position = data.getIntExtra(MainActivity.EXTRA_POSITION, 0);

                    adapter.updateItem(position, note);
                    rvBooks.smoothScrollToPosition(position);

                    showSnackbarMessage("Satu item berhasil diubah");
                } else if (resultCode == MainActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(MainActivity.EXTRA_POSITION, 0);

                    adapter.removeItem(position);

                    showSnackbarMessage("Satu item berhasil dihapus");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteHelper.close();
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvBooks, message, Snackbar.LENGTH_SHORT).show();
    }
}


interface LoadBooksCallback {
    void preExecute();
    void postExecute(ArrayList<Book> notes);
}