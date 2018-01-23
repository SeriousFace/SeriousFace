package demo.seriousface.com.imagepicker;

import android.Manifest;
import android.app.Activity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 作者： xieyue
 * 日期： 17/11/10
 */

public class PermissionUtil {

    /**
     * 获取权限
     * @param activity Activity
     * @param requirePermission 权限，如Manifest.permission.WRITE_EXTERNAL_STORAGE
     * @param listener 监听方法
     */
    public static void requestEach(Activity activity, final String requirePermission, final OnPermissionListener listener) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEach(requirePermission).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if(listener != null) {
                    listener.callback(permission);
                }
            }
        });
    }

    public interface OnPermissionListener {
        void callback(Permission permission);
    }

    public interface Action1<T> extends Action {
        void call(T t);
    }
}
