package gr.mmv.gethelp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by iceberg on 5/8/2018.
 */

public class EmerListViewAdapter extends ArrayAdapter<Emergency> {

    private List<Emergency> emergencyLists;

    private Context mCtx;

    TextView noteTextView, statusTextView, userNameTextView, userTelTextView, datetimeTextView;

    public EmerListViewAdapter(List<Emergency> emergencyLists, Context mCtx) {
        super(mCtx, R.layout.emer_custom_list, emergencyLists);
        this.emergencyLists = emergencyLists;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.emer_custom_list, null, true);

        noteTextView = (TextView) listViewItem.findViewById(R.id.noteTextView);
        statusTextView =  (TextView) listViewItem.findViewById(R.id.statusTextView);
        userNameTextView = (TextView) listViewItem.findViewById(R.id.userNameTextView);
        userTelTextView= (TextView) listViewItem.findViewById(R.id.userTelTextView);
        datetimeTextView = (TextView) listViewItem.findViewById(R.id.datetimeTextView);

        Emergency emergency = emergencyLists.get(position);

        noteTextView.setText(emergency.getNote());
        statusTextView.setText(emergency.getStatus());
        userNameTextView.setText(emergency.getUser_report_name());
        userTelTextView.setText(emergency.getUser_report_tel());
        datetimeTextView.setText(emergency.getDate_time());

       return listViewItem;
    }
}
