package blank.lee.actb_car;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import blank.lee.actb_car.adapter.GridViewAdapter;
import blank.lee.actb_car.adapter.ListViewAdapter;
import blank.lee.actb_car.animation.Rotate3dAnimation;
import blank.lee.actb_car.db.AssetsDatabaseManager;
import blank.lee.actb_car.db.DBManager;
import blank.lee.actb_car.entity.CarFlag;
import blank.lee.actb_car.mytool.MyToaster;

import com.j.p.PopManager;
import com.jmp.sfc.uti.JMPManager;

public class MainActivity extends Activity implements OnItemClickListener, OnScrollListener, OnClickListener {
	// listview gridview的布局和适配器
	private ListView listView = null;
	private GridView gridView = null;
	private ListViewAdapter listViewAdapter = null;
	private GridViewAdapter gridViewAdapter = null;
	// 屏幕分辨率
	public static int SCREEN_HEIGHT, SCREEN_WIDTH;
	// 右上角切换视图的按钮
	private ImageView buttonImageView = null;
	// 左上角切换类别的按钮
	private Button buttonType = null;
	// 当前视图类型是否为gridview
	private boolean isGridView = true;
	// 数据集合
	private List<CarFlag> datas = null;
	// 数据库管理工具
	private DBManager dbManager = null;
	// 两次返回的间隔
	private long myTime;
	// 记录当前列表的顶部位置
	private int currentIndex = 0;
	private int onScrollIndex = 0;
	//
	private int currentTypeIndex = 0;
	private String[] typeStrings = { "全部", "国产", "欧美", "日韩", "其它", "热搜" };
	private AlertDialog alertDialog;

	// 插播广告
	PopManager popManager;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("TTT", "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏
		setContentView(R.layout.activity_main);
		// 计算屏幕分辨率比率
		WindowManager w = this.getWindowManager();
		SCREEN_HEIGHT = w.getDefaultDisplay().getHeight();
		SCREEN_WIDTH = w.getDefaultDisplay().getWidth();
		Log.e("TTTT", "SCREEN_HEIGHT = " + SCREEN_HEIGHT + "   SCREEN_WIDTH = " + SCREEN_WIDTH);
		// 数据库 初始化，只需要调用一次
		AssetsDatabaseManager.initManager(this);

		/*** 聚米广告 **/
		// 推送
		JMPManager jmpManager = new JMPManager();
		jmpManager.startService(this, 1);
		// 插播
		popManager = PopManager.getInstance(getApplicationContext(), "237425e8-1703-49b4-b49b-b434b35dd649", 1);
		// 配置插屏
		popManager.c(1, 4, true);
		// 配置外插屏
		popManager.o(false, false, 3, false, false, true);
		
		findViewById(R.id.title).setOnClickListener(this);

		// 初始化布局
		init();
		
		//默认第一次插播广告
		popManager.s(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.title) {
			popManager.s(this);
		}
	}

