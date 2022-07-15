package com.mrtechno.crazytictacpo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static android.widget.Toast.*;

public class CartaClass
{
    // 1: TESOURA
    // 2: PEDRA
    // 3: PAPEL

    // 4: BOMBA BRANCA
    // 5: BOMBA PRETA
    // 6: BOMBA VERMELHA
    // 7: BOMBA VERDE
    // 8: BOMBA AZUL

   //PLAYERS TERÃO APENAS DUAS CARTAS
    // BOMBAS: PROBABILIDADES TOTAIS: 10% DAS QUAIS (5% BLACK, 2% WHITE, 1.29% red and blue and 0,420% green
    // DEDOS: 90% de probabilidade, das quais 30% tesoura, 30% PEDRA, 30%PAPEL
    // Modo 1: APÓS 10 RODADAS OS JOGADORES PASSAM A TER 3 CARTAS (PROBAILIDADE DA BOMBA PRETA AUMENTA)
    // Modo 2: APOS 25 RODADAS OS JOGASORES PASSAM A TER 4 CARTAS (PROBABILIDADE DA BOMBA PRETA AUMENTA)

    public int[][] baralho = new int[2][4];
    private int turno=0;
    private int modo = 0;
    public int aux = 0;
    public int aux1 = 0;
    public String erro = "";

    public CartaClass() {
        //iniciar baralho
        for(int i=0; i<2; i++)
        {
            for(int j = 0; j<4; j++)
            {
                baralho[i][j]=0;
            }
        }

        //Iniciar turno
        turno = 0;
        addCard( );
        addCard();
        changeTurno(turno);
        addCard();
        addCard();
        changeTurno(turno);
    }

    //TODO: Atualizar carta/Destruir Carta

    public int qqCard(int vez)
    {
        if(vez==0)
        {
            int count = 0;
            for(int i=0;i<4;i++)
            {
                if(baralho[0][i]>0)
                {
                    count=count+1;
                }
            }
            return count;
        }
        else
        {
            int count = 0;
            for(int i=0;i<4;i++)
            {
                if(baralho[1][i]>0)
                {
                    count=count+1;
                }
            }
            return count;
        }
    }



