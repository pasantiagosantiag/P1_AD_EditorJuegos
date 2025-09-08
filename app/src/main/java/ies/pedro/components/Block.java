/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.pedro.components;

import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import ies.pedro.App;
import ies.pedro.utils.Size;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;




/**
 *
 * @author Pedro
 */
public class Block {

    //private Rectangle r;
    private ImageView imgv;
    private Pane panel;
   
    private boolean selected;
    private Size size;
    private static Image img;
    private static Size img_block_size;
    private static Map<String, Rectangle2D> imgs_coordenadas;
    private String type;
    private ArrayList<IBlockListener> blocklisteners;

    static {
        try {

            int w = App.CELLWIDTH;
            int h = App.CELLHEIGHT;
            Size img_block_size = new Size(w, h);

            //img = new Image(Block.class.getResource("/blocks.png").toURI().toString());
            img = new Image(Block.class.getResource("/bloques.png").toURI().toString());

            imgs_coordenadas = new HashMap<>();
            imgs_coordenadas.put("White", new Rectangle2D(0, 0, w, h));
            imgs_coordenadas.put("Orange", new Rectangle2D(16, 0, w, h));
            imgs_coordenadas.put("Cyan", new Rectangle2D(32, 0, w, h));

            imgs_coordenadas.put("Green", new Rectangle2D(48, 0, w, h));

            imgs_coordenadas.put("Red", new Rectangle2D(0, 8, w, h));
            imgs_coordenadas.put("Blue", new Rectangle2D(16, 8, w, h));
            imgs_coordenadas.put("Magenta", new Rectangle2D(32, 8, w, h));

            imgs_coordenadas.put("Yellow", new Rectangle2D(48, 8, w, h));
            imgs_coordenadas.put("Yellow", new Rectangle2D(48, 8, w, h));
            imgs_coordenadas.put("Eraser", new Rectangle2D(48, 16, w, h));


        } catch (URISyntaxException ex) {
            Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Block() {

        
        this.selected = false;
        this.size = null;
        this.type = null;
        this.imgv = new ImageView(Block.getImage());
        this.panel = new Pane();
        this.panel.getChildren().add(imgv);
       // this.panel.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, null, null)));
        
        this.blocklisteners= new ArrayList();
    }

    public static Image getImage() {
        return Block.img;
    }

    public static String[] getNamesBlocks() {
        

        return Arrays.stream(Block.imgs_coordenadas.keySet().toArray()).toArray(String[]::new);//(String[]) Block.imgs_coordenadas.keySet().toArray();
    }

    public static Rectangle2D getCoordenadaByName(String name) {
        return Block.imgs_coordenadas.get(name);
    }

    public static Size getImgBlockSize() {
        return Block.img_block_size;
    }

    public void init() {

        this.imgv.setViewport(Block.getCoordenadaByName(type));
        this.imgv.setFitWidth(App.CELLWIDTH* App.SCALE);
        this.imgv.setFitHeight(App.CELLHEIGHT* App.SCALE);
        this.imgv.setOnMouseClicked(eh -> {
           this.blocklisteners.forEach(a->{a.onClicked(this);} );        
        });
    }

    

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        this.selected =true;
        
        this.panel.setStyle("-fx-background-color: FF0000;");
    }
    public void unselect(){
        this.selected=false;
        this.panel.setStyle("");
    }
    
    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Node getComponent() {
        return this.panel;
    }

    public String getType() {
        if (Objects.equals(this.type, "Eraser")) {
            return null;
        } else {
            return type;
        }
    }

    public void setTipo(String type) {
        this.type = type;
        this.imgv.setViewport(Block.getCoordenadaByName(type));

    }

    public void addBlocklistener(IBlockListener blocklistener) {
        this.blocklisteners.add(blocklistener);
    }
    public void removeBlocklistener(IBlockListener blocklistener){
        this.blocklisteners.remove(blocklistener);
    }

}
