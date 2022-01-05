package com.android.sdk.cache.encryption;

import androidx.annotation.Nullable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-20 16:51
 */
public interface Encipher {

    String encrypt(@Nullable String origin);

    String decrypt(@Nullable String encrypted);

}
