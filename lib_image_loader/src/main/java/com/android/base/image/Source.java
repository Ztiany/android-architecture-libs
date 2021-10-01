package com.android.base.image;

import android.net.Uri;

import java.io.File;


public class Source {

    String mUrl;
    File mFile;
    int mResource;
    Uri mUri;
    byte[] mBytes;

    private Source() {
    }

    public static Source create(String url) {
        Source source = new Source();
        source.mUrl = url;
        return source;
    }

    public static Source createWithPath(String path) {
        return create(new File(path));
    }

    public static Source create(Uri uri) {
        Source source = new Source();
        source.mUri = uri;
        return source;
    }

    public static Source create(File file) {
        Source source = new Source();
        source.mFile = file;
        return source;
    }

    public static Source create(byte[] bytes) {
        Source source = new Source();
        source.mBytes = bytes;
        return source;
    }

    public static Source create(int resource) {
        Source source = new Source();
        source.mResource = resource;
        return source;
    }

}
