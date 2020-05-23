# 多媒体文件选择库

基于 boxing 修改，因为 [Android Q SQLiteException](https://github.com/bilibili/boxing/issues/154) 问题没有被修复，暂时依赖源码。

## 1 AndroidN  在 FileProvider 的 xm l配置中加入：

```        
    <cache-path name="internal" path="boxing" />
    <external-path name="external" path="DCIM/bili/boxing" />
```

## 2 RotatePhotoView

[RotatePhotoView](https://github.com/ChenSiLiang/RotatePhotoView)

## 3 other options

-  [boxing](https://github.com/Bilibili/boxing)
-  [Matisse](https://github.com/zhihu/Matisse)
-  [ImagePicker](https://github.com/jeasonlzy/ImagePicker)
-  [PictureSelector](https://github.com/LuckSiege/PictureSelector)
-  [Album](https://github.com/yanzhenjie/Album)
-  [uCrop](https://github.com/Yalantis/uCrop)
-  [smartCropper](https://github.com/pqpo/SmartCropper)
-  [simpleCropper](https://github.com/igreenwood/SimpleCropView)

## 4 SystemMediaSelector

SystemMediaSelector 用于调用系统相机或相册获取图片。

需要考虑的问题：

1. Android 7.0默认启动严苛模式对`file:`类Uri的限制
2. 获取的图片方向问题，需要通过exif修正
3. 系统返回的不是file路径，而是其他类型的uri，需要通过相关方法转换。
4. 调用系统裁剪时，传的参数需要完整，比如比如魅族手机如果指定OutputX和OutputY为0，则只会裁减出一个像素

参考：

- [你需要知道的Android拍照适配方案](http://www.jianshu.com/p/f269bcda335f)
- [Android调用系统相机和相册-填坑篇](http://wuxiaolong.me/2016/05/24/Android-Photograph-Album2/)
- [Android大图裁剪](http://ryanhoo.github.io/blog/2014/06/03/the-ultimate-approach-to-crop-photos-on-android-2/)
- [get-filename-and-path-from-uri-from-mediastore](https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore)
- [how-to-get-the-full-file-path-from-uri](https://stackoverflow.com/questions/13209494/how-to-get-the-full-file-path-from-uri)
