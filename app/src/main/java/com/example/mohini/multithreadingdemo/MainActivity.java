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

        Thread myThread = new Thread(new MyThread());
        myThread.start();
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
            connection = (HttpURLConnection)url.openConnection();
            inputStream = connection.getInputStream();

            int read = -1;
            byte buffer[] = new byte[1024]; // will read these many bites in one go


            // create file

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + Uri.parse(urlString).getLastPathSegment());
            MessagePrinting.displayToast(this,"" +  file.getAbsolutePath());

            // reading bytes
            while((read = inputStream.read(buffer)) != -1){

                MessagePrinting.logMessage("read = " + read);

            }
        }
        catch (MalformedURLException e){
        }
        catch (IOException e){

        }
        finally {
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
        }

        return suucees;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        etUrlText.setText(listOfImages[0]);
    }


    private class MyThread implements Runnable{

        @Override
        public void run(){

            downloadImageUsingThread(listOfImages[0]);

        }
    }
}


