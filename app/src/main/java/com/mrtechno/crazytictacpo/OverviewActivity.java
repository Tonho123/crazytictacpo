package com.mrtechno.crazytictacpo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



public class OverviewActivity extends Activity {

    //INICIAR OBJETO ADVIEW
    private AdView mAdView;
    private Button play;
    private Button more;
    private Button noads;
    private SeekBar dific;
    private TextView txtdific;
    private int dificult=0;


    //Intent mMoreIntent = new Intent(OverviewActivity.this, MoreActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //CARREGAR LAYOUT ATUAL
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        enableFullScreen();
        setContentView( R.layout.activity_overview );

        //SINCRONIZAR BOTOES COM LAYOUT
        play =(Button) findViewById( R.id.bNewJogo );
        more =(Button) findViewById( R.id.bMore );
        noads = (Button) findViewById( R.id.bNoAds );
        dific = (SeekBar) findViewById( R.id.seekBar );
        txtdific = (TextView) findViewById( R.id.txtDificultValue);
        txtdific.setText(String.valueOf( dificult ) );

        //LISTENER: NOVO JOGO
        play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mJogoIntent = new Intent(OverviewActivity.this, JogoActivity.class);
                mJogoIntent.putExtra( "Dificuldade", dificult );
                OverviewActivity.this.startActivity( mJogoIntent );
                OverviewActivity.this.finish();
            }
        } );

        dific.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                txtdific.setText( String.valueOf( progress ) );
                dificult=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        } );

        //TODO: iMPLEMENTAR VENDA DE REMOÇÃO DE ADS
        noads.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "No add click", Toast.LENGTH_SHORT ).show();
            }
        } );

        //TODO: iMPLEMENTAR MORE
        more.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "more click", Toast.LENGTH_SHORT ).show();
            }
        } );


        // ADMOB: BANNER (PARTE INFERIOR DA TELA)
        mAdView=findViewById( R.id.adView );
        AdRequest adRequest1 = new AdRequest.Builder()
                .addTestDevice( "88414C5BDCD167901E7F38E71FB126B3" )
                .build();
        mAdView.loadAd( adRequest1 );
    }



    private String toString(Boolean ads)
    {
        if(ads)
        {
            return "Verdadeiro";
        }
        else
        {
            return "Falso";
        }
    }

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
