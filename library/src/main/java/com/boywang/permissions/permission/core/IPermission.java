package com.boywang.permissions.permission.core;

public interface IPermission {

    void granted();

    void cancel();

    void denied();

}
