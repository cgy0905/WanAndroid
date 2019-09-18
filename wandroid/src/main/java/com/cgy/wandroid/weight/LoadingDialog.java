package com.cgy.wandroid.weight;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cgy.wandroid.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/** Created by Administrator on 2017/10/19. */
public class LoadingDialog {
  private static DialogLoading dialog;

  public static void show(Activity activity) {
    show(activity, null);
  }

  public static void show(Activity activity, CharSequence message) {
    show(activity, message, true);
  }

  public static void show(Activity activity, CharSequence msg, boolean isCancel) {
    if (activity == null || activity.isFinishing()) return;
    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
      if (dialog != null && dialog.isShowing()) {
        return;
      }
      dialog = new DialogLoading(activity, msg, isCancel);
      dialog.show();
    } else {
      Observable.create(
              e -> {
                if (dialog != null && dialog.isShowing()) {
                  return;
                }
                dialog = new DialogLoading(activity, msg, isCancel);
                dialog.show();
              })
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe();
    }
  }

  public static void dismiss() {
    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
      if (dialog != null && dialog.isShowing()) {
        dialog.dismiss();
      }
    } else {
      Observable.create(
              e -> {
                if (dialog != null && dialog.isShowing()) {
                  dialog.dismiss();
                }
              })
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe();
    }
  }

  public static void destroy() {
    dialog = null;
  }

  public static class DialogLoading extends Dialog {

    public DialogLoading(@NonNull Context context, CharSequence msg, boolean isCancel) {
      super(context);
      getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_loading, null);
      TextView tv_desc = view.findViewById(R.id.tv_desc);
      if (!TextUtils.isEmpty(msg)) {
        tv_desc.setText(msg);
        tv_desc.setVisibility(View.VISIBLE);
      } else {
        tv_desc.setVisibility(View.GONE);
      }
      setContentView(view);
      setCanceledOnTouchOutside(false);
      setCancelable(isCancel);
      if (!isCancel) {
        setOnKeyListener((dialog, keyCode, event) -> false);
      }
    }
  }
}
