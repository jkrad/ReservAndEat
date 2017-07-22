package com.appspot.reservandeat_171704.reservandeat;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alberto on 13/07/2017.
 */

public class AdapterRes extends ArrayAdapter<DataRes> {
    Context context;
    int layoutResourceId;
    List<DataRes> datos = null;

    public AdapterRes(@NonNull Context context, @LayoutRes int resource, @NonNull List<DataRes> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.datos = objects;

    }


    static class DataHolder {
        ImageView ivRes;
        TextView tvRNombre;
        TextView tvTipoComida;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, null);

            holder = new DataHolder();
            holder.ivRes = (ImageView) convertView.findViewById(R.id.ivRes);
            holder.tvRNombre = (TextView) convertView.findViewById(R.id.tvResNombre);
            holder.tvTipoComida = (TextView) convertView.findViewById(R.id.tvTipoComida);

            convertView.setTag(holder);

        } else {
            holder = (DataHolder) convertView.getTag();
        }

        DataRes dataitem = datos.get(position);
        holder.tvRNombre.setText(dataitem.resNombre);
        holder.tvTipoComida.setText(dataitem.tipoComida);
        holder.ivRes.setImageResource(dataitem.resIdThumbnail);

        return convertView;
    }
}
