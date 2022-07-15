package com.mrtechno.crazytictacpo;

import android.widget.Switch;

import java.util.function.ToDoubleBiFunction;

public class SmartClass
{
    private int tabuleiro[][] = new int[3][3];
    private int winner = -1;
    private String end = "";
    private boolean change = false;
    private boolean status = true;

    //DEFINIÇÕES
    // 0: TABULEIRO VAZIO
    // 1: TESOURA
    // 2: PEDRA
    // 3: PAPEL

    // 4: BOMBA BRANCA
    // 5: BOMBA PRETA
    // 6: BOMBA VERMELHA
    // 7: BOMBA VERDE
    // 8: BOMBA AZUL
    // +50: OPONENTE

    //PRE SET
    public SmartClass() {
        inicializar();
    }

    public int userMove (int regiao, int objeto)
    {
        int linha =  (0);
        int coluna = (0);
        int resposta=0;
        int valor = objeto;
        switch (regiao) {
            case 11:
                linha = 0;
                coluna = 0;
                break;
            case 12:
                linha = 0;
                coluna = 1;
                break;
            case 13:
                linha = 0;
                coluna = 2;
                break;
            case 21:
                linha = 1;
                coluna = 0;
                break;
            case 22:
                linha = 1;
                coluna = 1;
                break;
            case 23:
                linha = 1;
                coluna = 2;
                break;
            case 31:
                linha = 2;
                coluna = 0;
                break;
            case 32:
                linha = 2;
                coluna = 1;
                break;
            case 33:
                linha = 2;
                coluna = 2;
                break;
        }

        //EFETUAR TRATAMENTO DO TABULEIRO
        if((objeto) > 3)
        {
            //É UMA BOMBA: DEVE-SE EFETUAR
            switch (objeto)
            {
                case 4:
                    whiteExplosion(linha, coluna);
                    break;
                case 5:
                    blackExplosion(linha, coluna);
                    break;
                case 6:
                    redExplosion(linha, coluna);
                    break;
                case 7:
                    blueExplosion(linha, coluna);
                    break;
                case 8:
                    greenExplosion(linha, coluna);
                    break;
            }
        }
        else
        {
            setChange( false );
            duelo(linha, coluna, valor);
            if(getChange( ))
            {
                if(endGameCheck(getTabuleiroM()))
                {
                    setStatus( false );
                }
            }
        }
        return 420;
    }

