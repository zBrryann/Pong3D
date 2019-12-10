/*
 * Pong3DSPVista.java
 *
 * Created on 23 de octubre de 2006, 22:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pong3dsp.vista;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pong3dsp.modelo.Pong3DSPModelo;

import com.sun.j3d.utils.universe.SimpleUniverse;
import pong3dsp.Main;

/**
 *
 * @author Administrador
 */
public class Pong3DSPVista extends Applet implements Runnable{

    Pong3DSPModelo modelo=new Pong3DSPModelo();
    TransformGroup tgGeneral=new TransformGroup();
    TransformGroup tgContrario, tgBola, tgWin, tgLose;
    private ArrayList<Controller> foundControllers;
    private javax.swing.JComboBox  jComboBox_controllers;
   int tiempo=50;
    /** Creates a new instance of Pong3DSPVista */
    public Pong3DSPVista() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config=SimpleUniverse.getPreferredConfiguration();
        Canvas3D c=new Canvas3D(config);
        add("Center", c);
        SimpleUniverse u = new SimpleUniverse(c);
        u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(crearEscena());
         Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
        jComboBox_controllers = new javax.swing.JComboBox();
        foundControllers=new ArrayList<Controller>();
        searchForControllers();
        c.addKeyListener(
                new KeyAdapter(){
                    public void keyPressed(KeyEvent keyEvent){
                        if(keyEvent.getKeyCode()==KeyEvent.VK_ENTER)
                            modelo.empezarPartida();
                        modelo.setTecla(modelo.IZQUIERDA, false);
                            modelo.setTecla(modelo.DERECHA, false);
                            modelo.setTecla(modelo.ARRIBA, false);
                            modelo.setTecla(modelo.ABAJO, false);
                    }
                }
        );
        
        c.addKeyListener(  
                new KeyAdapter() {
                    public void keyPressed(KeyEvent keyEvent){
                        if (keyEvent.getKeyCode()==KeyEvent.VK_ENTER)
                            modelo.empezarPartida();
                        else
                            accionTecla(keyEvent.getKeyCode(),true);
                    }
                    public void keyReleased(KeyEvent keyEvent){
                        accionTecla(keyEvent.getKeyCode(),false);
                    }
                    
                    private void accionTecla(int tecla, boolean bValor){
                        switch(tecla){
                            case KeyEvent.VK_LEFT:
                                modelo.setTecla(modelo.IZQUIERDA, bValor);
                                System.out.println("m"+modelo.IZQUIERDA);
                                break;
                            case KeyEvent.VK_RIGHT:
                                modelo.setTecla(modelo.DERECHA, bValor);
                                break;
                            case KeyEvent.VK_UP:
                                modelo.setTecla(modelo.ARRIBA, bValor);
                                break;
                            case KeyEvent.VK_DOWN:
                                modelo.setTecla(modelo.ABAJO, bValor);
                                break;
                        }
                    }
                }
        );
        modelo.empezarPartida();
        
      
        Thread hilo=new Thread(this);
        
