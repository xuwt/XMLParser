package com.xuwt.xmlparser.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.xuwt.xmlparser.Entry;
import com.xuwt.xmlparser.R;
import com.xuwt.xmlparser.StackOverflowXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends Activity {

    //url
    private static final String URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView= (WebView) this.findViewById(R.id.webView);


        new DownLoadXmlTask().execute(URL);
    }

    public class DownLoadXmlTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String result;
            try{
                result=loadXmlFromNetWork(params[0]);
            }catch (IOException e){
                result=getResources().getString(R.string.connection_error);


            }catch (XmlPullParserException e){
                result=getResources().getString(R.string.xml_error);
            }
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {

            //mWebView.loadData(s,"text/html","utf-8");
            mWebView.loadDataWithBaseURL(null,s,"text/html","utf-8",null);
        }
    }

    private String loadXmlFromNetWork(String urlString) throws IOException, XmlPullParserException {
        InputStream inputStream=null;
        StackOverflowXmlParser parser=new StackOverflowXmlParser();

        List<Entry> entries=null;
        String title=null;
        String url=null;
        String summary=null;

        Calendar rightNow=Calendar.getInstance();
        DateFormat formatter=new SimpleDateFormat("MMM dd H:mmaa");

        StringBuilder htmlString=new StringBuilder();
        htmlString.append("<h3>"+getResources().getString(R.string.page_tille)+"</h3>");
        htmlString.append("<em>"+getResources().getString(R.string.updated)+" "+
                formatter.format(rightNow.getTime())+"</em>");

        try {
            inputStream=downloadUrl(urlString);
            entries=parser.parse(inputStream);
        } finally {
            if(inputStream!=null){
                inputStream.close();
            }
        }



        for(Entry entry:entries){
            htmlString.append("<p><a href='");
            htmlString.append(entry.link);
            htmlString.append("'>"+entry.title+"</a></p>");

            htmlString.append(entry.summary);

        }

        return  htmlString.toString();


    }

    /**
     * 根据url获取输入流
     * @param urlString
     * @return
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url=new java.net.URL(urlString);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000);//milliseconds
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        connection.connect();
        InputStream stream=connection.getInputStream();
        return stream;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
