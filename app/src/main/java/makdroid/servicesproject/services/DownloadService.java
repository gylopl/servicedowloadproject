package makdroid.servicesproject.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import makdroid.servicesproject.utils.Constants;
import makdroid.servicesproject.utils.FileUtil;

public class DownloadService extends Service {

    public DownloadService() {
    }

    public static final String TAG = "DownloadService";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private HandlerThread thread;
    private String urlToDownload;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStartCommand");
        urlToDownload = intent.getStringExtra(Constants.DOWNLOAD_URL).toString();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy");
        mServiceHandler.terminate();
        thread.interrupt();
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        private volatile boolean isRunning = true;

        public void terminate() {
            this.isRunning = false;
        }

        @Override
        public void handleMessage(Message msg) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File downloadedCacheFile = null;
            while (isRunning) {
                try {
                    URL url = new URL(urlToDownload);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(Constants.READ_TIMEOUT);
                    connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
                    connection.connect();

                    int fileLength = connection.getContentLength();

                    input = new BufferedInputStream(connection.getInputStream());
                    File fileDir = new File(FileUtil.getStringDiskCacheDir(getApplicationContext()));
                    String fileName = urlToDownload.substring(urlToDownload.lastIndexOf('/') + 1, urlToDownload.length());
                    downloadedCacheFile = new File(fileDir, fileName);
                    output = new FileOutputStream(downloadedCacheFile, false);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1 && isRunning) {
                        total += count;
                        output.write(data, 0, count);
                        Intent localIntent =
                                new Intent(TAG).putExtra(Constants.UPDATE_PROGRESS, (int) ((total * 100 / fileLength)));
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
                    }
                    if (isRunning)
                        FileUtil.copy(downloadedCacheFile, new File(FileUtil.getAppDir(getApplicationContext()), fileName));
                } catch (SocketTimeoutException e) {
                    Intent localIntent =
                            new Intent(TAG).putExtra(Constants.CONNECTION_ERROR, true);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
                } catch (InterruptedIOException ignored) {
                    Log.i(TAG, "Interrupted, closing");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    if (downloadedCacheFile != null)
                        downloadedCacheFile.delete();//usuwam plik cache
                    try {
                        if (output != null) {
                            Log.i(TAG, "output.close();");
                            output.close();
                        }
                        if (input != null) {
                            Log.i(TAG, "input.close();");
                            input.close();
                        }
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }
                isRunning = false;
            }
//            if (thread.isInterrupted()) {
//                // cleanup and stop execution
//            }
            stopSelf(msg.arg1);
        }
    }

}