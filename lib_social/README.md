# 说明

## 微信登录

微信登录需要在`包名.wxapi` 包中添加一个名为 `WXEntryActivity` 的 Activity，并继承该 module 提供的 `AbsWXEntryActivity`。

```java
/**
 * 微信分享、登录回调
 *
 * @author Ztiany
 */
@SuppressWarnings("all")
public class WXEntryActivity extends AbsWXEntryActivity {


}
```

manifest 配置参考

```xml
        <!--微信分享回调-->
        <activity
            android:name="应用包名.wxapi.WXEntryActivity"
            android:exported="true"
            android:screenOrientation="portrait"/>
```

## 微信支付

微信支付需要在`包名.wxapi` 包中添加一个名为 `WXPayEntryActivity` 的 Activity，并继承该 module 提供的 `AbsWeChatPayEntryActivity`。

```java
/**
 * 微信分享、登录回调
 *
 * @author Ztiany
 */
@SuppressWarnings("all")
public class WXPayEntryActivity extends AbsWeChatPayEntryActivity {

}
```

manifest 配置参考

```xml
        <!--微信分享回调-->
        <activity
            android:name="应用包名.wxapi.WXPayEntryActivity"
            android:exported="true"
            android:screenOrientation="portrait"/>
```

## 支付宝支付

- sdk 版本：alipaySdk-15.6.2-20190416165100-noUtdid