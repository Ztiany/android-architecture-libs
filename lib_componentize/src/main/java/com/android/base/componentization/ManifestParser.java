package com.android.base.componentization;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

final class ManifestParser {

    private static final String MODULE_VALUE = "ComponentConfig";

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    public List<ComponentConfig> parse() {
        List<ComponentConfig> modules = new ArrayList<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (MODULE_VALUE.equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse ConfigModule", e);
        }

        return modules;
    }

    private static ComponentConfig parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find ComponentConfig implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate ComponentConfig implementation for " + clazz, e);
        }

        if (!(module instanceof ComponentConfig)) {
            throw new RuntimeException("Expected instanceof ComponentConfig, but found: " + module);
        }

        return (ComponentConfig) module;
    }

}