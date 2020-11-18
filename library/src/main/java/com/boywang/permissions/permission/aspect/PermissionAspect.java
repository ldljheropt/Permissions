package com.boywang.permissions.permission.aspect;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.boywang.permissions.permission.PermissionActivity;
import com.boywang.permissions.permission.annotation.Permission;
import com.boywang.permissions.permission.annotation.PermissionCancel;
import com.boywang.permissions.permission.annotation.PermissionDenied;
import com.boywang.permissions.permission.core.IPermission;
import com.boywang.permissions.permission.util.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionAspect{



    @Pointcut("execution(@com.boywang.permissions.permission.annotation.Permission * *(..)) && @annotation(permission)")
    public void pointActionMethod(Permission permission){}


    @Around("pointActionMethod(permission)")
    public void aProceedingJoinPoint(final ProceedingJoinPoint point, final Permission permission) throws Throwable {


        Context context=null;

        final Object thisObject = point.getThis();

        if(thisObject == null){

            Log.d("Error","aProceedingJoinPoint：JDK环境有问题，导致AspectJ全部失效...");
        }

        if (thisObject instanceof Context) {
            context = (Context) thisObject;
        } else if (thisObject instanceof Fragment) {
            context = ((Fragment) thisObject).getActivity();
        }

        if (null == context || permission == null) {
            throw new IllegalAccessException("null == context || permission == null is null");
        }

        final Context finalContext = context;


        PermissionActivity.requestPermissionAction(context, permission.value(), permission.requestCode(), new IPermission() {
            @Override
            public void granted() {

                try {
                    point.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void cancel() {
                PermissionUtils.invokeAnnotation(thisObject, PermissionCancel.class);

            }

            @Override
            public void denied() {
                PermissionUtils.invokeAnnotation(thisObject, PermissionDenied.class);

                if(permission.isJumpSettings()){
                    PermissionUtils.startAndroidSettings(finalContext);
                }

            }
        });

    }


}
