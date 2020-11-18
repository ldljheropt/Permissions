package com.boywang.permissions.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.boywang.permissions.R;
import com.boywang.permissions.permission.core.IPermission;
import com.boywang.permissions.permission.util.PermissionUtils;


public class PermissionActivity extends Activity {

    private final static String PARAM_PREMISSION = "param_permission";
    private final static String PARAM_REQUEST_CODE = "param_request_code";
    public final static int PARAM_REQUEST_CODE_DEFAULT = -1;

    private String[] permissions;
    private int requestCode;
    private static IPermission permissionListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_permission);

        permissions = getIntent().getStringArrayExtra(PARAM_PREMISSION);
        requestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, PARAM_REQUEST_CODE_DEFAULT);

        if (permissions == null && requestCode < 0 && permissionListener == null) {
            finish();
            return;
        }

        boolean permissionResult = PermissionUtils.hasPermissionRequest(this, permissions);
        if (permissionResult) {
            permissionListener.granted();
            finish();
            return;
        }

        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionUtils.requestPermissionSuccess(grantResults)) {
            permissionListener.granted();
            finish();
            return;
        }

        if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {

            permissionListener.denied();
            finish();
            return;
        }

        permissionListener.cancel();
        finish();
        return;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public static void requestPermissionAction(Context context, String[] permissions, int requestCode, IPermission iPermission) {
        PermissionActivity.permissionListener = iPermission;

        Intent intent = new Intent(context, PermissionActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_REQUEST_CODE, requestCode);
        bundle.putStringArray(PARAM_PREMISSION, permissions);

        intent.putExtras(bundle);
        context.startActivity(intent);

    }

}
