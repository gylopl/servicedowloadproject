package makdroid.servicesproject.services.intentService;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import makdroid.servicesproject.utils.Constants;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DownloadIntentService extends IntentService {

    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    // --Commented out by Inspection (05.06.2016 18:26):public static final String TAG = "DownloadIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra(Constants.DOWNLOAD_URL).toString();
        ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER);
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        File downloadedCacheFile = null;


        for (int i = 0; i <= 100; i++) {
            try {
                Bundle resultData = new Bundle();
                resultData.putInt(Constants.UPDATE_PROGRESS, i);
                receiver.send(Constants.UPDATE_PROGRESS_CODE, resultData);
                Thread.sleep(200);
            } catch (Exception e) {
            }

        }

//        try {
//            URL url = new URL(urlToDownload);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setReadTimeout(Constants.READ_TIMEOUT);
//            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
//            connection.connect();
//            int fileLength = connection.getContentLength();
//
//            input = new BufferedInputStream(connection.getInputStream());
//            File fileDir = new File(FileUtil.getStringDiskCacheDir(getApplicationContext()));
//            String fileName = urlToDownload.substring(urlToDownload.lastIndexOf('/') + 1, urlToDownload.length());
//            downloadedCacheFile = new File(fileDir, fileName);
//            output = new FileOutputStream(downloadedCacheFile, false);
//
//            byte data[] = new byte[1024];
//            long total = 0;
//            int count;
//            while ((count = input.read(data)) != -1) {
//                total += count;
//                Bundle resultData = new Bundle();
//                resultData.putInt(Constants.UPDATE_PROGRESS, (int) (total * 100 / fileLength));
//                receiver.send(Constants.UPDATE_PROGRESS_CODE, resultData);
//                output.write(data, 0, count);
//            }
//            FileUtil.copy(downloadedCacheFile, new File(FileUtil.getAppDir(getApplicationContext()), fileName));
//        } catch (SocketTimeoutException e) {
//
//        } catch (InterruptedIOException ignored) {
//            Log.i(TAG, "Interrupted, closing");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (downloadedCacheFile != null)
//                downloadedCacheFile.delete();//usuwam plik cache
//            try {
//                if (output != null) {
//                    Log.i(TAG, "output.close();");
//                    output.close();
//                }
//                if (input != null) {
//                    Log.i(TAG, "input.close();");
//                    input.close();
//                }
//            } catch (IOException e) {
//            }
//
//            if (connection != null)
//                connection.disconnect();
//        }

        Bundle resultData = new Bundle();
        resultData.putInt(Constants.UPDATE_PROGRESS, 100);
        receiver.send(Constants.UPDATE_PROGRESS_CODE, resultData);
    }


}