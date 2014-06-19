package blank.lee.actb_car.mytool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/******
 * @Description: Toast管理类
 ******/
public class MyToaster {

	private static Toast toast = null;

	private MyToaster() {
	}

	/******
	 * @MethodName: cancelToast
	 * @Description: 取消当前toast
	 ******/
	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
			toast = null;
		}
	}

	/******
	 * @MethodName: showToast
	 * @Description: 显示系统默认toast
	 ******/
	public static void showToast(Context context, String string) {
		cancelToast();
		toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);//
		toast.show();
	}

	/******
	 * @MethodName: showToast
	 * @Description: 显示系统默认toast long
	 ******/
	public static void showToast(Context context, String string, int time) {
		cancelToast();
		toast = Toast.makeText(context, string, Toast.LENGTH_LONG);
		toast.show();
	}
}