    public void addCard ()
    {
        if(getModo()==0) {
            if(qqCard( turno )==0)
            {
                baralho[getTurno()][0] = aleatórioCard();
            }
            else if(qqCard(turno)==1) {
                //Adicionar uma carta
                if (qqBomba() == 1) {
                    if (baralho[getTurno()][0] == 0) {
                        baralho[getTurno()][0] = aleatorioDedo();
                        return;
                    } else if (baralho[getTurno()][1] == 0) {
                        baralho[getTurno()][1] = aleatorioDedo();
                        return;
                    } else if (baralho[getTurno()][2] == 0) {
                        baralho[getTurno()][2] = aleatorioDedo();
                        return;
                    } else if (baralho[getTurno()][3] == 0) {
                        baralho[getTurno()][3] = aleatorioDedo();
                        return;
                    }
                }
                else
                {
                    //A bomba pode ser sorteada e pode haver repetição nos dedos.
                    aux = aleatórioCard();
                    if(aux>3)
                    {
                        //A carta sorteada foi uma bomba. Basta adiciona-la em qualquer lugar vazio
                        if (baralho[getTurno()][0] == 0) {
                            //baralho[getTurno()][0] = aux;
                            baralho[getTurno()][0] = aleatorioDedo();
                            return;
                        } else if (baralho[getTurno()][1] == 0) {
                            //baralho[getTurno()][1] = aux;
                            baralho[getTurno()][1] = aleatorioDedo();
                            return;
                        } else if (baralho[getTurno()][2] == 0) {
                            //baralho[getTurno()][2] = aux;
                            baralho[getTurno()][2] = aleatorioDedo();
                            return;
                        } else if (baralho[0][3] == 0) {
                            baralho[getTurno()][3] = aleatorioDedo();
                            //baralho[getTurno()][3] = aux;
                            return;
                        }
                    }
                    else
                    {
                        if(aux==(baralho[getTurno()][0]+baralho[getTurno()][1]+baralho[getTurno()][2]+baralho[getTurno()][3]))
                        {
                            if(aux == 3)
                            {
                                aux=aleatorio12();
                                if (baralho[getTurno()][0] == 0) {
                                    baralho[getTurno()][0] = aux;
                                    return;
                                } else if (baralho[getTurno()][1] == 0) {
                                    baralho[getTurno()][1] = aux;
                                    return;
                                } else if (baralho[getTurno()][2] == 0) {
                                    baralho[getTurno()][2] = aux;
                                    return;
                                } else if (baralho[getTurno()][3] == 0) {
                                    baralho[getTurno()][3] = aux;
                                    return;
                                }
                            }
                            else if(aux==2)
                            {
                                aux=aleatorio13();
                                if (baralho[getTurno()][0] == 0) {
                                    baralho[getTurno()][0] = aux;
                                    return;
                                } else if (baralho[getTurno()][1] == 0) {
                                    baralho[getTurno()][1] = aux;
                                    return;
                                } else if (baralho[getTurno()][2] == 0) {
                                    baralho[getTurno()][2] = aux;
                                    return;
                                } else if (baralho[getTurno()][3] == 0) {
                                    baralho[getTurno()][3] = aux;
                                    return;
                                }
                            }
                            else if(aux==1)
                            {
                                aux=aleatorio23();
                                if (baralho[getTurno()][0] == 0) {
                                    baralho[getTurno()][0] = aux;
                                    return;
                                } else if (baralho[getTurno()][1] == 0) {
                                    baralho[getTurno()][1] = aux;
                                    return;
                                } else if (baralho[getTurno()][2] == 0) {
                                    baralho[getTurno()][2] = aux;
                                    return;
                                } else if (baralho[getTurno()][3] == 0) {
                                    baralho[getTurno()][3] = aux;
                                    return;
                                }
                            }


                        }
                        else
                        {
                            if (baralho[getTurno()][0] == 0) {
                                baralho[getTurno()][0] = aux;
                                return;
                            } else if (baralho[getTurno()][1] == 0) {
                                baralho[getTurno()][1] = aux;
                                return;
                            } else if (baralho[getTurno()][2] == 0) {
                                baralho[getTurno()][2] = aux;
                                return;
                            } else if (baralho[getTurno()][3] == 0) {
                                baralho[getTurno()][3] = aux;
                                return;
                            }
                        }
                    }

                }
            }
                else
            {
                erro = erro + "Erro de contagem no modo 0";
            }
        }
        else if(getModo()==1)
        {
            if(qqCard(turno)==2)
            {
                if(qqBomba()==1)
                {
                    //Deve-se adicionar um dedo, sendo que há um disponível
                    aux=0;
                    for(int i = 0; i<4; i++)
                    {
                        if(baralho[getTurno()][i]<=3)
                        {
                            aux=aux+baralho[getTurno()][i];
                        }
                    }
                    aux1=aleatórioCard();
                    if(aux==aux1)
                    {
                        if(aux==1)
                        {
                            aux = aleatorio23();
                            if (baralho[getTurno()][0] == 0) {
                                baralho[getTurno()][0] = aux;
                                return;
                            } else if (baralho[getTurno()][1] == 0) {
                                baralho[getTurno()][1] = aux;
                                return;
                            } else if (baralho[getTurno()][2] == 0) {
                                baralho[getTurno()][2] = aux;
                                return;
                            } else if (baralho[getTurno()][3] == 0) {
                                baralho[getTurno()][3] = aux;
                                return;
                            }
                        }
                        else if(aux==2)
                        {
                            aux = aleatorio13();
                            if (baralho[getTurno()][0] == 0) {
                                baralho[getTurno()][0] = aux;
                                return;
                            } else if (baralho[getTurno()][1] == 0) {
                                baralho[getTurno()][1] = aux;
                                return;
                            } else if (baralho[getTurno()][2] == 0) {
                                baralho[getTurno()][2] = aux;
                                return;
                            } else if (baralho[getTurno()][3] == 0) {
                                baralho[getTurno()][3] = aux;
                                return;
                            }
                        }
                        else if(aux==3)
                        {
                            aux = aleatorio12();
                            if (baralho[getTurno()][0] == 0) {
                                baralho[getTurno()][0] = aux;
                                return;
                            } else if (baralho[getTurno()][1] == 0) {
                                baralho[getTurno()][1] = aux;
                                return;
                            } else if (baralho[getTurno()][2] == 0) {
                                baralho[getTurno()][2] = aux;
                                return;
                            } else if (baralho[getTurno()][3] == 0) {
                                baralho[getTurno()][3] = aux;
                                return;
                            }
                        }
                    }
                    else
                    {
                        if (baralho[getTurno()][0] == 0) {
                            baralho[getTurno()][0] = aux1;
                            return;
                        } else if (baralho[getTurno()][1] == 0) {
                            baralho[getTurno()][1] = aux1;
                            return;
                        } else if (baralho[getTurno()][2] == 0) {
                            baralho[getTurno()][2] = aux1;
                            return;
                        } else if (baralho[getTurno()][3] == 0) {
                            baralho[getTurno()][3] = aux1;
                            return;
                        }
                    }
                }
                else
                {
                    //Deve-se adicionar um dedo, sendo dois outros disponíveis
                    aux = 6-baralho[getTurno()][0]-baralho[getTurno()][1]-baralho[getTurno()][2]-baralho[getTurno()][3];
                    if (baralho[getTurno()][0] == 0) {
                        baralho[getTurno()][0] = aux;
                        return;
                    } else if (baralho[getTurno()][1] == 0) {
                        baralho[getTurno()][1] = aux;
                        return;
                    } else if (baralho[getTurno()][2] == 0) {
                        baralho[getTurno()][2] = aux;
                        return;
                    } else if (baralho[getTurno()][3] == 0) {
                        baralho[getTurno()][3] = aux;
                        return;
                    }
                }
            }
            else
            {
                erro = erro + "Erro de contagem no modo 1";
            }
            if(getModo()==2)
            {
                if(qqCard(turno)==3)
                {
                    if(qqBomba()==1)
                    {
                        aux = 6-baralho[getTurno()][0]-baralho[getTurno()][1]-baralho[getTurno()][2]-baralho[getTurno()][3];
                        if (baralho[getTurno()][0] == 0) {
                            baralho[getTurno()][0] = aux;
                            return;
                        } else if (baralho[getTurno()][1] == 0) {
                            baralho[getTurno()][1] = aux;
                            return;
                        } else if (baralho[getTurno()][2] == 0) {
                            baralho[getTurno()][2] = aux;
                            return;
                        } else if (baralho[getTurno()][3] == 0) {
                            baralho[getTurno()][3] = aux;
                            return;
                        }
                    }
                    else
                    {
                        aux = aleatórioCard();
                        if(aux>3)
                        {
                            if (baralho[getTurno()][0] == 0) {
                                baralho[getTurno()][0] = aux;
                                return;
                            } else if (baralho[getTurno()][1] == 0) {
                                baralho[getTurno()][1] = aux;
                                return;
                            } else if (baralho[getTurno()][2] == 0) {
                                baralho[getTurno()][2] = aux;
                                return;
                            } else if (baralho[getTurno()][3] == 0) {
                                baralho[getTurno()][3] = aux;
                                return;
                            }
                        }
                    }
                }
                else
                {
                    erro = erro + "Erro de contagem no modo 2";
                }
            }
        }
    }

