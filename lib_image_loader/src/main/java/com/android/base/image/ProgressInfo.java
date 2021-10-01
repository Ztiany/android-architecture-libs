package com.android.base.image;


import androidx.annotation.NonNull;

public class ProgressInfo {

    //当前下载的总长度
    private long currentBytes;
    //数据总长度
    private long contentLength;
    //本次调用距离上一次被调用所间隔的毫秒值
    private long intervalTime;
    //本次调用距离上一次被调用的间隔时间内下载的 byte 长度
    private long eachBytes;
    //请求的 ID
    private long id;
    //进度是否完成
    private boolean finish;

    ProgressInfo(long id) {
        this.id = id;
    }

    void setCurrentBytes(long currentBytes) {
        this.currentBytes = currentBytes;
    }

    void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    void setEachBytes(long eachBytes) {
        this.eachBytes = eachBytes;
    }

    void setFinish(boolean finish) {
        this.finish = finish;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public long getContentLength() {
        return contentLength;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public long getEachBytes() {
        return eachBytes;
    }

    public long getId() {
        return id;
    }

    public boolean isFinished() {
        return finish;
    }

    /**
     * 获取下载比例(0 - 1)
     */
    public float getProgress() {
        if (getCurrentBytes() <= 0 || getContentLength() <= 0) {
            return 0;
        }
        return ((1F * getCurrentBytes()) / getContentLength());
    }

    /**
     * 获取上传或下载网络速度，单位为byte/s
     */
    public long getSpeed() {
        if (getEachBytes() <= 0 || getIntervalTime() <= 0) {
            return 0;
        }
        return getEachBytes() * 1000 / getIntervalTime();
    }

    @NonNull
    @Override
    public String toString() {
        return "ProgressInfo{" +
                "id=" + id +
                ", currentBytes=" + currentBytes +
                ", contentLength=" + contentLength +
                ", eachBytes=" + eachBytes +
                ", intervalTime=" + intervalTime +
                ", finish=" + finish +
                '}';
    }

} 