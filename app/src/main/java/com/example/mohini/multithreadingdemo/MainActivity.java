package com.example.mohini.multithreadingdemo;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText etUrlText;
    private Button downloadButton;
    private LinearLayout loadingMessageSection = null;
    private ListView urlList;
    private ProgressBar progressBar;
    private String[] listOfImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the context of all views from layout
        etUrlText = (EditText) findViewById(R.id.etUrlText);
        downloadButton = (Button) findViewById(R.id.downloadButton);
        loadingMessageSection = (LinearLayout) findViewById(R.id.loadingMessageSection);
        urlList = (ListView) findViewById(R.id.urlList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        urlList.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageURLs);

    }

    public void downLoadImageButtonPressed(View v){

//        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
////        MessagePrinting.displayToast(this, file.getAbsolutePath());
//        String url = listOfImages[0];
//        Uri uriOfURL = Uri.parse(url);
//        MessagePrinting.displayToast(this, uriOfURL.getLastPathSegment());

        if(etUrlText.getText().toString().length() > 0) {
            Thread myThread = new Thread(new MyThread(etUrlText.getText().toString()));
            myThread.start();
        }
    }

    public boolean downloadImageUsingThread(String urlString){

        boolean suucees = false;
        HttpURLConnection connection = null;
        URL url = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        File file ;

        try {
            url = new URL(urlString);

            // open connection
            connection = (HttpURLConnection)url.openConnection();
            inputStream = connection.getInputStream();

            int read = -1;
            byte buffer[] = new byte[1024]; // will read these many bites in one go


            // create file to save image

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/multithreading_" + Math.random() + ".jpg" );

            boolean f = file.createNewFile();
                MessagePrinting.logMessage("------------PATH---------------" +  file.getAbsolutePath());
            outputStream = new FileOutputStream(file);


            // reading bytes
            while((read = inputStream.read(buffer)) != -1){

                MessagePrinting.logMessage("read = " + read);

                outputStream.write(buffer, 0, read);

            }

            suucees = true;
        } catch (IOException e){

            e.printStackTrace();

        }
        finally {

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingMessageSection.setVisibility(View.GONE);
                    etUrlText.setText("");
                }
            });
            if(connection != null){
                connection.disconnect();
            }

            if(inputStream != null){

                try {
                    inputStream.close();
                }
                catch (IOException e){

                }
            }

            if(outputStream != null){

                try{
                    outputStream.close();
                }catch (IOException e){
                    // todo
                }
            }
        }

        return suucees;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        etUrlText.setText(listOfImages[position]);
    }


    private class MyThread implements Runnable{

        String urlToBeDownloaded;
        MyThread(String url){
            urlToBeDownloaded = url;
        }

        @Override
        public void run(){
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingMessageSection.setVisibility(View.VISIBLE);
                }
            });
            downloadImageUsingThread(urlToBeDownloaded);

        }
    }
}


