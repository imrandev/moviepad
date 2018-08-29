package com.nerdgeeks.moviepad.utils.permission;

public interface PermissionService {
    void checkLocationPermission();
    void checkGpsEnabled();
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    void reportPermissionsError();
    void reportGpsError();
    void resolveGpsError();
    void resolvePermissionsError();
}
