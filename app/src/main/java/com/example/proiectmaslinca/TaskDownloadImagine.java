package com.example.proiectmaslinca;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

// String este tipul parametrului dat metodei doInBackground
// Void inseamna ca AsyncTask nu foloseste onProgressUpdates
// Boolean este tipul returnat de doInBackground si rezultatul obtinut se transmite la onPostExecute

public class TaskDownloadImagine extends AsyncTask<String, Void, Boolean>{

    private Context context;

    public TaskDownloadImagine(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String imageUrl = params[0];
        String imageName = params[1];

        try {
            if (context != null) {
                // Create a DownloadManager request
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));

                // Set the destination directory and file name
                File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "YourAppName");
                if (!destinationFile.exists()) {
                    destinationFile.mkdirs();
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "YourAppName" + File.separator + imageName + ".jpg");

                // Get the DownloadManager service
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                // Enqueue the download request
                long downloadId = downloadManager.enqueue(request);

                // For simplicity, we're not checking the download status in this example
                // You may want to implement a BroadcastReceiver to listen for download completion

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Eroare descarcare");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if (success) {
            Toast.makeText(context, "Descarcare reusita", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Descarcare esuata", Toast.LENGTH_SHORT).show();
        }
    }
}
