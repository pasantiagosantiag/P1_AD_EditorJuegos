/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.pedro.components;

import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import ies.pedro.App;
import ies.pedro.utils.Size;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author Pedro
 */
public class Block {

    // private Rectangle r;
    private ImageView imgv;
    private Pane panel;

    private boolean selected;
    // private Size size;
    private static Image img;
    private Size img_block_size;
    private static Map<String, HashMap<String, Rectangle2D>> imgs_coordenadas;
    private String type;
    private final ArrayList<IBlockListener> blocklisteners;
    private Runnable onClick;

    static {
        try {


            imgs_coordenadas = new HashMap<>();

            img = new Image(Block.class.getResource("/elementos.png").toURI().toString());
            imgs_coordenadas.put("Utils", new HashMap<>());


            imgs_coordenadas.get("Utils").put("Borrador", new Rectangle2D(164, 177, 8, 8));
            imgs_coordenadas.get("Utils").put("Puntero", new Rectangle2D(175, 176, 8, 8));

            imgs_coordenadas.put("Terrenos", new HashMap<>());
            imgs_coordenadas.get("Terrenos").put("TierraPequenya", new Rectangle2D(256, 80, 16, 16));
            imgs_coordenadas.get("Terrenos").put("TierraGrande", new Rectangle2D(208, 48, 48, 16));
            imgs_coordenadas.get("Terrenos").put("CuadradoHieloPequeny", new Rectangle2D(16, 96, 8, 8));
            imgs_coordenadas.get("Terrenos").put("CuadradoHielo", new Rectangle2D(0, 96, 16, 16));
            imgs_coordenadas.get("Terrenos").put("AguaAzul", new Rectangle2D(336, 120, 16, 8));
            imgs_coordenadas.get("Terrenos").put("AguaVerde", new Rectangle2D(336, 150, 16, 8));

            imgs_coordenadas.put("Enemigos", new HashMap<>());
            imgs_coordenadas.get("Enemigos").put("Pulpo", new Rectangle2D(96, 4, 15, 12));
            imgs_coordenadas.get("Enemigos").put("Calabaza", new Rectangle2D(144, 0, 16, 16));
            imgs_coordenadas.get("Enemigos").put("Captus", new Rectangle2D(144, 26, 16, 22));
            imgs_coordenadas.get("Enemigos").put("Tanque", new Rectangle2D(2, 69, 28, 26));
            imgs_coordenadas.put("Suelos", new HashMap<>());
            imgs_coordenadas.get("Suelos").put("SueloVerde", new Rectangle2D(32, 96, 16, 16));
            imgs_coordenadas.get("Suelos").put("SueloVerdePe", new Rectangle2D(48, 96, 8, 8));
            imgs_coordenadas.get("Suelos").put("SueloMarron", new Rectangle2D(64, 96, 16, 16));
            imgs_coordenadas.get("Suelos").put("SueloMarronPe", new Rectangle2D(80, 96, 8, 8));

            imgs_coordenadas.get("Suelos").put("SueloRojo", new Rectangle2D(0, 112, 16, 16));
            imgs_coordenadas.get("Suelos").put("SueloRojoPe", new Rectangle2D(16, 112, 8, 8));

            imgs_coordenadas.put("Cuadros", new HashMap<>());
            imgs_coordenadas.get("Cuadros").put("CuadradoRojoPequeny", new Rectangle2D(160, 128, 8, 8));
            imgs_coordenadas.get("Cuadros").put("CuadradoVerdePequeny", new Rectangle2D(160, 144, 8, 8));
            imgs_coordenadas.get("Cuadros").put("CuadradoNaranjaPequeny", new Rectangle2D(192, 128, 8, 8));
            imgs_coordenadas.get("Cuadros").put("CuadradoAzulPequeny", new Rectangle2D(176, 144, 8, 8));
            imgs_coordenadas.get("Cuadros").put("CuadradoSueloVerdePequenyo", new Rectangle2D(48, 96, 8, 8));


            imgs_coordenadas.get("Cuadros").put("CuadradoRojo", new Rectangle2D(144, 128, 16, 16));
            imgs_coordenadas.get("Cuadros").put("CuadradoVerde", new Rectangle2D(144, 144, 16, 16));
            imgs_coordenadas.get("Cuadros").put("CuadradoNaranja", new Rectangle2D(176, 128, 16, 16));
            imgs_coordenadas.get("Cuadros").put("CuadradoAzul", new Rectangle2D(176, 144, 16, 16));


        } catch (URISyntaxException ex) {
            Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Block(String group, String type) {
        this.type = type;
        this.selected = false;
        this.img_block_size = new Size((int) Block.imgs_coordenadas.get(group).get(type).getWidth(), (int) Block.imgs_coordenadas.get(group).get(type).getHeight());
        this.blocklisteners = new ArrayList();
        this.imgv = new ImageView(Block.getImage());
        this.panel = new Pane();
        this.panel.getChildren().add(imgv);
        var rectangulo = Block.getCoordenadaByName(group, type);
        this.imgv.setViewport(rectangulo);
        this.imgv.setFitWidth(rectangulo.getWidth() * App.SCALE);// this.size.getWidth());// App.CELLWIDTH* App.SCALE);
        this.imgv.setFitHeight(rectangulo.getHeight() * App.SCALE);// this.size.getWidth());// App.CELLHEIGHT*
        // App.SCALE);
        this.imgv.setOnMouseClicked(eh -> {
            if (this.onClick != null) {
                this.onClick.run();
            }
            this.blocklisteners.forEach(a -> {
                a.onClicked(this);
            });
        });


    }

    public static Image getImage() {
        return Block.img;
    }

    public static String[] getNamesBlocksByGroup(String group) {

        return Arrays.stream(Block.imgs_coordenadas.get(group).keySet().toArray()).toArray(String[]::new);// (String[])
        // Block.imgs_coordenadas.keySet().toArray();
    }

    public static List<String> getGroups() {
        System.out.println(Block.imgs_coordenadas.keySet().stream().toList());
        return Block.imgs_coordenadas.keySet().stream().toList(); //Arrays.stream((Block.imgs_coordenadas.keySet().toArray())).toList());//Arrays.stream(Block.imgs_coordenadas.keySet().toArray()).toArray(String[]::new);
    }

    public static HashMap<String, Rectangle2D> getGroup(String group) {
        return Block.imgs_coordenadas.get(group);
    }

    public static Rectangle2D getCoordenadaByName(String group, String name) {
        return Block.imgs_coordenadas.get(group).get(name);
    }

    public static Rectangle2D getCoordenadaByName(String name) {
        Rectangle2D block = Block.imgs_coordenadas.values().stream()
                .filter(submapa -> submapa.containsKey(name))
                .map(submapa -> submapa.get(name))
                .findFirst()
                .orElse(null);
        return block;
    }


    public boolean isSelected() {
        return selected;
    }

    public void select() {
        this.selected = true;

        this.panel.setStyle("-fx-background-color: FF0000;");
    }

    public void unselect() {
        this.selected = false;
        this.panel.setStyle("");
    }


    public Node getComponent() {
        return this.panel;
    }

    public String getType() {
       /* if (Objects.equals(this.type, "Eraser")) {
            return null;
        } else {*/
        return type;
        /*}*/
    }

    public Size getBlockSize() {
        return this.img_block_size;
    }

    public void setTipo(String group, String type) {
        this.type = type;
        this.imgv.setViewport(Block.getCoordenadaByName(group, type));

    }

    public void addBlocklistener(IBlockListener blocklistener) {
        this.blocklisteners.add(blocklistener);
    }

    public void addClickListener(Runnable event) {
        this.onClick = event;
    }

    public void removeClickListener() {
        this.onClick = null;
    }

    public void removeBlocklistener(IBlockListener blocklistener) {
        this.blocklisteners.remove(blocklistener);
    }

}
