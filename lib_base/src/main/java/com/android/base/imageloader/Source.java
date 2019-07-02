package com.android.base.imageloader;

import android.net.Uri;

import java.io.File;


public class Source {

    String mUrl;
    File mFile;
    int mResource;
    Uri mUri;

    public static Source create(String url) {
        Source source = new Source();
        source.mUrl = url;
        return source;
    }

    public static Source createWithPath(String path) {
        return create(new File(path));
    }

    public static Source createWithUri(Uri uri) {
        Source source = new Source();
        source.mUri = uri;
        return source;
    }

    public static Source create(File file) {
        Source source = new Source();
        source.mFile = file;
        return source;
    }

    public static Source create(int resource) {
        Source source = new Source();
        source.mResource = resource;
        return source;
    }

}
