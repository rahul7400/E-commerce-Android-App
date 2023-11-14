package in.macrocodes.creatives;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class CustomDatabase {
   public FirebaseFirestore getSettings(){
       FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
               .setPersistenceEnabled(true)
               .build();
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       db.setFirestoreSettings(settings);
       return db;
   }
}
