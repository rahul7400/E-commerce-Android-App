package in.macrocodes.creatives;

import android.widget.Toast;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;

public class MongoClientJava {
    MongoClient mongoClient;
    String appId = "affiliate-imvsn";

    public MongoClient getMongoClient(){
        App app = new App(new AppConfiguration.Builder(appId).build());
        User user = app.currentUser();

        if (user==null){
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, new App.Callback<User>() {
                @Override
                public void onResult(App.Result<User> result) {
                    if (result.isSuccess()){
                        assert user != null;
                        mongoClient  = user.getMongoClient("mongodb-atlas");
                    }
                }
            });
        }else{
            mongoClient  = user.getMongoClient("mongodb-atlas");
        }

        return mongoClient;
    }
}
