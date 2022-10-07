package com.firminapp.easyRequestMaker;

import android.content.Context;
import android.util.Log;

import com.firminapp.easyRequestMaker.PostDataModel.DataPart;
import com.firminapp.easyRequestMaker.PostDataModel.Get;
import com.firminapp.easyRequestMaker.PostDataModel.Header;
import com.firminapp.easyRequestMaker.PostDataModel.Post;
import com.firminapp.easyRequestMaker.callbacks.OnFalledListener;
import com.firminapp.easyRequestMaker.callbacks.OnJsonArrayResponseListener;
import com.firminapp.easyRequestMaker.callbacks.OnJsonObjectResponseListener;
import com.firminapp.easyRequestMaker.callbacks.OnResponseListener;
import com.firminapp.easyRequestMaker.callbacks.OnserverStatutListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class EasyRequestMaker extends OkHttpClient  {


    private boolean isFile=false;
    private OkHttpClient client ;
    private Context context;
    private Request request;
    private FormBody.Builder requestBody;
    private RequestBody body;
    private MultipartBody.Builder reqbodyfile;
    private HttpUrl.Builder urlbuilder;
    private String TAG=EasyRequestMaker.class.getSimpleName();
    private JSONObject sendedParamsForLog;
    private Request.Builder requestBuilder;
    private boolean isMediatype=false;
    private boolean cacheResponse=true;
    private boolean isServerUp=true;
    private String url;
    private JSONObject jsonObjectreturn;
    private JSONArray jsonArrayreturn;
    private OnJsonArrayResponseListener arraylistener;
    private MyMethods method=MyMethods.GET;
    private void setServerUp(boolean statut)
    {
        this.isServerUp=statut;
    }
    private boolean getServerStatut()
    {
        return  this.isServerUp;
    }
    public EasyRequestMaker() {
        super();
        sendedParamsForLog=new JSONObject();
        urlbuilder=new HttpUrl.Builder();
        requestBody=new FormBody.Builder();
        requestBuilder=new Request.Builder();
        reqbodyfile=   new MultipartBody.Builder();
    }
    public EasyRequestMaker setContext(Context context){
        this.context=context;
        return  this;
    }
    public EasyRequestMaker setDataPart(DataPart dataPart){
        reqbodyfile.addFormDataPart(dataPart.getKey(),dataPart.getValue());
        try {
            sendedParamsForLog.put("DataPart key "+dataPart.getKey()," value:"+dataPart.getValue() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  this;
    }
    public EasyRequestMaker setFile(DataPart dataPart,File file, MediaType mediatype){
        isFile=true;
        if (null!=file) {
            if (file.exists()) {
                reqbodyfile.addFormDataPart(dataPart.getKey(), file.getName(), RequestBody.create(mediatype, file));
            } else
                Log.e(TAG, "Error choosed file...");
        }
        else {
            Log.e(TAG, "Error choosed file...");

        }
        try {
            sendedParamsForLog.put("DataPart key "+dataPart.getKey(),"this is a file " );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  this;
    }

    public EasyRequestMaker setMethod(MyMethods method) {
        this.method=method;
        return this;
    }
    public EasyRequestMaker setUrl(String urlo){
        this.urlbuilder=HttpUrl.parse(urlo).newBuilder();
        return  this;
    }
    public EasyRequestMaker setUrlNotEncoded(String urlo){
        this.urlbuilder=HttpUrl.parse(urlo).newBuilder();
        this.url=urlo;
        return  this;
    }

    public EasyRequestMaker setGet(Get get){
        this.urlbuilder.addQueryParameter(get.getKey(),get.getValue());
        try {
            sendedParamsForLog.put("GET key:  "+get.getKey(),get.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  this;
    }
    public EasyRequestMaker setPost(Post post){
        requestBody.add(post.getKey(),post.getValue());
        try {
            sendedParamsForLog.put("POST key:  "+post.getKey(),post.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  this;
    }

    public void setMediaTyp(String data,String typeMedia){
        try {
            sendedParamsForLog.put("Mediatype",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isMediatype=true;
        MediaType mediaType = MediaType.parse(typeMedia);
        body=RequestBody.create(mediaType,data);

    }

    public EasyRequestMaker setGetListe(ArrayList<Get>listgets){
        for (Get get:listgets) {
            setGet(get);
        }
        return  this;
    }

    public EasyRequestMaker setPostList(ArrayList<Post>listpost){
        for (Post post:listpost) {
            setPost(post);
        }
        return  this;
    }
    public EasyRequestMaker setFileList(ArrayList<Post>listpost, ArrayList<File>listefile){
        for (Post post:listpost) {
            setPost(post);
        }
        return  this;
    }

    public EasyRequestMaker setHeader(Header header){
        requestBuilder.addHeader(header.getKey(),header.getValue());
        try {
            sendedParamsForLog.put("HEADER key:  "+header.getKey(),header.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  this;
    }

    public EasyRequestMaker setHeaderList(ArrayList <Header> headerListe){
        for (Header header:headerListe) {
           setHeader(header);
        }
        return  this;
    }

    public EasyRequestMaker onJsonObjectReceved(final OnJsonObjectResponseListener objectlistebner){
        this.getClientCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                objectlistebner.onUnsuccefull("echec","1110 "+e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    String body= null;
                    body = response.body().string();
                        if(response.isSuccessful()) {
                            if (body.startsWith("{")) {
                                try {
                                    objectlistebner.onJsonObjetResponse(new JSONObject(body));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    objectlistebner.onUnsuccefull(body, response.code()+"");
                                }

                            } else {
                                objectlistebner.onUnsuccefull(body, response.code()+"");
                            }
                        } else {
                            objectlistebner.onUnsuccefull(body,response.code()+"");
                        }
                }
            }
        });
        return   this;
    }

    public EasyRequestMaker onJsonArrayReceved(final OnJsonArrayResponseListener marraylistener){
        this.getClientCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                marraylistener.onUnsuccefull("Echec","1110 "+e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    String body= null;
                    body = response.body().string();
                    if (response.isSuccessful()) {
                        if (body.startsWith("[")) {
                            try {
                                marraylistener.onJonArrayResponse(new JSONArray(body));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            marraylistener.onUnsuccefull(body, response.code()+"");
                        }
                    }
                    else {
                       marraylistener.onUnsuccefull(body.toString(),response.code()+"");
                    }
                }
                else {
                    marraylistener.onUnsuccefull("No response from server", response.code()+"");
                }
            }
        });
        return this;
    }

    public EasyRequestMaker onResponse(final OnResponseListener responseListener){
        this.getClientCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onjsonArray", "request failled");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    String body= null;
                    responseListener.onResponse(response);
                }
            }
        });
        return  this;
    }

    public EasyRequestMaker onFailled(final OnFalledListener onFailledListener){
        this.getClientCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               onFailledListener.onFailled();
                Log.e("responseonfail", "onfailled");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
        return this;
    }
    private Call getClientCall(boolean isfile) {
        request= new Request.Builder()
                    .url(urlbuilder.build().toString())
                    .post(reqbodyfile.build())
                    .build();
        return   this.newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build().newCall(request);
    }
    private Request.Builder getRequestBuilder() {
        if (method.equals(MyMethods.GET)) {
            requestBuilder
                    .url(urlbuilder.build().toString())
                    .get();
        } else if (method.equals(MyMethods.PUT)) {
            if(isFile) {
                Log.e(TAG, "sending a file...");
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .put(reqbodyfile.build());
            } else {
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .put(requestBody.build())
                        .addHeader("content-type", "application/x-www-form-urlencoded");
            }
        }
        else if (method.equals(MyMethods.POST)) {
            if(isMediatype) {
                Log.e(TAG, "sending a mediaType...");
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .post(body);
            }else if(isFile) {
                Log.e(TAG, "sending a file...");
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .post(reqbodyfile.build());
            } else {
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .post(requestBody.build());
            }
        } else
            Log.e("Error", "You must specify the http method GET, PUT, or POST");
        return requestBuilder;
    }
    private Call getClientCall() {
        if (null==requestBuilder) {
            Log.e(TAG, "requestBuilder is null, may be a problem occured when initilizing the request!");
            return null;
        }
        return   this.newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build().newCall(getRequestBuilder().build());
    }
    private Call getClientCallForserverStatut(String serverIp)
    {
        OkHttpClient client=new OkHttpClient();
        Request.Builder req=null;
        req= new Request.Builder()
                    .url(serverIp)
                    .get()
                    .addHeader("content-type", "application/x-www-form-urlencoded");
        return   client.newCall(req.build());
    }
    public OkHttpClient getClient() {
        return  client;
    }
    public EasyRequestMaker onCheckServerStatut(String serverIp, final OnserverStatutListener responseListener) {
       this.getClientCallForserverStatut(serverIp).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("server", "oncheck//... the server isDown");
                setServerUp(false);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseListener.onStatutResponse(response);
                }
        });
        return  this;
    }
    public JSONObject getSendedParamsForLog(){
        try {
            sendedParamsForLog.put("url",urlbuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.sendedParamsForLog;
    }
}
