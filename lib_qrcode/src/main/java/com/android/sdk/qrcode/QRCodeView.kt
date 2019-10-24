package com.android.sdk.qrcode

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.configuration.UpdateConfiguration
import io.fotoapparat.log.logcat
import io.fotoapparat.preview.Frame
import io.fotoapparat.selector.*
import io.fotoapparat.view.CameraView

abstract class QRCodeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ProcessDataTask.Delegate {

    private lateinit var cameraView: CameraView
    private lateinit var fotoapparat: Fotoapparat

    private lateinit var scanBoxView: ScanBoxView
    private var delegate: Delegate? = null

    private var dataTask: ProcessDataTask? = null
    protected var spotAble = false

    private val framingRect = Rect()
    private var framingRectInPreview: Rect? = null

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        cameraView = CameraView(getContext())
        scanBoxView = ScanBoxView(getContext())
        scanBoxView.initCustomAttrs(context, attrs)
        addView(cameraView)
        addView(scanBoxView)
        try {
            fotoapparat = createFotoapparat()
        } catch (e: Exception) {
            e.printStackTrace()
            delegate?.onScanQRCodeOpenCameraError(e)
        }
    }

    private fun createFotoapparat(): Fotoapparat {
        val configuration = CameraConfiguration(
                previewResolution = firstAvailable(
                        wideRatio(highestResolution()),
                        standardRatio(highestResolution())
                ),
                previewFpsRange = highestFps(),
                flashMode = off(),
                focusMode = firstAvailable(
                        continuousFocusPicture(),
                        autoFocus()
                ),
                frameProcessor = this::processFrame
        )

        return Fotoapparat(
                context = this@QRCodeView.context,
                view = cameraView,
                logger = logcat(),
                lensPosition = back(),
                cameraConfiguration = configuration,
                cameraErrorCallback = {
                    delegate?.onScanQRCodeOpenCameraError(it)
                }
        )
    }

    private fun processFrame(frame: Frame) {
        val processDataTask = dataTask
        if (spotAble && (processDataTask == null || processDataTask.isCancelled)) {

            dataTask = object : ProcessDataTask(frame.image, frame.size, frame.rotation, this) {
                override fun onPostExecute(result: String?) {

                    if (spotAble) {
                        if (!result.isNullOrEmpty()) {
                            try {
                                delegate?.onScanQRCodeSuccess(result)
                                stopSpot()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    cancelProcessDataTask()
                }
            }.perform()
        }
    }

    /**
     * 设置扫描二维码的代理
     *
     * @param delegate 扫描二维码的代理
     */
    fun setDelegate(delegate: Delegate) {
        this.delegate = delegate
    }

    /**
     * 显示扫描框
     */
    fun showScanRect() {
        scanBoxView.visibility = View.VISIBLE
    }

    /**
     * 隐藏扫描框
     */
    fun hiddenScanRect() {
        scanBoxView.visibility = View.GONE
    }

    /**
     * 打开后置摄像头开始预览，但是并未开始识别
     */
    fun startCamera() {
        try {
            fotoapparat.start()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

    }

    /**
     * 关闭摄像头预览，并且隐藏扫描框
     */
    fun stopCamera() {
        stopSpotAndHiddenRect()
        try {
            fotoapparat.stop()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    /**
     * 延100开始识别
     */
    fun startSpot() {
        postDelayed({
            spotAble = true
            startCamera()
        }, 100)
    }

    /**
     * 停止识别
     */
    fun stopSpot() {
        cancelProcessDataTask()
        spotAble = false
    }

    /**
     * 停止识别，并且隐藏扫描框
     */
    fun stopSpotAndHiddenRect() {
        stopSpot()
        hiddenScanRect()
    }

    /**
     * 显示扫描框，并且延迟1.5秒后开始识别
     */
    fun startSpotAndShowRect() {
        startSpot()
        showScanRect()
    }

    /**
     * 当前是否为条码扫描样式
     *
     * @return
     */
    val isScanBarcodeStyle: Boolean
        get() = scanBoxView.isBarcode

    /**
     * 打开闪光灯
     */
    fun openFlashlight() {
        try {
            fotoapparat.updateConfiguration(UpdateConfiguration(flashMode = firstAvailable(torch(), off())))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 关闭散光灯
     */
    fun closeFlashlight() {
        try {
            fotoapparat.updateConfiguration(UpdateConfiguration(flashMode = firstAvailable(off())))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 销毁二维码扫描控件
     */
    fun onDestroy() {
        delegate = null
    }

    /**
     * 取消数据处理任务
     */
    protected fun cancelProcessDataTask() {
        dataTask?.cancelTask()
        dataTask = null
    }

    /**
     * 切换成扫描条码样式
     */
    fun changeToScanBarcodeStyle() {
        scanBoxView.isBarcode = true
    }

    /**
     * 切换成扫描二维码样式
     */
    fun changeToScanQRCodeStyle() {
        scanBoxView.isBarcode = false
    }

    fun setDebug(debug: Boolean) {
        Debug.setDebug(debug)
    }

    protected fun getFramingRectInPreview(previewWidth: Int, previewHeight: Int): Rect? {
        if (!scanBoxView.getScanBoxAreaRect(framingRect)) {
            return null
        }
        if (framingRectInPreview == null) {
            val rect = Rect(framingRect)
            val cameraResolution = Point(previewWidth, previewHeight)
            val screenResolution = Utils.getScreenResolution(context)
            val x = cameraResolution.x * 1.0f / screenResolution.x
            val y = cameraResolution.y * 1.0f / screenResolution.y
            rect.left = (rect.left * x).toInt()
            rect.right = (rect.right * x).toInt()
            rect.top = (rect.top * y).toInt()
            rect.bottom = (rect.bottom * y).toInt()
            framingRectInPreview = rect
        }
        return framingRectInPreview
    }

    interface Delegate {

        /**
         * 处理扫描结果
         */
        fun onScanQRCodeSuccess(result: String)

        /**
         * 处理打开相机出错
         */
        fun onScanQRCodeOpenCameraError(error: Exception)

    }

}