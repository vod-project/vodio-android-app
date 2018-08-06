package app.vodio.com.vodio.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.beans.Vod;

public class VodAdapter extends ArrayAdapter<Vod> implements View.OnClickListener {
    List<Vod> list;
    public VodAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public VodAdapter(Context context, int resource, List<Vod> items) {
        super(context, resource, items);
        list = items;
    }

    @Override
    public Vod getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.vod_item, null);
        }

        Vod p = getItem(position);

        if (p != null) {
            TextView authorLoginTv = (TextView) v.findViewById(R.id.authorTv);
            TextView titleTv       = (TextView) v.findViewById(R.id.titleTv);
            TextView timeTv        = (TextView) v.findViewById(R.id.timeTv);


            if (authorLoginTv != null) {
                authorLoginTv.setText(p.getAuthorLogin());
            }
            if(titleTv != null){
                titleTv.setText(p.getTitle());
            }
            if(timeTv != null){
                timeTv.setText(p.getTimeInSecond()+"");
            }
        }

        v.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),v.toString(),Toast.LENGTH_LONG).show();
    }
}