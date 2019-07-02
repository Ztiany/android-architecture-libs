package com.android.base.permission;


public class PermissionUIProviderFactory {

    private static IPermissionUIProvider sIPermissionUIProvider;

    static IPermissionUIProvider getPermissionUIProvider() {
        if (sIPermissionUIProvider == null) {
            sIPermissionUIProvider = new DefaultPermissionUIProvider();
        }
        return sIPermissionUIProvider;
    }

    public static void registerPermissionUIProvider(IPermissionUIProvider iPermissionUIProvider) {
        sIPermissionUIProvider = iPermissionUIProvider;
    }

}
