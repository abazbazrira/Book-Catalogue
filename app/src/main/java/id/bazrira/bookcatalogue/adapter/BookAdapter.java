package id.bazrira.bookcatalogue.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.bazrira.bookcatalogue.CustomOnItemClickListener;
import id.bazrira.bookcatalogue.MainActivity;
import id.bazrira.bookcatalogue.R;
import id.bazrira.bookcatalogue.entity.Book;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private ArrayList<Book> listBooks = new ArrayList<>();
    private Activity activity;

    public BookAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Book> getListBooks() {
        return listBooks;
    }

    public void setListBooks(ArrayList<Book> listBooks) {
        if (listBooks.size() > 0) {
            this.listBooks.clear();
        }
        this.listBooks.addAll(listBooks);

        notifyDataSetChanged();
    }

    public void addItem(Book note) {
        this.listBooks.add(note);
        notifyItemInserted(listBooks.size() - 1);
    }

    public void updateItem(int position, Book note) {
        this.listBooks.set(position, note);
        notifyItemChanged(position, note);
    }

    public void removeItem(int position) {
        this.listBooks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listBooks.size());
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        holder.tvCode.setText(listBooks.get(position).getCode());
        holder.tvTitle.setText(listBooks.get(position).getTitle());
        holder.tvAuthor.setText(listBooks.get(position).getAuthor());
        holder.cvBook.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Log.d("ITEM-CLICK", "card view onClick");
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_POSITION, position);
                intent.putExtra(MainActivity.EXTRA_BOOK, listBooks.get(position));
                activity.startActivityForResult(intent, MainActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listBooks.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCode, tvTitle, tvAuthor;
        final CardView cvBook;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCode = itemView.findViewById(R.id.tv_item_book_code);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvAuthor = itemView.findViewById(R.id.tv_item_author);
            cvBook = itemView.findViewById(R.id.cv_item_book);
        }
    }
}
