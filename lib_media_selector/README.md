# 多媒体文件选择库

## 1 应用内多媒体文件选择

### 版本1：基于 boxing 封装

版本 1 基于 boxing 封装，由于内部存在一些 bug，后改为本地依赖并对 bug 进行了修复。

使用注意，配置好 FileProvider：

```xml
<external-files-path
    name="app_external"
    path="/"/>
```

原始 boxing 库存在以下问题：

1.  [RotatePhotoView](https://github.com/ChenSiLiang/RotatePhotoView) 依赖混乱导致的崩溃。
2.  [Android Q SQLiteException](https://github.com/bilibili/boxing/issues/154) 问题。
3. 没有适配 Android 10 的 ScopedStorage。

### 其他可选方案

-  [EasyPhotos](https://github.com/HuanTanSheng/EasyPhotos)
- [Matisse](https://github.com/zhihu/Matisse)
- [ImagePicker](https://github.com/jeasonlzy/ImagePicker)
- [PictureSelector](https://github.com/LuckSiege/PictureSelector)
- [Album](https://github.com/yanzhenjie/Album)

## 2 使用系统内置组件进行文件选择

SystemMediaSelector 用于调用系统相机或 SAF 获取图片或文件。

需要考虑的问题：

1. Android 7.0默认启动严苛模式对`file:`类Uri的限制。
2. 获取的图片方向问题，需要通过 exif 修正。
3. 系统返回的不是 file 路径，而是其他类型的uri，需要通过相关方法转换。

参考：

- [你需要知道的Android拍照适配方案](http://www.jianshu.com/p/f269bcda335f)
- [Android调用系统相机和相册-填坑篇](http://wuxiaolong.me/2016/05/24/Android-Photograph-Album2/)
- [Android大图裁剪](http://ryanhoo.github.io/blog/2014/06/03/the-ultimate-approach-to-crop-photos-on-android-2/)
- [get-filename-and-path-from-uri-from-mediastore](https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore)
- [how-to-get-the-full-file-path-from-uri](https://stackoverflow.com/questions/13209494/how-to-get-the-full-file-path-from-uri)

## 3 图片裁剪库

使用系统裁剪是发现不同设备厂商以及不同系统版本之间有这样那样的问题，于是决定内置图片裁剪库，可选裁剪库有：

- [uCrop](https://github.com/Yalantis/uCrop)
- [smartCropper](https://github.com/pqpo/SmartCropper)
- [simpleCropper](https://github.com/igreenwood/SimpleCropView)

目前采用的是 uCrop
