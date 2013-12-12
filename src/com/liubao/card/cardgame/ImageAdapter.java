package com.liubao.card.cardgame;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context context;

	private List<Integer> images;// 图片的数据源

	public void setImages(List images) {
		this.images = images;
	}

	public ImageAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		if (images == null) {
			return 0;
		}
		return images.size();
	}

	@Override
	public Object getItem(int position) {

		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	// 单个视图的展示形式 //position 位置 ，convertView 视图
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = null;

		// 视图是否已经生成
		if (convertView == null) {
			imageView = new ImageView(context);
			// 设计图片的高度和宽度
			imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			// 图片居中展示
			imageView.setScaleType(ImageView.ScaleType.CENTER);
			//
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}
		// 设置图片的数据源  
		imageView.setImageResource(images.get(position));

		return imageView;
	}

}
