# 二维码扫描库

目前修改自   [BGAQRCode-Android](https://github.com/bingoogolapple/BGAQRCode-Android)

二维码框架：

- [zing](https://github.com/zxing/zxing)：纯 java 开发
- [ZBar](https://github.com/ZBar/ZBar)：使用了 so 库

开发流程：

- 在 Android 中打开 camera，实时的获取 camera 传递过来的图像字节码数据，然后传递给解码库进行解码。具体可以参考 **[BGAQRCode-Android](https://github.com/bingoogolapple/BGAQRCode-Android)** 项目。
