package com.mrtechno.crazytictacpo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.google.android.gms.ads.MobileAds;

public class SplashActivity extends Activity {

    long startTime;
    long endTime;
    double durantion;
    int count;
    Handler handler = new Handler(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        enableFullScreen();
        setContentView( R.layout.activity_splash );

        //INICIAR CONTADOR DE TEMPO
        startTime = System.nanoTime();

        //INICIALIZAR GOOGLE AD MOB
        MobileAds.initialize(this, "ca-app-pub-1749542030533741~9797749888");

        //TODO: Fazer animação com logo ou qquer coisa do tipo


        //TODO: Carregar informações básicas de usuário (preferências, pontuações, etc.)

        endTime = System.nanoTime();

        //FLUXO NATURAL DA SPLASH ACTIVITY
        final Intent startOverview = new Intent(SplashActivity.this, OverviewActivity.class);
        startOverview.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        durantion=(endTime-startTime)/1000000.0f;
        if(durantion>850)
        {
            SplashActivity.this.startActivity( startOverview );
            SplashActivity.this.finish();
        }
        else
        {
            //ESPERAR TEMPO DE VISUALIZAÇÃO CONFORTÁVEL
            handler.postDelayed(new Runnable() {
                public void run() {
                    //Iniciar nova activity
                    startOverview.putExtra( "ADS", false );
                    SplashActivity.this.startActivity( startOverview );
                    SplashActivity.this.finish();

                }
            }, 850-(int)durantion);
        }
    }

    //FUNÇÃO AUXILIAR E COMUM AO INÍCIO DE TODAS AS ACTIVITIES.
    public void enableFullScreen()
    {
        Window window = getWindow();
        window.addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        window.requestFeature( Window.FEATURE_NO_TITLE );
        if(Build.VERSION.SDK_INT>=19)
        {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }
    }
}
