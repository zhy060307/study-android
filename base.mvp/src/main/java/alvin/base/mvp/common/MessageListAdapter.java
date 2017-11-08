package alvin.base.mvp.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import alvin.base.mvp.R;
import alvin.base.mvp.domain.models.Message;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final Constract.Presenter presenter;

    private List<Message> messages = Collections.emptyList();

    MessageListAdapter(@NonNull Context context,
                       @NonNull Constract.Presenter presenter) {
        this.inflater = LayoutInflater.from(context);
        this.presenter = presenter;
        this.messages = messages;
    }

    public void update(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Message item = messages.get(position);

        holder.tvMessage.setText(item.getMessage());
        holder.tvTimestamp.setText(item.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
        holder.btnDelete.setOnClickListener(b -> presenter.deleteMessage(item.getId()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_message)
        TextView tvMessage;

        @BindView(R.id.tv_timestamp)
        TextView tvTimestamp;

        @BindView(R.id.btn_delete)
        ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
