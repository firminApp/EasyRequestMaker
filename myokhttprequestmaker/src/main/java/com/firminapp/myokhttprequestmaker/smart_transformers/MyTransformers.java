package com.firminapp.myokhttprequestmaker.smart_transformers;

import android.util.Log;

import com.firminapp.myokhttprequestmaker.PostDataModel.PostListe;

import org.json.JSONObject;

/**
 * Created by firmin on 08/06/18.
 */

public final class MyTransformers {
    private static String TAG=MyTransformers.class.getSimpleName();
    /**
     * transform a jsonobject to a list of post data to send
     * as form params in an http request
     * (::) trop fort en anglais n'est-ce pas!!!?
     *
     * the key of each value of the jsonobject is used as key of a post entry in
     * the request
     * @param object
     * @return
     */
    public static PostListe jsonToPostlist(JSONObject object)
    {
        PostListe postliste=new PostListe();
        if (object!=null)
        {


        }else
        {
            Log.e(TAG," a null json object vcan not be transform to postList ");
        }
return null;
    }

}
