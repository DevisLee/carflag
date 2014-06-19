package blank.lee.actb_car.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import blank.lee.actb_car.MainActivity;
import blank.lee.actb_car.R;
import blank.lee.actb_car.entity.CarFlag;

//listview列表的数据适配
public class ListViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<CarFlag> mDataList;
	private LayoutInflater inflater = null;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	public ListViewAdapter(Context context, List<CarFlag> dataList) {
		super();
		this.mContext = context;
		this.mDataList = dataList;
		inflater = LayoutInflater.from(mContext);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.bg_white)
				.showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		return mDataList != null ? mDataList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void refreshAdapter(List<CarFlag> dataList) {
		mDataList = dataList;
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}

	static class ViewHolder {
		ImageView iconImageView;
		TextView nameTextView;
		TextView enameTextView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item, null);
			convertView.setLayoutParams(new ListView.LayoutParams(MainActivity.SCREEN_WIDTH,
					MainActivity.SCREEN_WIDTH * 1 / 5));
			viewHolder = new ViewHolder();

			viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.listView_item_imageView);
			viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.listView_item_textView_name);
			viewHolder.enameTextView = (TextView) convertView.findViewById(R.id.listView_item_textView_ename);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage("assets://" + mDataList.get(position).getImage() + ".png", viewHolder.iconImageView,
				options);
		viewHolder.nameTextView.setText(mDataList.get(position).getName());
		viewHolder.enameTextView.setText(mDataList.get(position).getEname() + "  " + mDataList.get(position).getCountry());

		return convertView;
	}

}