package com.liulishuo.filedownloader.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Jacksgong on 9/23/15.
 * <p/>
 * ui进程与:downloader进程 相互通信对象
 */
public class FileDownloadTransferModel implements Parcelable {

    private int status;
    private int downloadId;
    private int soFarBytes;

    // ----  只有在连接上的时候带回
    // 总大小
    private int totalBytes;
    // 是否是断点续传
    private boolean isContinue;
    // ETag
    private String etag;
    // ----

    // ---- 只在错误的时候带回
    // 错误
    private Throwable throwable;

    public FileDownloadTransferModel(final FileDownloadModel model) {
        this.status = model.getStatus();
        this.downloadId = model.getId();
        this.soFarBytes = model.getSoFar();
        this.totalBytes = model.getTotal();
        this.etag = model.getETag();
    }

    public boolean isContinue() {
        return isContinue;
    }

    public void setIsContinue(boolean isContinue) {
        this.isContinue = isContinue;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public int getSoFarBytes() {
        return soFarBytes;
    }

    public void setSoFarBytes(int soFarBytes) {
        this.soFarBytes = soFarBytes;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public FileDownloadTransferModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @param dest
     * @param flags
     * @see com.liulishuo.filedownloader.FileDownloadTask.FileDownloadInternalLis
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeInt(this.downloadId);

        // 为了频繁拷贝的时候不带上
        if (this.status != FileDownloadStatus.completed) {
            dest.writeInt(this.soFarBytes);
        }

        if (this.status == FileDownloadStatus.error) {
            dest.writeSerializable(this.throwable);
        }

        if (this.status == FileDownloadStatus.pending) {
            dest.writeInt(this.totalBytes);
        }

        if (this.status == FileDownloadStatus.connected) {
            dest.writeInt(this.totalBytes);
            dest.writeString(this.etag);
            dest.writeByte(isContinue ? (byte) 1 : (byte) 0);
        }
    }

    /**
     * @param in
     * @see com.liulishuo.filedownloader.FileDownloadTask.FileDownloadInternalLis
     */
    protected FileDownloadTransferModel(Parcel in) {
        this.status = in.readInt();
        this.downloadId = in.readInt();

        // 为了频繁拷贝的时候不带上
        if (this.status != FileDownloadStatus.completed) {
            this.soFarBytes = in.readInt();
        }

        if (this.status == FileDownloadStatus.error) {
            this.throwable = (Throwable) in.readSerializable();
        }

        if (this.status == FileDownloadStatus.pending) {
            this.totalBytes = in.readInt();
        }

        if (this.status == FileDownloadStatus.connected) {
            this.totalBytes = in.readInt();
            this.etag = in.readString();
            this.isContinue = in.readByte() != 0;
        }
    }

    public static final Creator<FileDownloadTransferModel> CREATOR = new Creator<FileDownloadTransferModel>() {
        public FileDownloadTransferModel createFromParcel(Parcel source) {
            return new FileDownloadTransferModel(source);
        }

        public FileDownloadTransferModel[] newArray(int size) {
            return new FileDownloadTransferModel[size];
        }
    };
}