        hilo.start();
    }
    private void searchForControllers(){
        Controller[]controllers=ControllerEnvironment.getDefaultEnvironment().getControllers();
        for(int i=0;i<controllers.length;i++){
            Controller controller=controllers[i];
            if(controller.getType() == Controller.Type.STICK
                    || controller.getType() == Controller.Type.GAMEPAD
                    || controller.getType() == Controller.Type.WHEEL
                    || controller.getType() == Controller.Type.FINGERSTICK){
                foundControllers.add(controller);
                System.out.println("Fue Detectado");
                addControllerName(controller.getName()+"-"+controller.getType().toString()+"type");
            }
        }
    }
    public void addControllerName(String controllerName){
        jComboBox_controllers.addItem(controllerName);
    }
    public int getSelectedControllerName(){
        return jComboBox_controllers.getSelectedIndex();
    }
    public void showControllerDisconnected(){
        jComboBox_controllers.removeAllItems();
        jComboBox_controllers.addItem("Controller disconnected!");
    }
    
    private void startShowingControllerData(){
           
            int selectedControllerIndex=getSelectedControllerName();
            Controller controller=foundControllers.get(selectedControllerIndex);
            if(!controller.poll()){
                showControllerDisconnected();
                
            }
            int xAxisPercentage = 0;
            int yAxisPercentage = 0;
            Component[]components  =controller.getComponents();
            for(int i=0;i<components.length;i++){
                Component component=components[i];
                Identifier componentIdentifier=component.getIdentifier();
                
                if(component.isAnalog()){
                    float axisValue=component.getPollData();
                    int axisValueInPercentage=getAxisValueInPercentage(axisValue);  
                    if(componentIdentifier==Component.Identifier.Axis.X){
                        xAxisPercentage=axisValueInPercentage;
                        if(xAxisPercentage==0){
                            modelo.setTecla(modelo.IZQUIERDA, true);
                            modelo.setTecla(modelo.DERECHA, false);
                            modelo.setTecla(modelo.ARRIBA, false);
                            modelo.setTecla(modelo.ABAJO, false);
                        }
                        if(xAxisPercentage==100){
                            modelo.setTecla(modelo.DERECHA, true);
                            modelo.setTecla(modelo.IZQUIERDA, false);
                            modelo.setTecla(modelo.ARRIBA, false);
                            modelo.setTecla(modelo.ABAJO, false);
                        }
                        continue;
                    }
                    if (componentIdentifier == Component.Identifier.Axis.Y) {
                        // xAxisPercentage es un auxiliar para obtener el valor porcentual de Y.
                        yAxisPercentage = axisValueInPercentage;
                        
                        if (yAxisPercentage == 0) {
                            // si el valor es 0, el boton presionado es el boton arriba
                            modelo.setTecla(modelo.ARRIBA, true);
                            modelo.setTecla(modelo.ABAJO, false);
                            modelo.setTecla(modelo.DERECHA, false);
                            modelo.setTecla(modelo.IZQUIERDA, false);
                            // usamos el metodo arriba() de tab para rotar la pieza
 
                        }
                        if (yAxisPercentage == 100) {
                            // si el valor es 100, el boton presionado es el boton abajo
                            modelo.setTecla(modelo.ABAJO, true);
                             modelo.setTecla(modelo.ARRIBA, false);
                             modelo.setTecla(modelo.DERECHA, false);
                            modelo.setTecla(modelo.IZQUIERDA, false);
                            // usamos el metodo abajo() de tab para bajar rapido la pieza

                        }
                        continue; // Vamos al siguiente componente.
                    }
                }
            }
             try{
                Thread.sleep(50);
            }catch(InterruptedException ex){
                Logger.getLogger(Pong3DSPVista.class.getName()).log(Level.SEVERE,null,ex);
            }
           
        
    }
    public int getAxisValueInPercentage(float axisValue) {
        return (int) (((2 - (1 - axisValue)) * 100) / 2);
    } 
    
    
    public BranchGroup crearEscena(){
        BranchGroup bg=new BranchGroup();
        
        tgGeneral.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        bg.addChild(tgGeneral);
        
        tgGeneral.addChild(Figuras.getCampo());
        tgGeneral.addChild(tgContrario=Figuras.getContrario());
        tgGeneral.addChild(tgBola=Figuras.getBola());
        tgGeneral.addChild(Figuras.getLuz());
        bg.addChild(tgWin=Figuras.getTexto("YOU WIN"));
        bg.addChild(tgLose=Figuras.getTexto("YOU LOSE"));
        return bg;
    }
    
    private void posicionar(){
        //Posicionamiento general
        double dGeneral[]={-modelo.getXJugador(), -modelo.getYJugador(), 0};
        Transform3D tGeneral=new Transform3D();
        tGeneral.set(new Vector3d(dGeneral));
        tgGeneral.setTransform(tGeneral);
        //Posicionamiento contrario
        double dContrario[]={modelo.getXContrario(), modelo.getYContrario(), -8};
        Transform3D tContrario=new Transform3D();
        tContrario.set(new Vector3d(dContrario));
        tgContrario.setTransform(tContrario);
        //Posicionamiento bola
        double dBola[]={modelo.getXBola(), modelo.getYBola(), modelo.getZBola()*5-3};
        Transform3D tBola=new Transform3D();
        tBola.set(new Vector3d(dBola));
        tgBola.setTransform(tBola);
        
        //Mostrar textos
        int nEstado=modelo.getEstado();
        Vector3d v;
        Transform3D t=new Transform3D();
        //Gana
        if (nEstado==1) v=new Vector3d(-0.3f,0,1f);
        else v=new Vector3d(0,100,0);
        t.set(v);
        tgWin.setTransform(t);
        //Lose
        if (nEstado==2) v=new Vector3d(-0.3f,0,1f);
        else v=new Vector3d(0,100,0);
        t.set(v);
        tgLose.setTransform(t);
    }
    
    public void run(){
        while(true){
            try{
                Thread.sleep(30);
                posicionar();
                modelo.mover();
                startShowingControllerData();
               
            }catch(Exception e){}
        }
    }

}
