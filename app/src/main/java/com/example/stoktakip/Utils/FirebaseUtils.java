package com.example.stoktakip.Utils;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseUtils {


    /**
     *
     * @param auth
     * @param email
     * @return
     */
    public static void sendResetPasswordMail(FirebaseAuth auth, String email, final FragmentActivity activity){

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(activity, "Şifre sıfırlama linki email hesabınıza gönderildi .", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("resetPasswordMail", task.getException().toString());
                }

            }
        });
    }


}