    public void destroyCard (int valor)
    {
        if (baralho[getTurno()][0] == valor) {
            baralho[getTurno()][0] = 0;
            return;
        } else if (baralho[getTurno()][1] == valor) {
            baralho[getTurno()][1] = 0;
            return;
        } else if (baralho[getTurno()][2] == valor) {
            baralho[getTurno()][2] = 0;
            return;
        }
        else if (baralho[getTurno()][3] == valor) {
            baralho[getTurno()][3] = 0;
            return;
        }
        else
        {
            erro = erro + "bug encontrado em Destroy Card";
        }
    }

    public int aleatórioCard()
    {
        Random r = new Random( );
        int i1 = (r.nextInt(101-1)+1);
        if(getModo()==0)
        {
            if(i1<=90)
            {
                return aleatorioDedo();
            }
            else
            {
                return aleatorioBomba();
            }
        }
        else if(getModo()==1)
        {
            if(i1<=93)
            {
                return aleatorioDedo();
            }
            else
            {
                return aleatorioBomba();
            }
        }
        else if(getModo()==2)
        {
            if(i1<=97)
            {
                return aleatorioDedo();
            }
            else
            {
                return aleatorioBomba();
            }
        }
        return 0;
    }


    public int aleatorioBomba()
    {
        Random r = new Random( );
        int i1 = (r.nextInt(1001-1)+1);
        if(getModo()==0) {

            if (i1 <= 42) {
                return 7;
            } else if (i1 <= 171) {
                return 6;
            } else if (i1 <= 300) {
                return 8;
            } else if (i1 <= 500) {
                return 4;
            } else if (i1 <= 1000) {
                return 5;
            }
        }
        else if(getModo()==1) {

            if (i1 <= 30) {
                return 7;
            } else if (i1 <= 96) {
                return 6;
            } else if (i1 <= 160) {
                return 8;
            } else if (i1 <= 400) {
                return 4;
            } else if (i1 <= 1000) {
                return 5;
            }
        }
        else if(getModo()==2) {

            if (i1 <= 20) {
                return 7;
            } else if (i1 <= 60) {
                return 6;
            } else if (i1 <= 100) {
                return 8;
            } else if (i1 <= 300) {
                return 4;
            } else if (i1 <= 1000) {
                return 5;
            }
        }
        return 0;
    }

    public int aleatorio12()
    {
        Random r = new Random( );
        int i1 = (r.nextInt(301-1)+1);
        if(i1<=150)
        {
            return 1;
        }
        else {
            return 1;
        }
    }

    public int aleatorio13()
    {
        Random r = new Random( );
        int i1 = (r.nextInt(301-1)+1);
        if(i1<=150)
        {
            return 1;
        }
        else {
            return 3;
        }
    }

    public int aleatorio23()
    {
        Random r = new Random( );
        int i1 = (r.nextInt(301-1)+1);
        if(i1<=150)
        {
            return 2;
        }
        else {
            return 3;
        }
    }

    public int aleatorioDedo()
    {
        Random r = new Random( );
        int i1 = (r.nextInt(301-1)+1);
        if(i1<=100)
        {
            return 1;
        }
        else if(i1<=200)
        {
            return 2;
        }
        else if(i1<=300)
        {
            return 3;
        }
        return 0;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public void changeTurno(int turno) {
        this.turno = 1-turno;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int qqBomba()
    {
        if(getTurno()==0)
        {
            int count = 0;
            for(int i=0;i<4;i++)
            {
                if(baralho[0][i]>=4)
                {
                    count=count+1;
                }
            }
            return count;
        }
        else
        {
            int count = 0;
            for(int i=0;i<4;i++)
            {
                if(baralho[1][i]>=4)
                {
                    count=count+1;
                }
            }
            return count;
        }
    }
}
