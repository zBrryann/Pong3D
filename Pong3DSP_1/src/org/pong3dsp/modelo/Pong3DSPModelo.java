/*
 * Pong3DSPModelo.java
 *
 * Created on 24 de octubre de 2006, 19:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pong3dsp.modelo;

/**
 *
 * @author Administrador
 */
public class Pong3DSPModelo {

    public static final int DERECHA=0;
    public static final int IZQUIERDA=1;
    public static final int ARRIBA=2;
    public static final int ABAJO=3;
    
    private double xB, yB, zB;
    private double dx, dy, dz;
    private int nEstado; //0 Jugando, 1 Win, 2 Lose
    
    private boolean teclas[]=new boolean[4];
    private double x[]=new double[2];
    private double y[]=new double[2];
    
    public void setTecla(int nTecla, boolean bValor){
        if (nTecla>=0 && nTecla<=3){
            teclas[nTecla]=bValor;
        }
    }
    
    public double getValorLimites(double valor, double min, double max){
        if (min>valor) return min;
        if (max<valor) return max;
        return valor;
    }
    
    private void moverRaqueta(int nPos, double dx, double dy){
        double nLim=0.7;
        x[nPos]=getValorLimites(x[nPos]+dx, -nLim, nLim);
        y[nPos]=getValorLimites(y[nPos]+dy, -nLim, nLim);
    }
    
    private boolean contactaRaquetaConBola(int nRaq){
        return (xB>x[nRaq]-0.3) && (xB<x[nRaq]+0.3) && (yB>y[nRaq]-0.3) && (yB<y[nRaq]+0.3);
    }
    
    private void golpearConRaqueta(int nRaq, double nLim){
        zB=nLim;
        dz=-dz;
        dx=xB-x[nRaq];
        dy=yB-y[nRaq];
    }
    
    public void empezarPartida(){
        dz=-0.1;
        dx=Math.random()*0.2-0.1;
        dy=Math.random()*0.2-0.1;
        x[0]=y[0]=x[1]=y[1]=0;
        zB=yB=xB=0;
        nEstado=0;
    }
    
    public void mover(){
        if (nEstado!=0) return;
        
        double nLim=0.9;
        xB=getValorLimites(xB+dx,-nLim, nLim);
        yB=getValorLimites(yB+dy,-nLim, nLim);
        if (xB==-nLim || xB==nLim) dx=-dx;
        if (yB==-nLim || yB==nLim) dy=-dy;
        zB+=dz;
        if (zB<-nLim && contactaRaquetaConBola(0))
            golpearConRaqueta(0, -nLim);
        if (zB>nLim && contactaRaquetaConBola(1))
            golpearConRaqueta(1, nLim);
        if (zB<-nLim-0.2) nEstado=1;
        if (zB>nLim+0.2) nEstado=2;
        
        double nVel=0.2;
        double dx=0,dy=0;
        
        if (Math.abs(xB-x[0])>0.1) dx=nVel*Math.signum(xB-x[0]);
        if (Math.abs(yB-y[0])>0.1) dy=nVel*Math.signum(yB-y[0]);
        
        moverRaqueta(0, dx, dy);
        
        dx=dy=0;
        if (teclas[DERECHA]) dx+=nVel;
        if (teclas[IZQUIERDA]) dx-=nVel;
        if (teclas[ARRIBA]) dy+=nVel;
        if (teclas[ABAJO]) dy-=nVel;
        moverRaqueta(1,dx,dy);
    }
    
    public double getXJugador(){
        return x[1];
    }
    
    public double getYJugador(){
        return y[1];
    }
    
    public double getXContrario(){
        return x[0];
    }
    
    public double getYContrario(){
        return y[0];
    }    
    
    public double getXBola(){
        return xB;
    }
    
    public double getYBola(){
        return yB;
    }

    public double getZBola(){
        return zB;
    }
    
    public int getEstado(){
        return nEstado;
    }

}
