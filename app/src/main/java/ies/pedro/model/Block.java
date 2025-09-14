/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.pedro.model;

import com.google.gson.annotations.Expose;

import ies.pedro.utils.Point;
import jakarta.xml.bind.annotation.XmlRootElement;

import javafx.geometry.Rectangle2D;
import java.io.Serializable;


@XmlRootElement
public class Block implements  Serializable {
    @Expose
    private String type;




    private Rectangle2D rectangle;
    public Block(){
        this.type=null;
    }

    public Block(String type,Rectangle2D rectangle){
        this.type=type;
        this.rectangle=rectangle;
        System.out.println(rectangle);
    }
    public Rectangle2D getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle2D rectangle) {
        this.rectangle = rectangle;
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
    

    @Override
    public String toString(){
        return this.type+" "+this.rectangle;
    }
}
