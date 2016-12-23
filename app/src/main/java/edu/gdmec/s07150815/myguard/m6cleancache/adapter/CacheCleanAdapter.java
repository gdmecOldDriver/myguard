package edu.gdmec.s07150815.myguard.m6cleancache.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import android.text.format.Formatter;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m6cleancache.entity.CacheInfo;

/**
 * Created by student on 16/12/22.
 */

public class CacheCleanAdapter extends BaseAdapter{
    private Context context;
    private List<CacheInfo> cacheInfos;
    public CacheCleanAdapter(Context context,List<CacheInfo> cacheInfos){
        super();
        this.context=context;
        this.cacheInfos=cacheInfos;

    }
    @Override
    public int getCount() {
        return cacheInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return cacheInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.item_cacheclean_list,null);
            holder.mAppIconImgv= (ImageView) convertView.findViewById( R.id.imgv_appicon_cacheclean);
            holder.mCacheSizeTV= (TextView) convertView.findViewById(R.id.tv_appsize_cacheclean);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();

        }
        CacheInfo cacheInfo=cacheInfos.get(position);
        holder.mAppIconImgv.setImageDrawable(cacheInfo.appIcon);
        holder.mAppNameTV.setText(cacheInfo.appName);
        holder.mCacheSizeTV.setText(Formatter.formatFileSize(context,cacheInfo.cacheSize));
        return convertView;
    }
    static class ViewHolder{
        ImageView mAppIconImgv;
        TextView mAppNameTV;
        TextView mCacheSizeTV;
    }
}