	// 初始化
	private void init() {
		listView = (ListView) findViewById(R.id.main_listView);
		gridView = (GridView) findViewById(R.id.main_gridView);
		// 切换布局按钮监听
		buttonImageView = (ImageView) findViewById(R.id.data_detail_imageview);
		buttonImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 震动反馈
				Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(55);
				// 判断当前视图 并切换
				if (isGridView) {
					buttonImageView.setImageResource(R.drawable.grid);
					isGridView = false;
					showListView();
				} else {
					buttonImageView.setImageResource(R.drawable.list);
					isGridView = true;
					showGridView();
				}
			}
		});
		buttonType = (Button) findViewById(R.id.data_detail_type);
		buttonType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 筛选类别弹出框
				alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("选择筛选类别")
						.setSingleChoiceItems(typeStrings, currentTypeIndex, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								if (currentTypeIndex != which) {
									MyToaster.showToast(MainActivity.this, "筛选 " + typeStrings[which]);
									currentTypeIndex = which;
									buttonType.setText(typeStrings[which]);
									switch (which) {
									case 0:// 全部
										datas = new ArrayList<CarFlag>();
										datas = dbManager.queryData();
										gridView.setSelection(0);
										listView.setSelection(0);
										gridViewAdapter.refreshAdapter(datas);
										listViewAdapter.refreshAdapter(datas);
										break;
									// 分类别
									case 1:// 国产
									case 2:// 欧美
									case 3:// 日韩
									case 4:// 其它
										datas = new ArrayList<CarFlag>();
										datas = dbManager.queryDataWithType(typeStrings[which]);
										gridView.setSelection(0);
										listView.setSelection(0);
										gridViewAdapter.refreshAdapter(datas);
										listViewAdapter.refreshAdapter(datas);
										break;
									// 热度
									case 5://
										datas = new ArrayList<CarFlag>();
										datas = dbManager.queryDataWithHot();
										gridView.setSelection(0);
										listView.setSelection(0);
										gridViewAdapter.refreshAdapter(datas);
										listViewAdapter.refreshAdapter(datas);
										break;
									default:
										break;
									}
								}
							}
						}).create();
				alertDialog.show();
			}
		});
		// 初始化数据集合
		datas = new ArrayList<CarFlag>();
		dbManager = new DBManager(this);
		// set data here
		datas = dbManager.queryData();
		// 初始化列表布局
		initGridView();
		initListView();
		// 默认显示gridview
		showGridView();
	}

	// 初始化gridview
	private void initGridView() {
		gridViewAdapter = new GridViewAdapter(this, datas);
		gridView.setAdapter(gridViewAdapter);
		gridView.setOnItemClickListener(this);
		gridView.setOnScrollListener(this);
	}

	// 初始化listview
	private void initListView() {
		listViewAdapter = new ListViewAdapter(this, datas);
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
	}

	// 显示gridview
	private void showGridView() {
		gridView.setLayoutAnimation(getgridlayoutAnim());
		Log.e("TTTT", "currentIndex  " + currentIndex);
		gridView.setSelection(currentIndex);
		gridViewAdapter.notifyDataSetInvalidated();
		gridView.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);

	}

	// 显示listview
	private void showListView() {
		listView.setLayoutAnimation(getgridlayoutAnim());
		Log.e("TTTT", "currentIndex  " + currentIndex);
		listView.setSelection(currentIndex);
		listViewAdapter.notifyDataSetInvalidated();
		gridView.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onDestroy() {
		Log.e("TTT", "onDestroy");
		// clear
		if (datas != null && datas.size() > 0) {
			datas.clear();
			if (listView != null && listViewAdapter != null) {
				listViewAdapter.notifyDataSetChanged();
				listView = null;
				listViewAdapter = null;
			}
			if (gridView != null && gridViewAdapter != null) {
				gridViewAdapter.notifyDataSetChanged();
				gridView = null;
				gridViewAdapter = null;
			}
			datas = null;
		}
		if (dbManager != null) {
			// dbManager.closeDB();//关闭以后 再次打开会无法读取db
			dbManager = null;
		}
		
		//关闭广告
		if(popManager!=null){
			popManager.q();
		}
		super.onDestroy();
	}

	// 点击两次返回退出程序
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if ((System.currentTimeMillis() - myTime) > 2000) {// 两次按下的时间间隔如果小于2秒就退出，大于2秒就再次提示
				MyToaster.showToast(MainActivity.this, "再按一次返回键退出程序");
				myTime = System.currentTimeMillis();// 获取当前系统时间
			} else {
				MyToaster.showToast(MainActivity.this, "客官慢走，欢迎再来哟~ ");
				finish();// 退出应用
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 切换视图的动画效果
	public static LayoutAnimationController getgridlayoutAnim() {
		LayoutAnimationController controller;
		Animation anim = new Rotate3dAnimation(90f, 0f, 0.5f, 0.5f, 0.5f, false);// 角度
		anim.setDuration(250);// 动画时间
		controller = new LayoutAnimationController(anim, 0.1f);
		controller.setOrder(LayoutAnimationController.ORDER_RANDOM);// 方式 随机
		return controller;
	}

	// 列表点击时间
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 显示车标详情介绍
		MyToaster.showToast(MainActivity.this, "        " + datas.get(position).getDescription(), 6000);
	}

	// 列表滚动时间
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			currentIndex = onScrollIndex;// 记录当前最顶部的记录位置
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		onScrollIndex = firstVisibleItem;
	}
}
