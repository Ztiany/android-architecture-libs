# common ui

## 1 说明

由两部分组成：

1. 自己封装的一些 UI 工具。
2. GitHub 上一些不再更新的 UI 库。

GitHub 开源项目原始地址：

- [ViewPagerIndicator](https://github.com/JakeWharton/ViewPagerIndicator)
- [GestureLockView](https://github.com/sinawangnan7/GestureLockView)
- [SwitchButton](https://github.com/kyleduo/SwitchButton)
- [RcLayout](https://github.com/GcsSloop/rclayout)

## 2 使用方式

### 2.1 SwitchButton

```xml
    <!--通用 switch 样式 -->
<style name="StyleSwitchBtn">
    <item name="android:layout_width">wrap_content</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="kswThumbMargin">1dp</item>
    <item name="kswBackColor">@color/color_switch_bg</item>
    <item name="kswThumbColor">@color/color_switch_thumb</item>
</style>

<!--kswBackColor-->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="@color/white" android:state_enabled="false" />
    <item android:color="@color/white" android:state_enabled="true" />
</selector>

<!-- kswThumbColor -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="#E5E5E5" android:state_enabled="false" />
    <item android:color="#4589EE" android:state_checked="true" />
    <item android:color="#E9E9E9" android:state_checked="false" />
</selector>
```