package id.bazrira.bookcatalogue;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import id.bazrira.bookcatalogue.db.BookHelper;
import id.bazrira.bookcatalogue.entity.Book;

import static id.bazrira.bookcatalogue.db.DatabaseContract.BookColumns.AUTHOR;
import static id.bazrira.bookcatalogue.db.DatabaseContract.BookColumns.CODE;
import static id.bazrira.bookcatalogue.db.DatabaseContract.BookColumns.TITLE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtBookCode, edtTitle, edtAuthor;
    private Button btnSubmit;

    private boolean isEdit = false;
    private Book book;
    private int position;
    private BookHelper bookHelper;

    public static final String EXTRA_BOOK = "extra_book";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    private final int ALERT_DIALOG_CLOSE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtBookCode = findViewById(R.id.et_book_code);
        edtTitle = findViewById(R.id.et_title);
        edtAuthor = findViewById(R.id.et_author);
        btnSubmit = findViewById(R.id.btn_submit);

        bookHelper = BookHelper.getInstance(getApplicationContext());
        bookHelper.open();

        book = getIntent().getParcelableExtra(EXTRA_BOOK);
        if (book != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            book = new Book();
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit) {
            actionBarTitle = "Ubah";
            btnTitle = "Update";

            if (book != null) {
                edtBookCode.setText(book.getCode());
                edtTitle.setText(book.getTitle());
                edtAuthor.setText(book.getAuthor());
            }
        } else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSubmit.setText(btnTitle);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            String bookCode = edtBookCode.getText().toString().trim();
            String title = edtTitle.getText().toString().trim();
            String author = edtAuthor.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                edtTitle.setError("Field can not be blank");
                return;
            }

            book.setCode(bookCode);
            book.setTitle(title);
            book.setAuthor(author);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();
            values.put(CODE, bookCode);
            values.put(TITLE, title);
            values.put(AUTHOR, author);

            if (isEdit) {
                long result = bookHelper.update(String.valueOf(book.getId()), values);
                if (result > 0) {
                    intent.putExtra(EXTRA_BOOK, book);
                    setResult(RESULT_UPDATE, intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
                }
            } else {
                long result = bookHelper.insert(values);

                if (result > 0) {
                    book.setId((int) result);
                    intent.putExtra(EXTRA_BOOK, book);
                    setResult(RESULT_ADD, intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Gagal menambah data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ALERT_DIALOG_DELETE = 20;
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Book";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            long result = bookHelper.deleteById(String.valueOf(book.getId()));
                            if (result > 0) {
                                Intent intent = new Intent();
                                intent.putExtra(EXTRA_POSITION, position);
                                setResult(RESULT_DELETE, intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
