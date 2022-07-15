package com.mrtechno.crazytictacpo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

public class BoardClass extends View
{
    public int numColumns;
    public int numRows;
    private Paint paint;
    private int largura;
    private int altura;
    private int size;
    private int xcenter;
    private int ycenter;
    private int delta;
    private int e;
    private float[][]cartesianoEXT;
    private float[][]cartesianoINT;
    private float xstart;
    private float ystart;

    //MÉTODOS
    public BoardClass(Context context) {
        super( context );
        init(null);
    }
    public BoardClass (Context context, @Nullable AttributeSet attrs)     {
        super(context, attrs);
        init( attrs );
    }
    private void init(AttributeSet attrs)
    {
        setDimen();
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw( canvas );
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setDimen();

        //RETÂNGULOS EXTERNOS
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                paint.setColor( Color.DKGRAY );
                canvas.drawRect(
                        cartesianoEXT[0][0+col],
                        cartesianoEXT[1][0+row],
                        cartesianoEXT[0][1+col],
                        cartesianoEXT[1][1+row], paint);


                paint.setColor( Color.WHITE );
                canvas.drawRect(
                        cartesianoINT[0][0+2*col],
                        cartesianoINT[1][0+2*row],
                        cartesianoINT[0][1+2*col],
                        cartesianoINT[1][1+2*row], paint);
            }
        }
    }
    public void setDimen ()    {

        //configuração do tabuleiro
        numColumns = 3;
        numRows = 3;

        //Definindo dimensões
        largura = getWidth();
        altura = getHeight();
        size = (int) Math.min( largura, altura );

        //Ponto central
        xcenter = largura / 2;
        ycenter = altura / 2;
        e = 8;
        delta = (size-2*e) / 3;

        //DEFINIR COORDENADAS EXTERNAS
        cartesianoEXT = new float[2][4];
        //eixo x
        cartesianoEXT[0][0]= xcenter-3*delta/2-e;
        cartesianoEXT[0][1]= xcenter-delta/2-e/2;
        cartesianoEXT[0][2]= xcenter+delta/2+e/2;
        cartesianoEXT[0][3]= xcenter+3*delta/2+e;
        //eixo y
        cartesianoEXT[1][0]= ycenter-3*delta/2-e;
        cartesianoEXT[1][1]= ycenter-delta/2-e/2;
        cartesianoEXT[1][2]= ycenter+delta/2+e/2;
        cartesianoEXT[1][3]= ycenter+3*delta/2+e;


        //DEFINIR COORDENADAS INTERNAS
        cartesianoINT = new float [2][6];
        //eixo x
        cartesianoINT[0][0]= xcenter-3*delta/2-e;
        cartesianoINT[0][1]= xcenter-delta/2-e;
        cartesianoINT[0][2]= xcenter-delta/2;
        cartesianoINT[0][3]= xcenter+delta/2;
        cartesianoINT[0][4]= xcenter+delta/2+e;
        cartesianoINT[0][5]= xcenter+3*delta/2+e;
        //eixo y
        cartesianoINT[1][0]= ycenter-3*delta/2-e;
        cartesianoINT[1][1]= ycenter-delta/2-e;
        cartesianoINT[1][2]= ycenter-delta/2;
        cartesianoINT[1][3]= ycenter+delta/2;
        cartesianoINT[1][4]= ycenter+delta/2+e;
        cartesianoINT[1][5]= ycenter+3*delta/2+e;

        xstart=0+cartesianoINT[0][0];
        ystart = 0+cartesianoINT[1][0];


    }
    public int getDelta()
    {
        return delta;
    }
    public float xcenter(int posic)    {
        switch (posic)
        {
            case 11:
                return (cartesianoINT[0][0]+cartesianoINT[0][1])/2;
            case 21:
                return (cartesianoINT[0][0]+cartesianoINT[0][1])/2;
            case 31:
                return (cartesianoINT[0][0]+cartesianoINT[0][1])/2;
            case 12:
                return (cartesianoINT[0][2]+cartesianoINT[0][3])/2;
            case 22:
                return (cartesianoINT[0][2]+cartesianoINT[0][3])/2;
            case 32:
                return (cartesianoINT[0][2]+cartesianoINT[0][3])/2;
            case 13:
                return (cartesianoINT[0][4]+cartesianoINT[0][5])/2;
            case 23:
                return (cartesianoINT[0][4]+cartesianoINT[0][5])/2;
            case 33:
                return (cartesianoINT[0][4]+cartesianoINT[0][5])/2;
            default:
                return 0.0f;
        }
    }
    public float getYCenter(int posic)    {
        switch (posic)
        {
            case 11:
                return (cartesianoINT[1][0]+cartesianoINT[1][1])/2;
            case 12:
                return (cartesianoINT[1][0]+cartesianoINT[1][1])/2;
            case 13:
                return (cartesianoINT[1][0]+cartesianoINT[1][1])/2;
            case 21:
                return (cartesianoINT[1][2]+cartesianoINT[1][3])/2;
            case 22:
                return (cartesianoINT[1][2]+cartesianoINT[1][3])/2;
            case 23:
                return (cartesianoINT[1][2]+cartesianoINT[1][3])/2;
            case 31:
                return (cartesianoINT[1][4]+cartesianoINT[1][5])/2;
            case 32:
                return (cartesianoINT[1][4]+cartesianoINT[1][5])/2;
            case 33:
                return (cartesianoINT[1][4]+cartesianoINT[1][5])/2;
            default:
                return 0.0f;
        }
    }
    public int getE()
    {
        return e;
    }
    public float getXstart() {
        return xstart;
    }
    public float getYstart() {
        return ystart;
    }
    public int whereIsIT(float x, float y)    {
        if(x>cartesianoINT[0][0]+10 && x<=cartesianoEXT[0][1] && y>cartesianoINT[1][0]+10 && y<=cartesianoEXT[1][1])
            return 11;
        else if(x>cartesianoEXT[0][1] && x<=cartesianoEXT[0][2] && y>cartesianoINT[1][0]+10 && y<=cartesianoEXT[1][1])
            return 12;
        else if(x>cartesianoEXT[0][2] && x<cartesianoINT[0][5]-10 && y>cartesianoINT[1][0]+10 && y<=cartesianoEXT[1][1])
            return 13;
        else if(x>cartesianoINT[0][0]+10 && x<=cartesianoEXT[0][1] && y>cartesianoEXT[1][1] && y<=cartesianoEXT[1][2])
            return 21;
        else if(x>cartesianoEXT[0][1]&& x<=cartesianoEXT[0][2] && y>cartesianoEXT[1][1] && y<=cartesianoEXT[1][2])
            return 22;
        else if(x>cartesianoEXT[0][2] && x<cartesianoINT[0][5]-10 && y>cartesianoEXT[1][1] && y<=cartesianoEXT[1][2])
            return 23;
        else if(x>cartesianoINT[0][0]+10 && x<cartesianoEXT[0][1] && y>cartesianoEXT[1][2] && y<cartesianoINT[1][5]-10)
            return 31;
        else if(x>cartesianoEXT[0][1] && x<cartesianoEXT[0][2] && y>cartesianoEXT[1][2] && y<cartesianoINT[1][5]-10)
            return 32;
        else if(x>cartesianoEXT[0][2] && x<cartesianoINT[0][5]-10 && y>cartesianoEXT[1][2] && y<cartesianoINT[1][5]-10)
            return 33;
        else
            return 0;
    }
}