    public void inicializar()
    {
        for(int i =0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                setTabuleiro( i, j, 0 );
            }
        }
    }

    public boolean endGameCheck(int[][] tabuleiro)
    {
        //CHECAR LINHAS
        for(int i=0;i<3;i++)
        {
            if ((tabuleiro[i][0] * tabuleiro[i][1] * tabuleiro[i][2]) > 0) {
                if ((tabuleiro[i][0] == tabuleiro[i][1]) && (tabuleiro[i][1] == tabuleiro[i][2])) {
                    if (tabuleiro[i][0] < 50) {
                        setWinner( 10 );
                    } else {
                        setWinner( 50 );
                    }
                    setEnd( "Linha " + i );
                    return true;
                }
            }
        }
        //CHECAR COLUNAS
        for(int j=0;j<3;j++)
        {
            if((tabuleiro[0][j]*tabuleiro[1][j]*tabuleiro[2][j])>0)
            {
                if((tabuleiro[0][j]==tabuleiro[1][j]) &&(tabuleiro[1][j]==tabuleiro[2][j]))
                {
                    if(tabuleiro[0][j]<50)
                    {
                        setWinner(10);
                    }
                    else
                    {
                        setWinner( 50 );
                    }
                    setEnd( "Coluna " + j );
                    return true;
                }
            }
        }
        //CHECAR DIAGONAL PRINCIPAL
        if((tabuleiro[0][0]*tabuleiro[1][1]*tabuleiro[2][2])>0)
        {
            if((tabuleiro[0][0]==tabuleiro[1][1]) &&(tabuleiro[1][1]==tabuleiro[2][2]))
            {
                if(tabuleiro[0][0]<50)
                {
                    setWinner(10);
                }
                else
                {
                    setWinner( 50 );
                }
                setEnd( "Diagonal Principal");
                return true;
            }
        }
        //CHECAR DIAGONAL SECUNDÁRIA
        if((tabuleiro[0][2]*tabuleiro[1][1]*tabuleiro[2][0])>0)
        {
            if((tabuleiro[0][2]==tabuleiro[1][1]) &&(tabuleiro[1][1]==tabuleiro[2][0]))
            {
                if(tabuleiro[2][0]<50)
                {
                    setWinner(10);
                }
                else
                {
                    setWinner( 50 );
                }
                setEnd( "Diagonal Secundária");
                return true;
            }
        }
        return false;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getChange() {
        return change;
    }

    private void setWinner(int winner) {
        winner = winner;
    }

    public void duelo (int linha, int coluna, int valor)
    {
        if(getTabuleiro(linha, coluna)==0)
        {
            //Inserir peça no tabuleiro
            setTabuleiro( linha, coluna, valor );
            setChange( true );
        }
        else
        {
            //DEFINIÇÕES
            // 0: TABULEIRO VAZIO
            // 1: TESOURA
            // 2: PEDRA
            // 3: PAPEL

            //INSERE-SE UMA TESOURA EM UM PAPEL
            if((valor%10)==1 && getTabuleiro( linha, coluna )%10==3)
            {
                setTabuleiro( linha, coluna, valor );
                setChange( true );
            }

            //INSERE-SE UMA PEDRA EM UMA TESOURA
            if((valor%10)==2 && getTabuleiro( linha, coluna )%10==1)
            {
                setTabuleiro( linha, coluna, valor );
                setChange( true );
            }

            //INSERE-SE UM PAPEL EM UMA PEDRA
            if((valor%10)==3 && getTabuleiro( linha, coluna )%10==2)
            {
                setTabuleiro( linha, coluna, valor );
                setChange( true );
            }
        }

    }

    //EXPLOSIONS
    public void whiteExplosion(int linha, int coluna)
    {
    if(linha==0 && coluna==0)
    {
        setTabuleiro( 0, 0, 0 );
        setTabuleiro( 1, 0, 0 );
        setTabuleiro( 1, 1, 0 );
        setTabuleiro( 0, 1, 0 );
    }
    if(linha==0 && coluna==1)
    {
        setTabuleiro( 0, 0, 0 );
        setTabuleiro( 0, 1, 0 );
        setTabuleiro( 0, 2, 0 );
        setTabuleiro( 1, 0, 0 );
        setTabuleiro( 1, 1, 0 );
        setTabuleiro( 1, 2, 0 );
    }
    if(linha==0 && coluna==2)
    {
        setTabuleiro( 0, 1, 0 );
        setTabuleiro( 0, 2, 0 );
        setTabuleiro( 1, 2, 0 );
        setTabuleiro( 1, 1, 0 );
    }
    if(linha==1 && coluna==0)
    {
        setTabuleiro( 0, 0, 0 );
        setTabuleiro( 0, 1, 0 );
        setTabuleiro( 1, 0, 0 );
        setTabuleiro( 1, 1, 0 );
        setTabuleiro( 2, 0, 0 );
        setTabuleiro( 2, 1, 0 );
    }
    if(linha==1 && coluna==1)
    {
        inicializar( );
    }
    if(linha==1 && coluna==2)
    {
        setTabuleiro( 0, 1, 0 );
        setTabuleiro( 0, 2, 0 );
        setTabuleiro( 1, 1, 0 );
        setTabuleiro( 1, 2, 0 );
        setTabuleiro( 2, 1, 0 );
        setTabuleiro( 2, 2, 0 );
    }
    if(linha==2 && coluna==0)
    {
        setTabuleiro( 1, 1, 0 );
        setTabuleiro( 1, 0, 0 );
        setTabuleiro( 2, 0, 0 );
        setTabuleiro( 2, 1, 0 );
    }
    if(linha==2 && coluna==1)
    {
        setTabuleiro( 1, 0, 0 );
        setTabuleiro( 1, 1, 0 );
        setTabuleiro( 1, 2, 0 );
        setTabuleiro( 2, 0, 0 );
        setTabuleiro( 2, 1, 0 );
        setTabuleiro( 2, 2, 0 );
    }
    if(linha==2 && coluna==2)
    {
        setTabuleiro( 1, 1, 0 );
        setTabuleiro( 1, 2, 0 );
        setTabuleiro( 2, 1, 0 );
        setTabuleiro( 2, 2, 0 );
    }
}

    public void blackExplosion(int linha, int coluna)
    {
        if(linha==0 && coluna==0)
        {
            setTabuleiro( 0, 0, 0 );
        }
        if(linha==0 && coluna==1)
        {
            setTabuleiro( 0, 1, 0 );
        }
        if(linha==0 && coluna==2)
        {
            setTabuleiro( 0, 2, 0 );
        }
        if(linha==1 && coluna==0)
        {
            setTabuleiro( 1, 0, 0 );
        }
        if(linha==1 && coluna==1)
        {
            setTabuleiro( 1, 1, 0 );
        }
        if(linha==1 && coluna==2)
        {
            setTabuleiro( 1, 2, 0 );
        }
        if(linha==2 && coluna==0)
        {
            setTabuleiro( 2, 0, 0 );
        }
        if(linha==2 && coluna==1)
        {
            setTabuleiro( 2, 1, 0 );
        }
        if(linha==2 && coluna==2)
        {
            setTabuleiro( 2, 2, 0 );
        }
    }

    public void redExplosion(int linha, int coluna)
    {
        if(linha==0 && coluna==0)
        {
            setLinhaTabuleiro( 0, 0 );
            setColunaTabuleiro( 0, 0 );
        }
        if(linha==0 && coluna==1)
        {
            setLinhaTabuleiro( 0, 0 );
            setColunaTabuleiro( 1, 0 );
        }
        if(linha==0 && coluna==2)
        {
            setLinhaTabuleiro( 0, 0 );
            setColunaTabuleiro( 2, 0 );
        }
        if(linha==1 && coluna==0)
        {
            setLinhaTabuleiro( 1, 0 );
            setColunaTabuleiro( 0, 0 );
        }
        if(linha==1 && coluna==1)
        {
            setLinhaTabuleiro( 1, 0 );
            setColunaTabuleiro( 1, 0 );
        }
        if(linha==1 && coluna==2)
        {
            setLinhaTabuleiro( 1, 0 );
            setColunaTabuleiro( 2, 0 );
        }
        if(linha==2 && coluna==0)
        {
            setLinhaTabuleiro( 2, 0 );
            setColunaTabuleiro( 0, 0 );
        }
        if(linha==2 && coluna==1)
        {
            setLinhaTabuleiro( 2, 0 );
            setColunaTabuleiro( 1, 0 );
        }
        if(linha==2 && coluna==2)
        {
            setLinhaTabuleiro( 2, 0 );
            setColunaTabuleiro( 2, 0 );
        }
    }

    public void blueExplosion(int linha, int coluna)
    {
        if(linha==0 && coluna==0)
        {
            setTabuleiro( 0, 0, 0 );
            setTabuleiro( 1, 1, 0 );
            setTabuleiro( 2, 2, 0 );
        }
        if(linha==0 && coluna==1)
        {
            setTabuleiro( 0, 1, 0 );
            setTabuleiro( 1, 0, 0 );
            setTabuleiro( 1, 2, 0 );
        }
        if(linha==0 && coluna==2)
        {
            setTabuleiro( 0, 2, 0 );
            setTabuleiro( 1, 1, 0 );
            setTabuleiro( 2, 0, 0 );
        }
        if(linha==1 && coluna==0)
        {
            setTabuleiro( 0, 1, 0 );
            setTabuleiro( 1, 0, 0 );
            setTabuleiro( 2, 1, 0 );
        }
        if(linha==1 && coluna==1)
        {
            setTabuleiro( 0, 0, 0 );
            setTabuleiro( 1, 1, 0 );
            setTabuleiro( 2, 2, 0 );
            setTabuleiro( 0, 2, 0 );
            setTabuleiro( 2, 0, 0 );
        }
        if(linha==1 && coluna==2)
        {
            setTabuleiro( 0, 1, 0 );
            setTabuleiro( 1, 2, 0 );
            setTabuleiro( 2, 1, 0 );
        }
        if(linha==2 && coluna==0)
        {
            setTabuleiro( 0, 2, 0 );
            setTabuleiro( 1, 1, 0 );
            setTabuleiro( 2, 0, 0 );
        }
        if(linha==2 && coluna==1)
        {
            setTabuleiro( 1, 0, 0 );
            setTabuleiro( 1, 2, 0 );
            setTabuleiro( 2, 1, 0 );
        }
        if(linha==2 && coluna==2)
        {
            setTabuleiro( 0, 0, 0 );
            setTabuleiro( 1, 1, 0 );
            setTabuleiro( 2, 2, 0 );
        }
    }

    public void greenExplosion(int linha, int coluna)
    {
        //TODO
    }

    private void setLinhaTabuleiro(int linha, int valor)
    {
        for (int coluna = 0; coluna < 3; coluna++) {
            this.tabuleiro[linha][coluna] = valor;

        }
    }

    private void setColunaTabuleiro(int coluna, int valor)
    {
        for (int linha = 0; linha < 3; linha++) {
            this.tabuleiro[linha][coluna] = valor;

        }
    }

    private void setTabuleiro(int linha, int coluna, int valor)
    {
        this.tabuleiro[linha][coluna] = valor;
    }

    public int getTabuleiro(int linha, int coluna)
    {
        return tabuleiro[linha][coluna];
    }

    public int[][] getTabuleiroM() {
        return tabuleiro;
    }

    public int getTabuleiroP(int posic) {
        switch (posic)
        {
            case 11:
                return tabuleiro[0][0];
            case 12:
                return tabuleiro[0][1];
            case 13:
                return tabuleiro[0][2];
            case 21:
                return tabuleiro[1][0];
            case 22:
                return tabuleiro[1][1];
            case 23:
                return tabuleiro[1][2];
            case 31:
                return tabuleiro[2][0];
            case 32:
                return tabuleiro[2][1];
            case 33:
                return tabuleiro[2][2];
            default:
                return 0;
        }
    }
}
