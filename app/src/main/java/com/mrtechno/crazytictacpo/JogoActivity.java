package com.mrtechno.crazytictacpo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import static android.content.ContentValues.TAG;

public class JogoActivity extends AppCompatActivity implements View.OnClickListener, View.OnDragListener, View.OnTouchListener {

    //GOOGLE ADMOB
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    //LAYOUT
    private ImageView img11;
    private ImageView img12;
    private ImageView img13;
    private ImageView img21;
    private ImageView img22;
    private ImageView img23;
    private ImageView img31;
    private ImageView img32;
    private ImageView img33;
    private ImageView imgdrag1;
    private ImageView imgdrag2;
    private ImageView imgdrag3;
    private ImageView imgdrag4;
    private ImageView imgOpon1;
    private ImageView imgOpon2;
    private ImageView imgOpon3;
    private ImageView imgOpon4;
    private BoardClass board;
    private TextView tst;
    public int imgd;

    //VARIÁVEIS DE SISTEMA
    private int lastloc;
    private int atualoc;
    private SmartClass jogo;
    private CartaClass carta;
    private int turno;
    private int rodadas;
    private boolean RTL = false;

    //ELEMENTOS DE CÓDIGO
    private int dificuldade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //CARREGAR LAYOUT
        super.onCreate( savedInstanceState );
        enableFullScreen();
        setContentView( R.layout.activity_jogo );

        //FAZER CONEXÃO DOS ELEMENTOS DE LAYOUT
        imgdrag1 = findViewById( R.id.gragImg1 );
        imgdrag2 = findViewById( R.id.gragImg2 );
        imgdrag3 = findViewById( R.id.gragImg3 );
        imgdrag4 = findViewById( R.id.gragImg4 );
        imgdrag1.setTag( "Tesoura" );
        imgdrag2.setTag( "Pedra" );
        imgdrag3.setTag( "Papel" );
        imgdrag4.setTag( "Bomba" );
        imgOpon1 = findViewById( R.id.gragImg11 );
        imgOpon2 = findViewById( R.id.gragImg22 );
        imgOpon3 = findViewById( R.id.gragImg33 );
        imgOpon4 = findViewById( R.id.gragImg44 );
        img11 = (ImageView) findViewById( R.id.img11 );
        img12 = (ImageView) findViewById( R.id.img12 );
        img13 = (ImageView) findViewById( R.id.img13 );
        img21 = (ImageView) findViewById( R.id.img21 );
        img22 = (ImageView) findViewById( R.id.img22 );
        img23 = (ImageView) findViewById( R.id.img23 );
        img31 = (ImageView) findViewById( R.id.img31 );
        img32 = (ImageView) findViewById( R.id.img32 );
        img33 = (ImageView) findViewById( R.id.img33 );
        tst = findViewById( R.id.dificcc );
        board = findViewById( R.id.board );

        //RECUPERAR DIFICULDADE
        dificuldade = (int) getIntent().getIntExtra( "Dificuldade", 0 );

        //ADMOB: INTERSTICIAL
        mInterstitialAd = new InterstitialAd( this );
        mInterstitialAd.setAdUnitId( "ca-app-pub-3940256099942544/1033173712" );
        AdRequest adRequest2 = new AdRequest.Builder()
                .addTestDevice( "88414C5BDCD167901E7F38E71FB126B3" )
                .build();
        mInterstitialAd.loadAd( adRequest2 );

        // ADMOB: BANNER (PARTE INFERIOR DA TELA)
        mAdView = findViewById( R.id.adView );
        AdRequest adRequest1 = new AdRequest.Builder()
                .addTestDevice( "88414C5BDCD167901E7F38E71FB126B3" )
                .build();
        mAdView.loadAd( adRequest1 );

