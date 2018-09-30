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

/**
 * cette classe est sensée me reduire le travaille a faire pour envoyer une requet
 * http vers le sserveur  pour recuperer les données sous forme d'un objet
 * json ou sous forme d'un liste jsonArray
 * Created by firmin on 02/02/18.
 *
 * L'utilisation de cette classe est tout simple.
 * créer une intsance de la classe et appeler la methodes aproprier dont
 * vous avez besoin pour votre requet
 * exemple:
 *
 *
 *   EasyRequestMaker requestMaker=new EasyRequestMaker();
 requestMaker.setUrl(WebSercives.URL_SLIDER)
 .setMethod(MyMethods.POST)
 .setHeader(header)
 .setToken()
 .setPost(post)
 .onFailled(new OnFalledListener() {
@Override
public void onFailled() {
Log.e("request", "la requet n'a pas pu aboutire");
}
})
 .onJsonArrayReceved(new OnJsonArrayResponseListener() {
@Override
public void onJonArrayResponse(JSONArray arry) {
Log.e("array",arry.toString());
}

@Override
public void onUnsuccefull(String response, String code) {
Log.e("respon","response pas bonne :code= "+code+" response="+response);


}
});
 */

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

    /**
     * boolean permettant de savoir s'il faut faire le caching de la reponse
     * de la requet en cours ou pas. par defaut il est false. ie que par defaut on ne cache pas
     * les réponses. c'est la methode onCache qui permet de modifier ce statut et
     * faire le cache
     */
    private boolean cacheResponse=true;
    /**
     * boolean permettant de verifier l'etat du serveur actuellement;
     * par defaut le serveur est considerer comme up ie c'est-à-dire
     * disponible pour recevoir des requet
     * si fait appel a onCheckServerStatut je fait une vrai verification du statut du serveur
     * et je met a jour ce statut.
     */
    private boolean isServerUp=true;


    private String url;
    private JSONObject jsonObjectreturn;
    private JSONArray jsonArrayreturn;
    private OnJsonArrayResponseListener arraylistener;
    /**
     * par defaut les methode sont de type get
     */
    private MyMethods method=MyMethods.GET;



    /**
     * Les clef de chaque entrées du json constitue un paramettre de post
     * @param url
     * @param postparam
     */
    public EasyRequestMaker(String url, JSONObject postparam)
    {



    }


    /**
     * je met a jour le statut du serveur: up ou down..;
     * @param statut
     */
    private void setServerUp(boolean statut)
    {
        this.isServerUp=statut;
    }
    private boolean getServerStatut()
    {
        return  this.isServerUp;
    }

    public EasyRequestMaker()
    {
        super();
        sendedParamsForLog=new JSONObject();


   // client=new OkHttpClient();
    urlbuilder=new HttpUrl.Builder();
    requestBody=new FormBody.Builder();
    requestBuilder=new Request.Builder();
    reqbodyfile=   new MultipartBody.Builder();
   // this.retryOnConnectionFailure();
    }

    
    public EasyRequestMaker setContext(Context context){
        this.context=context;
        
        return  this;
    }
    //add files with this
    public EasyRequestMaker setDataPart(DataPart dataPart){
        reqbodyfile.addFormDataPart(dataPart.getKey(),dataPart.getValue());

        try {
            sendedParamsForLog.put("DataPart key "+dataPart.getKey()," value:"+dataPart.getValue() );
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return  this;
    }
    //add files with this
    public EasyRequestMaker setFile(DataPart dataPart,File file, MediaType mediatype){

        isFile=true;

        if (null!=file)
        {
            if (file.exists())
            {
                reqbodyfile.addFormDataPart(dataPart.getKey(), file.getName(), RequestBody.create(mediatype, file));
            }
            else
                Log.e(TAG, "Error choosed file...");
        }
        else
        {
            Log.e(TAG, "Error choosed file...");

        }
        try {
            sendedParamsForLog.put("DataPart key "+dataPart.getKey(),"this is a file " );
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  this;
    }

    public EasyRequestMaker setMethod(MyMethods method)
    {
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
        for (Get get:listgets)
        {
            setGet(get);
        }

        return  this;
    }

    public EasyRequestMaker setPostList(ArrayList<Post>listpost){
        for (Post post:listpost)
        {
            setPost(post);
        }

        return  this;
    }
    public EasyRequestMaker setFileList(ArrayList<Post>listpost, ArrayList<File>listefile){
        for (Post post:listpost)
        {
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

        for (Header header:headerListe)
        {
           setHeader(header);
        }
        //getRequestBuilder().addHeader(header.getKey(),header.getValue());

        return  this;
    }


    /**
     * retourne un jsonObject a traiter dans un callBack
     * @param objectlistebner
     * @return
     */
    public EasyRequestMaker onJsonObjectReceved(final OnJsonObjectResponseListener objectlistebner){
        this.getClientCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                objectlistebner.onUnsuccefull("echec","1110 "+e.getMessage());
               // Log.e("onJsonObject", "request failled "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    String body= null;
                    body = response.body().string();
                        if(response.isSuccessful()) {
                            if (body.startsWith("{")) {
                                try {

                                    //savedataFromLocal(urlbuilder.build().toString(),body);
                                   // AppDatabase.getDatabase(context).itemAndResponseString().addNew(tosave);
                                    objectlistebner.onJsonObjetResponse(new JSONObject(body));

                                  //  Log.e("JsonObjectreceved", body);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    objectlistebner.onUnsuccefull(body, response.code()+"");

                                }


                            } else {

                               // Log.e("probleme", body + " :n'est pas un jsonObject. a vérifier le format du json");
                                objectlistebner.onUnsuccefull(body, response.code()+"");

                            }
                        }
                        else
                        {
                            objectlistebner.onUnsuccefull(body,response.code()+"");
//                            Log.e("cachbody after",response.body().string());
                        }
                }
               // Log.e("probleme", "retour null");

            }
        });

        return   this;


    }

    /**
     * retourne un json array
     * @param marraylistener
     */
    public EasyRequestMaker onJsonArrayReceved(final OnJsonArrayResponseListener marraylistener){


        this.getClientCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               // Log.e("onjsonArray", "request failled");

                marraylistener.onUnsuccefull("Echec","1110 "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    String body= null;

//                    Log.e(TAG,local);

                    body = response.body().string();

                    //Log.e("response111",body);
                    if (response.isSuccessful()) {

                        if (body.startsWith("[")) {
                            try {
                                marraylistener.onJonArrayResponse(new JSONArray(body));

                            } catch (JSONException e) {
                                e.printStackTrace();
                              //  Log.e(TAG,"Exeption receved json: "+e.getMessage());

                            }
                        } else
                        {
                            marraylistener.onUnsuccefull(body, response.code()+"");

                       //     Log.e("probleme", body + " : is not a JSONArray");

                        }

                    }
                    else
                    {//la reponse ne retourne pas un code 2OO
                        //marraylistener.onUnsuccefull(body,response.code()+"");
                        //marraylistener.onUnsuccefull(body, response.code()+"");
                       marraylistener.onUnsuccefull(body.toString(),response.code()+"");
                    }
                }
                else
                {
                    marraylistener.onUnsuccefull("No response from server", response.code()+"");

                }

               // Log.e("probleme", "retour null");

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


        // return   jsonArrayreturn;
    }

    /**
     * A ne pas appeler...!
     * appeler cette fonction que seulement quand c'est totalllement
     * indispenssable.. sinon vous aurez une très mauvaise surprise!
     * @param onFailledListener
     * @return
     */
    public EasyRequestMaker onFailled(final OnFalledListener onFailledListener){

        this.getClientCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

               onFailledListener.onFailled();
                Log.e("responseonfail", "onfailled");


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              //  Log.e("responseonfail", response.body().string());
            }
        });
        // Log.e("jsonArray", jsonArrayreturn.toString());


        // return   jsonArrayreturn;
        return this;
    }

    /**
     * à l'appelle de cette methode assurez vous que vous avez deja bien initialiser
     * la requet avec au moins tous les pamettre neccessaires.
     * le retour de cette methode vous met a disposition un client okhttp tout pret a
     *  etre executer.
     *  vous pouver l'executer de facon synchrone ou asynchrone en appeleant la methode execut(à
     *   ou enqueue()
     *   ne me dites surtout pas que je suis gentile... je le sais  je suis tres gentil meme
     *   En cas de methode POST
     * faites attention de bien renseigner le requestbody
     * en faisant soit set post ou setPostListe
     * si non ca va planter gravvve...
     *  @author firmin
     * @return
     */
    private Call getClientCall(boolean isfile)
    {



            /**
             * faites attention de bien renseigner le requestbody
             * en faisant soit set post ou setPostListe
             * si non ca va planter gravvve...
             */


            request= new Request.Builder()
                    .url(urlbuilder.build().toString())
                    .post(reqbodyfile.build()
                    )

                    //  .cacheControl(CacheControl.FORCE_CACHE)
                    // .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();




        return   this.newBuilder()

                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build().newCall(request);

    }
    private Request.Builder getRequestBuilder()
    {

        // String token=SessionManager.getInstance().getTokenRefreshToken().get(SessionManager.KEY_TOKEN);
        // Log.e(TAG,"token= "+token);
        //Log.e(TAG,urlbuilder.build().toString());

        if (method.equals(MyMethods.GET))
        {
          //  Log.e(TAG,urlbuilder.build().toString());
            requestBuilder
                    .url(urlbuilder.build().toString())
                    .get();
                    //  .addHeader("access_token",token)

                    //.addHeader("content-type", "application/x-www-form-urlencoded")
                    //.build();

            //request.cacheControl();
        }
        else if (method.equals(MyMethods.PUT))
        {
            /**
             * faites attention de bien renseigner le requestbody
             * en faisant soit set post ou setPostListe
             * si non ca va planter gravvve...
             */

            /**
             * verifions si c'est un fichier que le gar veut envoyer plutot que de simple données
             */
            if(isFile)
            {
                Log.e(TAG, "sending a file...");
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .put(reqbodyfile.build());
                        // .addHeader("access_token",token)
                       // .build();
               // Log.e(TAG, "url send file: "+request.url()) ;
            }


            else
            {
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .put(requestBody.build())
                        // .addHeader("access_token",token)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                       ;
                // .build();
            }

        }
        else if (method.equals(MyMethods.POST))
        {
            /**
             * faites attention de bien renseigner le requestbody
             * en faisant soit set post ou setPostListe
             * si non ca va planter gravvve...
             */


            /**
             * verifions si c'est un fichier que le gar veut envoyer plutot que de simple données
             */
            if(isMediatype)
            {
                Log.e(TAG, "sending a mediaType...");
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .post(body);
                // .addHeader("access_token",token)
                // .build();
                // Log.e(TAG, "url send file: "+request.url()) ;
            }else
            if(isFile)
            {
                Log.e(TAG, "sending a file...");
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .post(reqbodyfile.build());
                        // .addHeader("access_token",token)
                       // .build();
              //  Log.e(TAG, "url send file: "+request.url()) ;
            }
            else
            {
                requestBuilder
                        .url(urlbuilder.build().toString())
                        .post(requestBody.build());
                        // .addHeader("access_token",token)
                        //.addHeader("content-type", "application/x-www-form-urlencoded")
                        //.build();
            }

        }
        else

            Log.e("probleme", "ypu must specify the http method GET, PUT, or POST");
        //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "OkhttpCach43");

        return requestBuilder;
    }
    private Call getClientCall()
    {
        if (null==requestBuilder)
        {
            Log.e(TAG, "requestBuilder is null, may be a problem occured when initilizing the request!");

            return null;
        }


        return   this.newBuilder()

                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build().newCall(getRequestBuilder().build());

    }

    /**
     * Permet de pinger le serveur pour s'assurer de sa
     * disponibilité avant de se mettre a faire nos foutues requets.
     * Vaut mieux de le ssavoir avant si non c'est du taf inutile!
     * Cette methode est surtout utile pour onCheckServerStatut()
     * appellable de l'extérieur.
     * @return
     */
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
    public OkHttpClient getClient()
    {
        return  client;
    }

    /**
     * si le gare est connecté on utilise le cache pour 30 seconde
     * S'il n'est pas connecté, le cache est disponible pendand un an!,
     * vivement qu'il ne désinstall pas l'appli pendant tout ce temp! (:^:)
     //* @param context context dans lequel le cache s'effectue.
     * utile pour pouvoir créer le fichier. Hummm justement parlant de la création de
     * fichier... je galère grave pour ca pour le moment. Mais pusque je suis très fort
     * je compte trouver la solution très vite (+++).
   //  * @param cachFileName Le nom du fichier dans lequel ecrire du cache.
     * @return
     */

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
    public void mediatype()
    {

    }
}
