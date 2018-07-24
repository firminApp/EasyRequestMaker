# EasyRequestMaker
## introduction
this is my simple Okhttp request maker.
it's based on Okhtt library.

## Usage

in your build.gradle (project)

      allprojects {
      		repositories {
      			...
      			maven {
      			 url 'https://jitpack.io'
      			  }
      		}
      	}

and in your build.gradl(app)

       dependencies {
       	        implementation 'com.github.firminApp:EasyRequestMaker:v1.0.2'
       	}
below is a simple code of an exemple of usage of this library. it's just amazing!
  
  ###exemple
  
      EasyRequestMaker requestMaker=new new EasyRequestMaker();
            requestMaker.setUrl(WebSercives.URL_SLIDER)
                    .setMethod(MyMethods.POST)
                    .setHeader(header)
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
                    

just create an instance of the class MyOkHttpRequestMaker.

and then put all your params by calling the appropriate methode.

the three methods below are required 

      setContext(yourContext);
      setUrl(yourUrl);
      setMethod(MyMethod.[GET,POST,PUT];

according to the data you are waiting for the server,

you can call one of thes methods or both

        onJsonArrayReceved(new OnJsonArrayResponseListener() {
                          @Override
                          public void onJonArrayResponse(JSONArray arry) {
                              Log.e("array",arry.toString());
                              // process your Json data here
                          }
      
                          @Override
                          public void onUnsuccefull(String response, String code) {
                                  Log.e("respon","response pas bonne :code= "+code+" response="+response);
                                    // there is an error
      
                          }
                      });
                      //or
           .onJsonObjectReceved(new OnJsonObjectResponseListener() {
                              @Override
                              public void onJsonObjetResponse(JSONObject object) {
                                  
          
                               }
          
                              @Override
                              public void onUnsuccefull(String response, String code) {
                                      }
                          })
                         ;
 
 ## futures
 
  
  
@author Firmin