        //RETIRNANDO PARA OVERVIEW APÓS O ANÚNCIO
        mInterstitialAd.setAdListener( new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                gotoNext();
            }
        } );

        //TOUCHLISTEBERS: inicia-se aqui o processo de drag
        imgdrag1.setOnTouchListener( this );
        imgdrag2.setOnTouchListener( this );
        imgdrag3.setOnTouchListener( this );
        imgdrag4.setOnTouchListener( this );

        //DRAGLISTENER: o Tabuleiro
        board.setOnDragListener( this );
        //findViewById( R.id.contotno ).setOnDragListener( this );

        iniciarJogo();
    }

    private void iniciarJogo() {
        //TINSTANCIANDO A CLASSE SMART (Tabuleiro)
        jogo = new SmartClass();
        jogo.inicializar();

        //INSTANCIANDO A CLASSE CARTAS
        carta = new CartaClass();

        //NESSE PONTO O TABULEIRO ESTÁ ZERADO E CADA JOGADOR POSSUI DUAS CARTAS
        atualizacaoInicial();

        //USER INICIA O JOGO
        rodadas=1;
        turno = 0;
        carta.setTurno( turno );
    }

    private void atualizacaoInicial() {
        //FAZ A ATUALIZAÇÃO Inicial do tabuleiro

        img11.setVisibility( View.INVISIBLE );
        img12.setVisibility( View.INVISIBLE );
        img13.setVisibility( View.INVISIBLE );
        img21.setVisibility( View.INVISIBLE );
        img22.setVisibility( View.INVISIBLE );
        img23.setVisibility( View.INVISIBLE );
        img31.setVisibility( View.INVISIBLE );
        img32.setVisibility( View.INVISIBLE );
        img33.setVisibility( View.INVISIBLE );

        imgdrag1.setVisibility( View.INVISIBLE );
        imgdrag2.setVisibility( View.INVISIBLE );
        imgdrag3.setVisibility( View.INVISIBLE );
        imgdrag4.setVisibility( View.INVISIBLE );

        imgOpon1.setVisibility( View.INVISIBLE );
        imgOpon2.setVisibility( View.INVISIBLE );
        imgOpon3.setVisibility( View.INVISIBLE );
        imgOpon4.setVisibility( View.INVISIBLE );
        for (int j = 0; j < 3; j++) {
            switch (carta.baralho[0][j]) {
                case 0:
                    break;
                case 1:
                    setCards( imgdrag1, 1 );
                    break;
                case 2:
                    setCards( imgdrag2, 2 );
                    break;
                case 3:
                    setCards( imgdrag3, 3 );
                    break;
                default:
                    setCards( imgdrag4, carta.baralho[0][j] );
                    break;
            }
        }

        setCards( imgOpon1, 9 );
        setCards( imgOpon2, 9 );
        setCards( imgOpon3, carta.baralho[0][2] );
        setCards( imgOpon4, carta.baralho[0][3] );
    }

    private void setCards(ImageView v, int i) {
        switch (i) {
            case 1:
                v.setVisibility( View.VISIBLE );
                v.setImageResource( R.drawable.tesoura );
                break;
            case 2:
                v.setVisibility( View.VISIBLE );
                v.setImageResource( R.drawable.pedra );
                break;
            case 3:
                v.setVisibility( View.VISIBLE );
                v.setImageResource( R.drawable.papel );
                break;
            case 4:
                v.setVisibility( View.VISIBLE );
                v.setImageResource( R.drawable.pink );
                break;
            case 5:
                v.setVisibility( View.VISIBLE );
                v.setImageResource( R.drawable.black );
                break;
            case 6:
                v.setVisibility( View.VISIBLE );
                v.setImageResource( R.drawable.red );
                break;
            case 7:
                v.setVisibility( View.VISIBLE );
                v.setImageResource( R.drawable.green );
                break;
            case 8:
                v.setVisibility( View.VISIBLE );
                v.setImageResource( R.drawable.blue );
                break;
            case 9:
                v.setVisibility( View.VISIBLE );
                v.setImageResource( R.drawable.tictacponicon );
                break;
            default:
                v.setVisibility( View.INVISIBLE );
                break;
        }
    }

    private void RepandRes() {
        float aumento = board.getDelta() * 1.0f / img11.getHeight() * 1.0f;
        float Desloc = (board.getDelta() * 1.0f - img11.getHeight() * 1.0f) / 2;

        img11.getLayoutParams().height = (int) (img11.getHeight() * aumento);
        img11.getLayoutParams().width = (int) (img11.getWidth() * aumento);
        img11.setX( 0 + board.getXstart() + Desloc );
        img11.setY( 0 + board.getYstart() + Desloc );
        //img11.requestLayout();

        img12.getLayoutParams().height = (int) (img11.getHeight() * aumento);
        img12.getLayoutParams().width = (int) (img11.getWidth() * aumento);
        img12.setX( 0 + board.getXstart() + board.getDelta() + board.getE() + Desloc );
        img12.setY( 0 + board.getYstart() + Desloc );
        //img12.requestLayout();

        img13.getLayoutParams().height = (int) (img11.getHeight() * aumento);
        img13.getLayoutParams().width = (int) (img11.getWidth() * aumento);
        img13.setX( 0 + board.getXstart() + 2 * board.getDelta() + 2 * board.getE() + Desloc );
        img13.setY( 0 + board.getYstart() + Desloc );
        //img13.requestLayout();

        img21.getLayoutParams().height = (int) (img11.getHeight() * aumento);
        img21.getLayoutParams().width = (int) (img11.getWidth() * aumento);
        img21.setX( 0 + board.getXstart() + Desloc );
        img21.setY( 0 + board.getYstart() + board.getDelta() + board.getE() + Desloc );
        //img21.requestLayout();

        img22.getLayoutParams().height = (int) (img11.getHeight() * aumento);
        img22.getLayoutParams().width = (int) (img11.getWidth() * aumento);
        img22.setX( 0 + board.getXstart() + board.getDelta() + board.getE() + Desloc );
        img22.setY( 0 + board.getYstart() + board.getDelta() + board.getE() + Desloc );
        //img22.requestLayout();

        img23.getLayoutParams().height = (int) (img11.getHeight() * aumento);
        img23.getLayoutParams().width = (int) (img11.getWidth() * aumento);
        img23.setX( 0 + board.getXstart() + 2 * board.getDelta() + 2 * board.getE() + Desloc );
        img23.setY( 0 + board.getYstart() + board.getDelta() + board.getE() + Desloc );
        //img23.requestLayout();

        img31.getLayoutParams().height = (int) (img11.getHeight() * aumento);
        img31.getLayoutParams().width = (int) (img11.getWidth() * aumento);
        img31.setX( 0 + board.getXstart() + Desloc );
        img31.setY( 0 + board.getYstart() + 2 * board.getDelta() + 2 * board.getE() + Desloc );
        //img31.requestLayout();

        img32.getLayoutParams().height = (int) (img11.getHeight() * aumento);
        img32.getLayoutParams().width = (int) (img11.getWidth() * aumento);
        img32.setX( 0 + board.getXstart() + board.getDelta() + board.getE() + Desloc );
        img32.setY( 0 + board.getYstart() + 2 * board.getDelta() + 2 * board.getE() + Desloc );
        //img32.requestLayout();

        img33.getLayoutParams().height = (int) (img11.getHeight() * aumento);
        img33.getLayoutParams().width = (int) (img11.getWidth() * aumento);
        img33.setX( 0 + board.getXstart() + 2 * board.getDelta() + 2 * board.getE() + Desloc );
        img33.setY( 0 + board.getYstart() + 2 * board.getDelta() + 2 * board.getE() + Desloc );
        img33.requestLayout();
    }

    public void atualizaBaralho()
    {
        if(turno==0) {
            imgdrag1.setVisibility( View.INVISIBLE );
            imgdrag2.setVisibility( View.INVISIBLE );
            imgdrag3.setVisibility( View.INVISIBLE );
            imgdrag4.setVisibility( View.INVISIBLE );
            for (int j = 0; j < 3; j++) {
                switch (carta.baralho[0][j]) {
                    case 0:
                        break;
                    case 1:
                        setCards( imgdrag1, 1 );
                        break;
                    case 2:
                        setCards( imgdrag2, 2 );
                        break;
                    case 3:
                        setCards( imgdrag3, 3 );
                        break;
                    default:
                        setCards( imgdrag4, carta.baralho[0][j] );
                        break;
                }
            }
        }
        else
        {
            //Seta cartas do adversário
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder( this )
                .setIcon( android.R.drawable.ic_dialog_alert )
                .setTitle( "Closing Activity" )
                .setMessage( "Are you sure you want to close this activity?" )
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d( "TAG", "The interstitial wasn`t loaded yet" );
                            gotoNext();
                        }
                    }
                } )
                .setNegativeButton( "No", null )
                .show();
    }

    private void gotoNext() {
        Intent adsIntent = new Intent( JogoActivity.this, OverviewActivity.class );
        adsIntent.putExtra( "ADS", true );
        JogoActivity.this.startActivity( adsIntent );
        JogoActivity.this.finish();
    }

    public void enableFullScreen() {
        Window window = getWindow();
        window.addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        window.requestFeature( Window.FEATURE_NO_TITLE );
        if (Build.VERSION.SDK_INT >= 19) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );

        }
    }

    @Override
    public void onClick(View v) {
        RepandRes();
        Toast.makeText( this, "Imagem tocada - remover toast onClick", Toast.LENGTH_SHORT ).show();
        //imagemDrag.setX(board.getXCenter( 23 )-board.getDelta()/4);
        //imagemDrag.setY(board.getYCenter( 23 )-board.getDelta()/4);
    }

    public boolean onTouch(View v, MotionEvent me) {
        RepandRes();
        int action = me.getAction();
        if (action == MotionEvent.ACTION_DOWN)
        {
            if (v.getTag() == "Tesoura")
            {
                imgd = 1;
            }
            else if (v.getTag() == "Pedra")
            {
                imgd = 2;
            }
            else if (v.getTag() == "Papel")
            {
                imgd = 3;
            }
            else
            {
                imgd = 4;
            }
            //tst.setText( String.valueOf(imgd) );
            ClipData data = ClipData.newPlainText( "", "" );
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder( v );
            v.startDrag( data, shadowBuilder, v, 0 );
            //v.setVisibility( v.INVISIBLE );
            // v.setVisibility( v.INVISIBLE );
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int dragAction = event.getAction();
        //PROCESSO INICIADO:
        if (dragAction == DragEvent.ACTION_DRAG_STARTED)
        {
            lastloc = 0;
            return true;
        }
        else if (dragAction == DragEvent.ACTION_DRAG_ENTERED)
        {
            return true;
        }
        else if (dragAction == DragEvent.ACTION_DRAG_EXITED)
        {
            if (atualoc!=0)
            {
                changeVisib(atualoc, 0);
            }
            atualizaTab();
            return false;
        }
        else if (dragAction == DragEvent.ACTION_DRAG_LOCATION)
        {
            //POSIÇÃO DA SOMBRA DO DRAG
            float posX = event.getX();
            float posY = event.getY();
            int teste = 0;
            //REGIÃO DO TABULEIRO ONDE A SOMBRA ESTÁ PROJETADA
            atualoc = board.whereIsIT( posX, posY );
            //TRADA A MUDANÇA DA REGIÃO DE PROJEÇÃO DA SOMBRA
            if (atualoc == lastloc && atualoc > 0)
            {
                //SOMBRA ESTÁ NA MESMA POSIÇÃO.
                //Animação da sombra com a ImagemView di tabuleiro
                //teste=tratamento(jogo.userMove( imgd, objectOnDrag()), true);
            }
            else if (atualoc == lastloc && atualoc <=0)
            {
                //EFETUA SAÍDA PELAS BORDAS. TRATAMENTO ANÁLOGO AO DRAG_EXIT.
                //changeVisib(lastloc);
            }
            else if (atualoc!=lastloc && atualoc<=0)
            {
                //SOMBRA SAIU DO TABULEIRO, MAS SUA ÚLTIMA POSIÇÃO AINDA CONTINNUA LÁ.
                if(!(lastloc<=0))
                {
                    setVisible(lastloc, jogo.getTabuleiroP( lastloc ), imgd, 0);
                    //changeVisib(lastloc, 0);
                }
                lastloc=atualoc;
            }
            else if(atualoc!=lastloc && atualoc>0)
            {
                if(lastloc>0)
                {
                    setVisible(lastloc, jogo.getTabuleiroP( lastloc ), imgd, 0);
                    setVisible(atualoc, jogo.getTabuleiroP( atualoc ), imgd, 1);
                }
                else
                {
                    setVisible(atualoc, jogo.getTabuleiroP( atualoc ), imgd, 1);
                }

                lastloc=atualoc;
            }
            return true;
        }
        else if (dragAction == DragEvent.ACTION_DROP)
        {
            tst.setText( "Rodada: " + String.valueOf( rodadas ) + "   Turno: " + String.valueOf( turno ) );
            setVisible( atualoc, jogo.getTabuleiroP( atualoc ), imgd, 0 );
            if(atualoc>0)
            {
                if(atualoc<=0)
                {
                    return false;
                }
                else
                {
                    if(imgd==4)
                    {
                        //TODO: EXERCUTAR DROP DE BOMBA
                        boolean bomba=false;
                        int i = -1;
                        while (!bomba && i<=2)
                        {
                            if(carta.baralho[turno][1+i]>=4)
                            {
                                i=carta.baralho[turno][(1 + i)];
                                bomba=true;
                            }
                            i++;
                        }
                        bomba(atualoc, i);//Anumacao
                        jogo.userMove(atualoc, i);//Atualiza classe smart
                        carta.destroyCard( imgd );
                        atualizaTab();  //Atualiza tabuleiro
                        atualizaBaralho();
                        mturno();
                        if(turno==1)
                        {
                            oponenteMove();
                        }
                    }
                    else
                    {
                        jogo.userMove(atualoc, imgd);//Atualiza classe smart
                        carta.destroyCard( imgd );
                        if(jogo.getChange())
                            atualizaTab();  //Atualiza tabuleiro
                        carta.setTurno( turno );
                        carta.addCard();
                        atualizaBaralho();
                        mturno();
                        if(turno == 1)
                        {
                            oponenteMove();
                        }
                    }

                    //o objeto de arrasto caiu em cima do tabuleiro
                    // atualloc deverá ser passado para obter mais info,
                }
                return true;
            }
            //RETORNAR AÇÃO DE DROP. VERIFICAR O QUE PODE ACONTECER.
            return false;
        }
        else
        {
            return false;
        }
    }

    private void oponenteMove() {
        //CRIAR CLASSE DE JOGADOR
        //TODO: CHECAR ESTADO DO ATUAL JOGO
        //      1) Oponente ameaçado
        //      2) Oponente em vantagem
        //      3) Definir função utilidade
        //      4) Definit probabilidade
    }


    public void bomba(int posic, int valor)
    {
    }

    private void setVisible(int posic, int pec , int drag, int action)
    {
        //CASO EM QUE A POSIÇÃO NO TABULEIRO ESTÁ VAZIA.
        if(pec==0)
        {
            if(action==0)
            {
                changeVisib( posic, 0 );
                mController( posic, drag, 0 );
            }
            else
            {
                switch (drag)
                {
                    case 1:
                        setSource( posic, 9, 1, 1 );
                        break;
                    case 2:
                        setSource( posic, 9, 1, 1 );
                        break;
                    case 3:
                        setSource( posic, 9, 1, 1 );
                        break;
                    case 4:
                        mController( posic, 4, 1 );
                        break;
                    default:
                        break;
                }
            }
        }
        else if(pec==1)
        {
            if(action==0)
            {
                setSource( posic, 1, 0, 1 );
                mController( posic, drag, 0 );

            }
            else
            {
                switch (drag)
                {
                    case 1:
                        setSource( posic, 1, 2, 1 );
                        break;
                    case 2:
                        setSource( posic, 1, 1, 1 );
                        break;
                    case 3:
                        setSource( posic, 1, 2, 1 );
                        break;
                    case 4:
                        mController( posic, 1, 1 );
                        break;
                    default:
                        break;
                }
            }
        }
        else if(pec==2)
        {
            if(action==0)
            {
                setSource( posic, 2, 0, 1 );
                mController( posic, drag, 0 );

            }
            else
            {
                switch (drag)
                {
                    case 1:
                        setSource( posic, 2, 2, 1 );
                        break;
                    case 2:
                        setSource( posic, 2, 2, 1 );
                        break;
                    case 3:
                        setSource( posic, 2, 1, 1 );
                        break;
                    case 4:
                        mController( posic, 2, 1 );
                        break;
                    default:
                        break;
                }
            }
        }
        else if(pec==3)
        {
            if(action==0)
            {
                setSource( posic, 3, 0, 1 );
                mController( posic, drag, 0 );

            }
            else            {
                switch (drag)
                {
                    case 1:
                        setSource( posic, 3, 1, 1 );
                        break;
                    case 2:
                        setSource( posic, 3, 2, 1 );
                        break;
                    case 3:
                        setSource( posic, 3, 2, 1 );
                        break;
                    case 4:
                        mController( posic, 3, 1 );
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void mController (int posic, int imgd, int action)
    {
        switch (imgd)
        {
            case 0:
            case 1:
            case 2:
            case 3:
                break;
            case 4:
                //procurar a bomba em questão:
                boolean bomba=false;
                int i = -1;
                while (!bomba && i<=2)
                {
                    if(carta.baralho[turno][1+i]>=4)
                    {
                        i=carta.baralho[turno][(1 + i)];
                        bomba=true;
                    }
                    i++;
                }
                switch (i-1)
                {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        break;
                    case 4:
                        //BOMBA ROSA
                        switch (posic)
                        {
                            case 1:
                                break;
                        }
                        break;

                    case 5:
                        //BOMBA PRETA
                        if(action==1)
                            setSource( posic, jogo.getTabuleiroP( posic ), 5, action );
                        else
                            setSource( posic, jogo.getTabuleiroP( posic ), 0, action );

                    case 6:
                        //BOMBA VERMELHA
                        break;

                    case 7:
                        //BOMBA AZUL
                        break;

                    case 8:
                        //BOMBA VERDE
                        break;

                }
        }
    }

    public void setSource (int posic, int imgd, int tipo, int action)
    {
        switch (posic) {
            case 11:
                if (action==0)
                    img11.setVisibility( View.INVISIBLE );
                else
                {
                    img11.setVisibility( View.VISIBLE );
                    switch (imgd) {
                        case 1:
                            switch (tipo) {
                                case 0:
                                    img11.setImageResource( R.drawable.tesoura );
                                    break;
                                case 1:
                                    img11.setImageResource( R.drawable.tesoura1 );
                                    break;
                                case 2:
                                    img11.setImageResource( R.drawable.tesoura2 );
                                    break;
                                case 4:
                                    img11.setImageResource( R.drawable.tesoura4 );
                                    break;
                                case 5:
                                    img11.setImageResource( R.drawable.tesoura5 );
                                    break;
                                case 6:
                                    img11.setImageResource( R.drawable.tesoura6 );
                                    break;
                                case 7:
                                    img11.setImageResource( R.drawable.tesoura7 );
                                    break;
                                case 8:
                                    img11.setImageResource( R.drawable.tesoura8 );
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (tipo) {
                                case 0:
                                    img11.setImageResource(R.drawable.pedra );
                                    break;
                                case 1:
                                    img11.setImageResource(R.drawable.pedra1);
                                    break;
                                case 2:
                                    img11.setImageResource(R.drawable.pedra2);
                                    break;
                                case 4:
                                    img11.setImageResource(R.drawable.pedra4);
                                    break;
                                case 5:
                                    img11.setImageResource(R.drawable.pedra5);
                                    break;
                                case 6:
                                    img11.setImageResource(R.drawable.pedra6);
                                    break;
                                case 7:
                                    img11.setImageResource(R.drawable.pedra7);
                                    break;
                                case 8:
                                    img11.setImageResource(R.drawable.pedra8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 3:
                            switch (tipo) {
                                case 0:
                                    img11.setImageResource( R.drawable.papel );
                                    break;
                                case 1:
                                    img11.setImageResource( R.drawable.papel1 );
                                    break;
                                case 2:
                                    img11.setImageResource( R.drawable.papel2 );
                                    break;
                                case 4:
                                    img11.setImageResource( R.drawable.papel4 );
                                    break;
                                case 5:
                                    img11.setImageResource( R.drawable.papel5 );
                                    break;
                                case 6:
                                    img11.setImageResource( R.drawable.papel6);
                                    break;
                                case 7:
                                    img11.setImageResource( R.drawable.papel7);
                                    break;
                                case 8:
                                    img11.setImageResource( R.drawable.papel8);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0:
                        case 9:
                            switch (tipo) {
                                case 1:
                                    img11.setImageResource( R.drawable.greennn);
                                    break;
                                case 2:
                                    img11.setImageResource( R.drawable.reddd);
                                    break;
                                case 4:
                                    img11.setImageResource( R.drawable.pinkkk);
                                    break;
                                case 5:
                                    img11.setImageResource( R.drawable.blackkk);
                                    break;
                                case 6:
                                    img11.setImageResource( R.drawable.bred);
                                    break;
                                case 7:
                                    img11.setImageResource( R.drawable.bluee);
                                    break;
                                case 8:
                                    img11.setImageResource( R.drawable.bgreen);
                                    break;
                                case 0:
                                default:
                                    img11.setVisibility( View.INVISIBLE );
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }
                break;

            case 12:
                if (action==0)
                    img12.setVisibility( View.INVISIBLE );
                else
                {
                    img12.setVisibility( View.VISIBLE );
                    switch (imgd) {
                        case 1:
                            switch (tipo) {
                                case 0:
                                    img12.setImageResource( R.drawable.tesoura );
                                    break;
                                case 1:
                                    img12.setImageResource( R.drawable.tesoura1 );
                                    break;
                                case 2:
                                    img12.setImageResource( R.drawable.tesoura2 );
                                    break;
                                case 4:
                                    img12.setImageResource( R.drawable.tesoura4 );
                                    break;
                                case 5:
                                    img12.setImageResource( R.drawable.tesoura5 );
                                    break;
                                case 6:
                                    img12.setImageResource( R.drawable.tesoura6 );
                                    break;
                                case 7:
                                    img12.setImageResource( R.drawable.tesoura7 );
                                    break;
                                case 8:
                                    img12.setImageResource( R.drawable.tesoura8 );
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (tipo) {
                                case 0:
                                    img12.setImageResource(R.drawable.pedra );
                                    break;
                                case 1:
                                    img12.setImageResource(R.drawable.pedra1);
                                    break;
                                case 2:
                                    img12.setImageResource(R.drawable.pedra2);
                                    break;
                                case 4:
                                    img12.setImageResource(R.drawable.pedra4);
                                    break;
                                case 5:
                                    img12.setImageResource(R.drawable.pedra5);
                                    break;
                                case 6:
                                    img12.setImageResource(R.drawable.pedra6);
                                    break;
                                case 7:
                                    img12.setImageResource(R.drawable.pedra7);
                                    break;
                                case 8:
                                    img12.setImageResource(R.drawable.pedra8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 3:
                            switch (tipo) {
                                case 0:
                                    img12.setImageResource( R.drawable.papel );
                                    break;
                                case 1:
                                    img12.setImageResource( R.drawable.papel1 );
                                    break;
                                case 2:
                                    img12.setImageResource( R.drawable.papel2 );
                                    break;
                                case 4:
                                    img12.setImageResource( R.drawable.papel4 );
                                    break;
                                case 5:
                                    img12.setImageResource( R.drawable.papel5 );
                                    break;
                                case 6:
                                    img12.setImageResource( R.drawable.papel6);
                                    break;
                                case 7:
                                    img12.setImageResource( R.drawable.papel7);
                                    break;
                                case 8:
                                    img12.setImageResource( R.drawable.papel8);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0:
                        case 9:
                            switch (tipo) {
                                case 1:
                                    img12.setImageResource( R.drawable.greennn);
                                    break;
                                case 2:
                                    img12.setImageResource( R.drawable.reddd);
                                    break;
                                case 4:
                                    img12.setImageResource( R.drawable.pinkkk);
                                    break;
                                case 5:
                                    img12.setImageResource( R.drawable.blackkk);
                                    break;
                                case 6:
                                    img12.setImageResource( R.drawable.bred);
                                    break;
                                case 7:
                                    img12.setImageResource( R.drawable.bluee);
                                    break;
                                case 8:
                                    img12.setImageResource( R.drawable.bgreen);
                                    break;
                                case 0:
                                default:
                                    img12.setVisibility( View.INVISIBLE );
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }
                break;

            case 13:
                img13.setVisibility( View.VISIBLE );
                if (action==0)
                    img13.setVisibility( View.INVISIBLE );
                else
                {
                    switch (imgd) {
                        case 1:
                            switch (tipo) {
                                case 0:
                                    img13.setImageResource( R.drawable.tesoura );
                                    break;
                                case 1:
                                    img13.setImageResource( R.drawable.tesoura1 );
                                    break;
                                case 2:
                                    img13.setImageResource( R.drawable.tesoura2 );
                                    break;
                                case 4:
                                    img13.setImageResource( R.drawable.tesoura4 );
                                    break;
                                case 5:
                                    img13.setImageResource( R.drawable.tesoura5 );
                                    break;
                                case 6:
                                    img13.setImageResource( R.drawable.tesoura6 );
                                    break;
                                case 7:
                                    img13.setImageResource( R.drawable.tesoura7 );
                                    break;
                                case 8:
                                    img13.setImageResource( R.drawable.tesoura8 );
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (tipo) {
                                case 0:
                                    img13.setImageResource(R.drawable.pedra );
                                    break;
                                case 1:
                                    img13.setImageResource(R.drawable.pedra1);
                                    break;
                                case 2:
                                    img13.setImageResource(R.drawable.pedra2);
                                    break;
                                case 4:
                                    img13.setImageResource(R.drawable.pedra4);
                                    break;
                                case 5:
                                    img13.setImageResource(R.drawable.pedra5);
                                    break;
                                case 6:
                                    img13.setImageResource(R.drawable.pedra6);
                                    break;
                                case 7:
                                    img13.setImageResource(R.drawable.pedra7);
                                    break;
                                case 8:
                                    img13.setImageResource(R.drawable.pedra8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 3:
                            switch (tipo) {
                                case 0:
                                    img13.setImageResource( R.drawable.papel );
                                    break;
                                case 1:
                                    img13.setImageResource( R.drawable.papel1 );
                                    break;
                                case 2:
                                    img13.setImageResource( R.drawable.papel2 );
                                    break;
                                case 4:
                                    img13.setImageResource( R.drawable.papel4 );
                                    break;
                                case 5:
                                    img13.setImageResource( R.drawable.papel5 );
                                    break;
                                case 6:
                                    img13.setImageResource( R.drawable.papel6);
                                    break;
                                case 7:
                                    img13.setImageResource( R.drawable.papel7);
                                    break;
                                case 8:
                                    img13.setImageResource( R.drawable.papel8);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0:
                        case 9:
                            switch (tipo) {
                                case 1:
                                    img13.setImageResource( R.drawable.greennn);
                                    break;
                                case 2:
                                    img13.setImageResource( R.drawable.reddd);
                                    break;
                                case 4:
                                    img13.setImageResource( R.drawable.pinkkk);
                                    break;
                                case 5:
                                    img13.setImageResource( R.drawable.blackkk);
                                    break;
                                case 6:
                                    img13.setImageResource( R.drawable.bred);
                                    break;
                                case 7:
                                    img13.setImageResource( R.drawable.bluee);
                                    break;
                                case 8:
                                    img13.setImageResource( R.drawable.bgreen);
                                    break;
                                case 0:
                                default:
                                    img13.setVisibility( View.INVISIBLE );
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }
                break;

            case 21:
                if (action==0)
                    img21.setVisibility( View.INVISIBLE );
                else
                {
                    img21.setVisibility( View.VISIBLE );
                    switch (imgd) {
                        case 1:
                            switch (tipo) {
                                case 0:
                                    img21.setImageResource( R.drawable.tesoura );
                                    break;
                                case 1:
                                    img21.setImageResource( R.drawable.tesoura1 );
                                    break;
                                case 2:
                                    img21.setImageResource( R.drawable.tesoura2 );
                                    break;
                                case 4:
                                    img21.setImageResource( R.drawable.tesoura4 );
                                    break;
                                case 5:
                                    img21.setImageResource( R.drawable.tesoura5 );
                                    break;
                                case 6:
                                    img21.setImageResource( R.drawable.tesoura6 );
                                    break;
                                case 7:
                                    img21.setImageResource( R.drawable.tesoura7 );
                                    break;
                                case 8:
                                    img21.setImageResource( R.drawable.tesoura8 );
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (tipo) {
                                case 0:
                                    img21.setImageResource(R.drawable.pedra );
                                    break;
                                case 1:
                                    img21.setImageResource(R.drawable.pedra1);
                                    break;
                                case 2:
                                    img21.setImageResource(R.drawable.pedra2);
                                    break;
                                case 4:
                                    img21.setImageResource(R.drawable.pedra4);
                                    break;
                                case 5:
                                    img21.setImageResource(R.drawable.pedra5);
                                    break;
                                case 6:
                                    img21.setImageResource(R.drawable.pedra6);
                                    break;
                                case 7:
                                    img21.setImageResource(R.drawable.pedra7);
                                    break;
                                case 8:
                                    img21.setImageResource(R.drawable.pedra8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 3:
                            switch (tipo) {
                                case 0:
                                    img21.setImageResource( R.drawable.papel );
                                    break;
                                case 1:
                                    img21.setImageResource( R.drawable.papel1 );
                                    break;
                                case 2:
                                    img21.setImageResource( R.drawable.papel2 );
                                    break;
                                case 4:
                                    img21.setImageResource( R.drawable.papel4 );
                                    break;
                                case 5:
                                    img21.setImageResource( R.drawable.papel5 );
                                    break;
                                case 6:
                                    img21.setImageResource( R.drawable.papel6);
                                    break;
                                case 7:
                                    img21.setImageResource( R.drawable.papel7);
                                    break;
                                case 8:
                                    img21.setImageResource( R.drawable.papel8);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0:
                        case 9:
                            switch (tipo) {
                                case 1:
                                    img21.setImageResource( R.drawable.greennn);
                                    break;
                                case 2:
                                    img21.setImageResource( R.drawable.reddd);
                                    break;
                                case 4:
                                    img21.setImageResource( R.drawable.pinkkk);
                                    break;
                                case 5:
                                    img21.setImageResource( R.drawable.blackkk);
                                    break;
                                case 6:
                                    img21.setImageResource( R.drawable.bred);
                                    break;
                                case 7:
                                    img21.setImageResource( R.drawable.bluee);
                                    break;
                                case 8:
                                    img21.setImageResource( R.drawable.bgreen);
                                    break;
                                case 0:
                                default:
                                    img21.setVisibility( View.INVISIBLE );
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }
                break;

            case 22:
                if (action==0)
                    img22.setVisibility( View.INVISIBLE );
                else
                {
                    img22.setVisibility( View.VISIBLE );
                    switch (imgd) {
                        case 1:
                            switch (tipo) {
                                case 0:
                                    img22.setImageResource( R.drawable.tesoura );
                                    break;
                                case 1:
                                    img22.setImageResource( R.drawable.tesoura1 );
                                    break;
                                case 2:
                                    img22.setImageResource( R.drawable.tesoura2 );
                                    break;
                                case 4:
                                    img22.setImageResource( R.drawable.tesoura4 );
                                    break;
                                case 5:
                                    img22.setImageResource( R.drawable.tesoura5 );
                                    break;
                                case 6:
                                    img22.setImageResource( R.drawable.tesoura6 );
                                    break;
                                case 7:
                                    img22.setImageResource( R.drawable.tesoura7 );
                                    break;
                                case 8:
                                    img22.setImageResource( R.drawable.tesoura8 );
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (tipo) {
                                case 0:
                                    img22.setImageResource(R.drawable.pedra );
                                    break;
                                case 1:
                                    img22.setImageResource(R.drawable.pedra1);
                                    break;
                                case 2:
                                    img22.setImageResource(R.drawable.pedra2);
                                    break;
                                case 4:
                                    img22.setImageResource(R.drawable.pedra4);
                                    break;
                                case 5:
                                    img22.setImageResource(R.drawable.pedra5);
                                    break;
                                case 6:
                                    img22.setImageResource(R.drawable.pedra6);
                                    break;
                                case 7:
                                    img22.setImageResource(R.drawable.pedra7);
                                    break;
                                case 8:
                                    img22.setImageResource(R.drawable.pedra8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 3:
                            switch (tipo) {
                                case 0:
                                    img22.setImageResource( R.drawable.papel );
                                    break;
                                case 1:
                                    img22.setImageResource( R.drawable.papel1 );
                                    break;
                                case 2:
                                    img22.setImageResource( R.drawable.papel2 );
                                    break;
                                case 4:
                                    img22.setImageResource( R.drawable.papel4 );
                                    break;
                                case 5:
                                    img22.setImageResource( R.drawable.papel5 );
                                    break;
                                case 6:
                                    img22.setImageResource( R.drawable.papel6);
                                    break;
                                case 7:
                                    img22.setImageResource( R.drawable.papel7);
                                    break;
                                case 8:
                                    img22.setImageResource( R.drawable.papel8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 0:
                        case 9:
                            switch (tipo) {
                                case 1:
                                    img22.setImageResource( R.drawable.greennn);
                                    break;
                                case 2:
                                    img22.setImageResource( R.drawable.reddd);
                                    break;
                                case 4:
                                    img22.setImageResource( R.drawable.pinkkk);
                                    break;
                                case 5:
                                    img22.setImageResource( R.drawable.blackkk);
                                    break;
                                case 6:
                                    img22.setImageResource( R.drawable.bred);
                                    break;
                                case 7:
                                    img22.setImageResource( R.drawable.bluee);
                                    break;
                                case 8:
                                    img22.setImageResource( R.drawable.bgreen);
                                    break;
                                case 0:
                                default:
                                    img22.setVisibility( View.INVISIBLE );
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }
                break;

            case 23:
                if (action==0)
                    img23.setVisibility( View.INVISIBLE );
                else
                {
                    img23.setVisibility( View.VISIBLE );
                    switch (imgd) {
                        case 1:
                            switch (tipo) {
                                case 0:
                                    img23.setImageResource( R.drawable.tesoura );
                                    break;
                                case 1:
                                    img23.setImageResource( R.drawable.tesoura1 );
                                    break;
                                case 2:
                                    img23.setImageResource( R.drawable.tesoura2 );
                                    break;
                                case 4:
                                    img23.setImageResource( R.drawable.tesoura4 );
                                    break;
                                case 5:
                                    img23.setImageResource( R.drawable.tesoura5 );
                                    break;
                                case 6:
                                    img23.setImageResource( R.drawable.tesoura6 );
                                    break;
                                case 7:
                                    img23.setImageResource( R.drawable.tesoura7 );
                                    break;
                                case 8:
                                    img23.setImageResource( R.drawable.tesoura8 );
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (tipo) {
                                case 0:
                                    img23.setImageResource(R.drawable.pedra );
                                    break;
                                case 1:
                                    img23.setImageResource(R.drawable.pedra1);
                                    break;
                                case 2:
                                    img23.setImageResource(R.drawable.pedra2);
                                    break;
                                case 4:
                                    img23.setImageResource(R.drawable.pedra4);
                                    break;
                                case 5:
                                    img23.setImageResource(R.drawable.pedra5);
                                    break;
                                case 6:
                                    img23.setImageResource(R.drawable.pedra6);
                                    break;
                                case 7:
                                    img23.setImageResource(R.drawable.pedra7);
                                    break;
                                case 8:
                                    img23.setImageResource(R.drawable.pedra8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 3:
                            switch (tipo) {
                                case 0:
                                    img23.setImageResource( R.drawable.papel );
                                    break;
                                case 1:
                                    img23.setImageResource( R.drawable.papel1 );
                                    break;
                                case 2:
                                    img23.setImageResource( R.drawable.papel2 );
                                    break;
                                case 4:
                                    img23.setImageResource( R.drawable.papel4 );
                                    break;
                                case 5:
                                    img23.setImageResource( R.drawable.papel5 );
                                    break;
                                case 6:
                                    img23.setImageResource( R.drawable.papel6);
                                    break;
                                case 7:
                                    img23.setImageResource( R.drawable.papel7);
                                    break;
                                case 8:
                                    img23.setImageResource( R.drawable.papel8);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0:
                        case 9:
                            switch (tipo) {
                                case 1:
                                    img23.setImageResource( R.drawable.greennn);
                                    break;
                                case 2:
                                    img23.setImageResource( R.drawable.reddd);
                                    break;
                                case 4:
                                    img23.setImageResource( R.drawable.pinkkk);
                                    break;
                                case 5:
                                    img23.setImageResource( R.drawable.blackkk);
                                    break;
                                case 6:
                                    img23.setImageResource( R.drawable.bred);
                                    break;
                                case 7:
                                    img23.setImageResource( R.drawable.bluee);
                                    break;
                                case 8:
                                    img23.setImageResource( R.drawable.bgreen);
                                    break;
                                case 0:
                                default:
                                    img23.setVisibility( View.INVISIBLE );
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }
                break;

            case 31:
                if (action==0)
                    img31.setVisibility( View.INVISIBLE );
                else
                {
                    img31.setVisibility( View.VISIBLE );
                    switch (imgd) {
                        case 1:
                            switch (tipo) {
                                case 0:
                                    img31.setImageResource( R.drawable.tesoura );
                                    break;
                                case 1:
                                    img31.setImageResource( R.drawable.tesoura1 );
                                    break;
                                case 2:
                                    img31.setImageResource( R.drawable.tesoura2 );
                                    break;
                                case 4:
                                    img31.setImageResource( R.drawable.tesoura4 );
                                    break;
                                case 5:
                                    img31.setImageResource( R.drawable.tesoura5 );
                                    break;
                                case 6:
                                    img31.setImageResource( R.drawable.tesoura6 );
                                    break;
                                case 7:
                                    img31.setImageResource( R.drawable.tesoura7 );
                                    break;
                                case 8:
                                    img31.setImageResource( R.drawable.tesoura8 );
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (tipo) {
                                case 0:
                                    img31.setImageResource(R.drawable.pedra );
                                    break;
                                case 1:
                                    img31.setImageResource(R.drawable.pedra1);
                                    break;
                                case 2:
                                    img31.setImageResource(R.drawable.pedra2);
                                    break;
                                case 4:
                                    img31.setImageResource(R.drawable.pedra4);
                                    break;
                                case 5:
                                    img31.setImageResource(R.drawable.pedra5);
                                    break;
                                case 6:
                                    img31.setImageResource(R.drawable.pedra6);
                                    break;
                                case 7:
                                    img31.setImageResource(R.drawable.pedra7);
                                    break;
                                case 8:
                                    img31.setImageResource(R.drawable.pedra8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 3:
                            switch (tipo) {
                                case 0:
                                    img31.setImageResource( R.drawable.papel );
                                    break;
                                case 1:
                                    img31.setImageResource( R.drawable.papel1 );
                                    break;
                                case 2:
                                    img31.setImageResource( R.drawable.papel2 );
                                    break;
                                case 4:
                                    img31.setImageResource( R.drawable.papel4 );
                                    break;
                                case 5:
                                    img31.setImageResource( R.drawable.papel5 );
                                    break;
                                case 6:
                                    img31.setImageResource( R.drawable.papel6);
                                    break;
                                case 7:
                                    img31.setImageResource( R.drawable.papel7);
                                    break;
                                case 8:
                                    img31.setImageResource( R.drawable.papel8);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0:
                        case 9:
                            switch (tipo) {
                                case 1:
                                    img31.setImageResource( R.drawable.greennn);
                                    break;
                                case 2:
                                    img31.setImageResource( R.drawable.reddd);
                                    break;
                                case 4:
                                    img31.setImageResource( R.drawable.pinkkk);
                                    break;
                                case 5:
                                    img31.setImageResource( R.drawable.blackkk);
                                    break;
                                case 6:
                                    img31.setImageResource( R.drawable.bred);
                                    break;
                                case 7:
                                    img31.setImageResource( R.drawable.bluee);
                                    break;
                                case 8:
                                    img31.setImageResource( R.drawable.bgreen);
                                    break;
                                case 0:
                                default:
                                    img31.setVisibility( View.INVISIBLE );
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }
                break;

            case 32:
                if (action==0)
                    img32.setVisibility( View.INVISIBLE );
                else
                {
                    img32.setVisibility( View.VISIBLE );
                    switch (imgd) {
                        case 1:
                            switch (tipo) {
                                case 0:
                                    img32.setImageResource( R.drawable.tesoura );
                                    break;
                                case 1:
                                    img32.setImageResource( R.drawable.tesoura1 );
                                    break;
                                case 2:
                                    img32.setImageResource( R.drawable.tesoura2 );
                                    break;
                                case 4:
                                    img32.setImageResource( R.drawable.tesoura4 );
                                    break;
                                case 5:
                                    img32.setImageResource( R.drawable.tesoura5 );
                                    break;
                                case 6:
                                    img32.setImageResource( R.drawable.tesoura6 );
                                    break;
                                case 7:
                                    img32.setImageResource( R.drawable.tesoura7 );
                                    break;
                                case 8:
                                    img32.setImageResource( R.drawable.tesoura8 );
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (tipo) {
                                case 0:
                                    img32.setImageResource(R.drawable.pedra );
                                    break;
                                case 1:
                                    img32.setImageResource(R.drawable.pedra1);
                                    break;
                                case 2:
                                    img32.setImageResource(R.drawable.pedra2);
                                    break;
                                case 4:
                                    img32.setImageResource(R.drawable.pedra4);
                                    break;
                                case 5:
                                    img32.setImageResource(R.drawable.pedra5);
                                    break;
                                case 6:
                                    img32.setImageResource(R.drawable.pedra6);
                                    break;
                                case 7:
                                    img32.setImageResource(R.drawable.pedra7);
                                    break;
                                case 8:
                                    img32.setImageResource(R.drawable.pedra8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 3:
                            switch (tipo) {
                                case 0:
                                    img32.setImageResource( R.drawable.papel );
                                    break;
                                case 1:
                                    img32.setImageResource( R.drawable.papel1 );
                                    break;
                                case 2:
                                    img32.setImageResource( R.drawable.papel2 );
                                    break;
                                case 4:
                                    img32.setImageResource( R.drawable.papel4 );
                                    break;
                                case 5:
                                    img32.setImageResource( R.drawable.papel5 );
                                    break;
                                case 6:
                                    img32.setImageResource( R.drawable.papel6);
                                    break;
                                case 7:
                                    img32.setImageResource( R.drawable.papel7);
                                    break;
                                case 8:
                                    img32.setImageResource( R.drawable.papel8);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0:
                        case 9:
                            switch (tipo) {
                                case 1:
                                    img32.setImageResource( R.drawable.greennn);
                                    break;
                                case 2:
                                    img32.setImageResource( R.drawable.reddd);
                                    break;
                                case 4:
                                    img32.setImageResource( R.drawable.pinkkk);
                                    break;
                                case 5:
                                    img32.setImageResource( R.drawable.blackkk);
                                    break;
                                case 6:
                                    img32.setImageResource( R.drawable.bred);
                                    break;
                                case 7:
                                    img32.setImageResource( R.drawable.bluee);
                                    break;
                                case 8:
                                    img32.setImageResource( R.drawable.bgreen);
                                    break;
                                case 0:
                                default:
                                    img32.setVisibility( View.INVISIBLE );
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }
                break;


            case 33:
                if (action==0)
                    img33.setVisibility( View.INVISIBLE );
                else
                {
                    img33.setVisibility( View.VISIBLE );
                    switch (imgd) {
                        case 1:
                            switch (tipo) {
                                case 0:
                                    img33.setImageResource( R.drawable.tesoura );
                                    break;
                                case 1:
                                    img33.setImageResource( R.drawable.tesoura1 );
                                    break;
                                case 2:
                                    img33.setImageResource( R.drawable.tesoura2 );
                                    break;
                                case 4:
                                    img33.setImageResource( R.drawable.tesoura4 );
                                    break;
                                case 5:
                                    img33.setImageResource( R.drawable.tesoura5 );
                                    break;
                                case 6:
                                    img33.setImageResource( R.drawable.tesoura6 );
                                    break;
                                case 7:
                                    img33.setImageResource( R.drawable.tesoura7 );
                                    break;
                                case 8:
                                    img33.setImageResource( R.drawable.tesoura8 );
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (tipo) {
                                case 0:
                                    img33.setImageResource(R.drawable.pedra );
                                    break;
                                case 1:
                                    img33.setImageResource(R.drawable.pedra1);
                                    break;
                                case 2:
                                    img33.setImageResource(R.drawable.pedra2);
                                    break;
                                case 4:
                                    img33.setImageResource(R.drawable.pedra4);
                                    break;
                                case 5:
                                    img33.setImageResource(R.drawable.pedra5);
                                    break;
                                case 6:
                                    img33.setImageResource(R.drawable.pedra6);
                                    break;
                                case 7:
                                    img33.setImageResource(R.drawable.pedra7);
                                    break;
                                case 8:
                                    img33.setImageResource(R.drawable.pedra8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 3:
                            switch (tipo) {
                                case 0:
                                    img33.setImageResource( R.drawable.papel );
                                    break;
                                case 1:
                                    img33.setImageResource( R.drawable.papel1 );
                                    break;
                                case 2:
                                    img33.setImageResource( R.drawable.papel2 );
                                    break;
                                case 4:
                                    img33.setImageResource( R.drawable.papel4 );
                                    break;
                                case 5:
                                    img33.setImageResource( R.drawable.papel5 );
                                    break;
                                case 6:
                                    img33.setImageResource( R.drawable.papel6);
                                    break;
                                case 7:
                                    img33.setImageResource( R.drawable.papel7);
                                    break;
                                case 8:
                                    img33.setImageResource( R.drawable.papel8);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 0:
                        case 9:
                            switch (tipo) {
                                case 1:
                                    img33.setImageResource( R.drawable.greennn);
                                    break;
                                case 2:
                                    img33.setImageResource( R.drawable.reddd);
                                    break;
                                case 4:
                                    img33.setImageResource( R.drawable.pinkkk);
                                    break;
                                case 5:
                                    img33.setImageResource( R.drawable.blackkk);
                                    break;
                                case 6:
                                    img33.setImageResource( R.drawable.bred);
                                    break;
                                case 7:
                                    img33.setImageResource( R.drawable.bluee);
                                    break;
                                case 8:
                                    img33.setImageResource( R.drawable.bgreen);
                                    break;
                                default:
                                    img33.setVisibility( View.INVISIBLE );
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }
                break;

            default:
                break;
        }
    }

    public void changeVisib ( int posic, int visible)
        {
            switch (posic) {
                case 11:
                    if (visible==0)
                        img11.setVisibility( View.INVISIBLE );
                    else
                        img11.setVisibility( View.VISIBLE );
                    break;
                case 12:
                    if (visible==0)
                        img12.setVisibility( View.INVISIBLE );
                    else
                        img12.setVisibility( View.VISIBLE );
                    break;
                case 13:
                    if (visible==0)
                        img13.setVisibility( View.INVISIBLE );
                    else
                        img13.setVisibility( View.VISIBLE );
                    break;
                case 21:
                    if (visible==0)
                        img21.setVisibility( View.INVISIBLE );
                    else
                        img21.setVisibility( View.VISIBLE );
                    break;
                case 22:
                    if (visible==0)
                        img22.setVisibility( View.INVISIBLE );
                    else
                        img22.setVisibility( View.VISIBLE );
                    break;
                case 23:
                    if (visible==0)
                        img23.setVisibility( View.INVISIBLE );
                    else
                        img23.setVisibility( View.VISIBLE );
                    break;
                case 31:
                    if (visible==0)
                        img31.setVisibility( View.INVISIBLE );
                    else
                        img31.setVisibility( View.VISIBLE );
                    break;
                case 32:
                    if (visible==0)
                        img32.setVisibility( View.INVISIBLE );
                    else
                        img32.setVisibility( View.VISIBLE );
                    break;
                case 33:
                    if (visible==0)
                        img33.setVisibility( View.INVISIBLE );
                    else
                        img33.setVisibility( View.VISIBLE );
                    break;
                default:
                    break;
            }
        }

    public void atualizaTab()
    {
        for(int i = 0; i<3; i++)
        {
            for(int j =0; j<3; j++)
            {
                if(jogo.getTabuleiro(i, j)==0)
                {
                    changeVisib( 10*i+j+11, 0 );
                }
                else
                {
                    setSource( 10*i+j+11, jogo.getTabuleiro( i, j ), 0, 1  );
                }
            }
        }

    }

    private void mturno()
    {
        if(turno==0)
        {
            turno = 1;
        }
        else
        {
            turno = 0;
            rodadas=rodadas+1;
        }
        carta.changeTurno( turno );
    }
    }
