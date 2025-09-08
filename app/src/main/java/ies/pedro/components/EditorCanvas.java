/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.pedro.components;


import ies.pedro.App;
import ies.pedro.model.Level;
import ies.pedro.utils.Size;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class EditorCanvas extends StackPane implements IBlockListener {
 private double scale = 1.0; // factor de zoom
    private GraphicsContext ctx;
    private Size board_size;
    private Size boardcells_size;
    private int rows;
    private int cols;
    private Canvas canvas, bgcanvas;
    private ScrollPane scrollPane;
    private GraphicsContext ctxbg;
    private Block block;
    private Level level;
    private int offSetX = 8 * App.SCALE;
    private int offSetY = 8 * App.SCALE;
    private int maxRow=20;
    private boolean repaintbackground = true;
    private Image imgFondo;

    public EditorCanvas() {
        super();
    }

    public void init() {

        this.canvas = new Canvas(this.board_size.getWidth(), this.board_size.getHeight());
        this.bgcanvas = new Canvas(this.board_size.getWidth(), this.board_size.getHeight());
        this.ctx = canvas.getGraphicsContext2D();
        this.ctxbg = this.bgcanvas.getGraphicsContext2D();
       // this.imgFondo = new Image(getClass().getResourceAsStream("/elementos.png"));
        Pane container = new Pane();
        container.getChildren().addAll(this.bgcanvas, this.canvas);
      
        this.scrollPane = new ScrollPane(container);
        this.scrollPane.setPannable(true);
         // Evento de zoom con la rueda
        scrollPane.addEventFilter(ScrollEvent.SCROLL, e -> {
            if (e.isControlDown()) { // Ctrl + rueda = zoom
                double zoomFactor = 1.05;
                if (e.getDeltaY() < 0) {
                    zoomFactor = 1 / zoomFactor;
                }
                scale *= zoomFactor;

                // Aplicar transformación de escala
                canvas.setScaleX(scale);
                canvas.setScaleY(scale);
                e.consume();
            }
        });

        this.getChildren().add(this.scrollPane);
        this.canvas.setOnMouseClicked(t -> {

            //solo se puede escribir en el area correcta
            if (this.block != null && this.level != null  && t.getX()>this.offSetX && t.getX()<this.board_size.getWidth()-this.offSetX  && t.getY()>this.offSetY) {
                //transformar la pulsacion a la posición
                int r = (int) (((int) t.getY()-this.offSetY) / ((this.board_size.getHeight()-this.offSetY) / this.rows));
                int c = (int) (((int) t.getX()-this.offSetX) / ((this.board_size.getWidth()-this.offSetX*2) / this.cols));
                //solo se deja colocar celdas por encima de una línea, desde 0 hasta maxRow
                if(r<this.maxRow) {
                    this.getLevel().setBlockValue(this.block.getType(), r, c);
                }
                //System.out.println(this.getLevel());
            }
            this.draw();
        });

    }

    public void reset() {
        if(this.getLevel()!=null)
            this.getLevel().reset();
        this.repaintbackground = true;
        this.draw();
    }
    public void setImage(Image img){
        this.imgFondo=img;
       this.reset();
    }
    private void clear() {
        this.bgcanvas.getGraphicsContext2D().clearRect(0, 0, this.board_size.getWidth(), this.board_size.getHeight());
        this.canvas.getGraphicsContext2D().clearRect(0, 0, this.board_size.getWidth(), this.board_size.getHeight());
    }

    public void reset(Size s) {
        this.setRows(s.getHeight());
        this.setCols(s.getWidth());
        this.getLevel().reset();
        this.repaintbackground = true;
        this.clear();
        this.init();
        this.draw();
    }

    private void drawGrid(GraphicsContext gc) {
        //limpiar lienzo
         gc.clearRect(0, 0, this.board_size.getWidth(), this.board_size.getHeight());
        //si existe el nivel y tiene fondo se pinta
        if (this.level != null && this.level.getBackgroundImage() != null && this.level.getBackgroundImage() != "") {
            var fondo=new Image(this.level.getBackgroundImage());
             gc.drawImage(fondo,
            
                    0,
                    // this.getLevel().getBackgroundPosition().getY(),
                    0,
                    306,
                    160,
                    0, 0,  306*3, 160*3);//this.level.getBackgroundPosition().getX(), this.level.getBackgroundPosition().getY(), 224, 240);
        
         /*    gc.drawImage(fondo,
            
                    0,
                    // this.getLevel().getBackgroundPosition().getY(),
                    0,
                    this.imgFondo.getWidth(),
                    240,
                    0, 0,  this.imgFondo.getWidth(), this.board_size.getHeight());//this.level.getBackgroundPosition().getX(), this.level.getBackgroundPosition().getY(), 224, 240);
        */ } else {
            gc.setFill(Color.GRAY);
            gc.fillRect(0, 0, this.getBoard_size().getWidth(), this.getBoard_size().getHeight());
        }
      /*  gc.setStroke(Color.WHITE);//new Color((double) Math.random(), (double) Math.random(), (double) Math.random(), 1));
        //tamaño de las celdas, se quitan los bordes, cuidado en el eje x hay 2 bordes *2
        int h = 8 * App.SCALE;//(this.board_size.getHeight() - 8 * App.SCALE) / this.getRows();
        int w =8 * App.SCALE;// (this.board_size.getWidth() - (8 * App.SCALE) * 2) / this.getCols();

        //se pintan las filas y las columnas
        for (int k = 0; k < this.rows && k<=this.maxRow; k++) {
            gc.moveTo(offSetX, k * h + offSetY);
            gc.lineTo(this.getBoard_size().getWidth() - offSetX, k * h + offSetY);
            gc.stroke();

        }
        for (int i = 0; i <= this.cols; i++) {
            gc.moveTo(i * w + offSetX, offSetY);
            gc.lineTo(i * w + offSetX, h*this.maxRow+offSetY);//this.getBoard_size().getHeight() + offSetY);
            gc.stroke();
        }*/
    }

    public void draw() {
        //solo se pinta el fondo si hay un cambio, por rendimiento
        if (this.repaintbackground) {
            this.drawGrid(this.ctxbg);
            this.repaintbackground = false;
        }
        this.draw(this.ctx);
    }

    private void draw(GraphicsContext gc) {
        int offSetX = 8 * App.SCALE;
        int offSetY = 8 * App.SCALE;
        //tamaño de las celdas, se quitan los bordes, cuidado en el eje x hay 2 bordes *2
        int h = (this.board_size.getHeight() - 8 * App.SCALE) / this.getRows();
        int w = (this.board_size.getWidth() - (8 * App.SCALE) * 2) / this.getCols();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BROWN);
        gc.clearRect(0, 0, this.getBoard_size().getWidth(), this.getBoard_size().getHeight());
        if (this.level != null) {
            for (int i = 0; i <= this.getRows(); i++) {
                for (int j = 0; j < this.getCols(); j++) {
                    if (this.getLevel().getBlockValue(i, j) != null) {
                        Rectangle2D r = Block.getCoordenadaByName(this.getLevel().getBlockValue(i, j));
                        if (r != null)
                            gc.drawImage(Block.getImage(), r.getMinX(), r.getMinY(), App.CELLWIDTH, App.CELLHEIGHT, j * w + offSetX, i * h+offSetY, w, h);;

                    }
                }
            }
        }
    }

    public Size getBoard_size() {
        return board_size;
    }

    public void setBoard_size(Size board_size) {
        this.board_size = board_size;
    }

    public void setBlock(Block block) {
        this.block = block;
        this.draw();
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
        this.repaintbackground = true;
        this.clear();
        this.draw();
    }
    public boolean isRepaintbackground() {
        return repaintbackground;
    }

    public void setRepaintbackground(boolean repaintbackground) {
        this.repaintbackground = repaintbackground;
    }

    @Override
    public void onClicked(Block b) {
        this.block = b;
    }

    @Override
    public void onDoubleClicked(Block b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
