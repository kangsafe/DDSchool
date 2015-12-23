package com.ddschool.ui;

import android.content.Context;
import android.os.Build;
import com.ddschool.ui.TipsToast;

public class UICommon {
	private static TipsToast sTipsToast;

	public static void showTips(Context context, int iconResID, String tips) {
		if (context == null) {
			return;
		}

		if (tips == null) {
			tips = "";
		}

		if (sTipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				sTipsToast.cancel();
			}
		} else {
		
			sTipsToast = TipsToast.makeText(context, tips,
					TipsToast.LENGTH_SHORT);
		}

		sTipsToast.setIcon(iconResID);
		sTipsToast.setText(tips);
		sTipsToast.show();
	}
}