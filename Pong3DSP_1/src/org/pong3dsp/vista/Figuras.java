/*
 * Figuras.java
 *
 * Created on 24 de octubre de 2006, 23:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pong3dsp.vista;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;

/**
 *
 * @author Administrador
 */
public class Figuras {

    public static TransformGroup getLuz(){
        TransformGroup tg=new TransformGroup();
        AmbientLight al=new AmbientLight(new Color3f(0.5f, 0.5f, 0.5f));
        al.setInfluencingBounds(new BoundingSphere(new Point3d(),10));
        tg.addChild(al);
        
        PointLight pl=new PointLight(new Color3f(1,1,1), new Point3f(0,0,2), new Point3f(0.1f,0.1f,0.1f));
        pl.setInfluencingBounds(new BoundingSphere(new Point3d(),10));
        tg.addChild(pl);
        
        return tg;
    }
    
    private static Appearance getApararienciaColor(float r, float v, float a){
        Appearance app=new Appearance();
        Material mat=new Material();
        mat.setAmbientColor(r,v,a);
        mat.setDiffuseColor(r,v,a);
        mat.setSpecularColor(1,1,1);
        app.setMaterial(mat);
        return app;
    }
    
    public static TransformGroup getBola(){
        TransformGroup tg=new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(new Sphere(0.05f, getApararienciaColor(0,1,0)));
        
        PointLight pl=new PointLight(new Color3f(0,1,0), new Point3f(0,0,0), new Point3f(0.2f,0.2f,0.2f));
        pl.setInfluencingBounds(new BoundingSphere(new Point3d(),10));
        tg.addChild(pl);
        
        return tg;
        
    }
    
    public static TransformGroup getContrario(){
        TransformGroup tg=new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(new Box(0.3f, 0.3f, 0.01f, getApararienciaColor(1,1,0)));
        return tg;
    }
    
    public static TransformGroup getTexto(String sTexto){
        TransformGroup tg=new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(new Text2D(sTexto, new Color3f(0,1,1), "Times", 32, 0));
        return tg;
    }
    
    public static TransformGroup getCampo(){
        TransformGroup tg=new TransformGroup();
        
        Appearance app[]={
            getApararienciaColor(0,0.2f,1),
            getApararienciaColor(1,0.2f,0),
            getApararienciaColor(0,0,1),
            getApararienciaColor(1,0,0)
        };
        
        for (int n=0;n<4;n++){
            Transform3D tAux=new Transform3D();
            tAux.rotZ(Math.PI/2*n);
            TransformGroup tgAux=new TransformGroup(tAux);
            tg.addChild(tgAux);
            
            Point3d coords[]=new Point3d[11*2];
            Vector3f normal[]=new Vector3f[11*2];
            for (int m=0;m<11;m++){
                coords[m*2]=new Point3d(-1,1,2-m);
                coords[m*2+1]=new Point3d(-1,-1,2-m);
                normal[m*2]=new Vector3f(1,-1,0);
                normal[m*2+1]=new Vector3f(1,1,0);
            }
            
            int vertices[]={coords.length-2};
            TriangleStripArray geometria=new TriangleStripArray(
                    coords.length,
                    GeometryArray.COORDINATES|GeometryArray.NORMALS,
                    vertices
            );
            geometria.setCoordinates(0,coords);
            geometria.setNormals(0,normal);
            
            Shape3D figura=new Shape3D(geometria, app[n]);
            tgAux.addChild(figura);
        }
        return tg;
    }
    
}
