/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.pedro.model;

import com.google.gson.annotations.Expose;

import ies.pedro.utils.Point;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class Block implements  Serializable {
    @Expose
    private String type;
    private Point point;

    public Block(){
        this.type=null;
    }
    public Block(String type,Point point){
        this.type=type;
        this.point=point;
    }
    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type=type;
    }
    public boolean isEmpty(){
        return this.type==null;
    }
    
    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }
    @Override
    public String toString(){
        return this.type+" "+this.point;
    }
}
