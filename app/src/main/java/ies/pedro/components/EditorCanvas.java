/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.pedro.components;

import com.google.common.graph.Graph;

import ies.pedro.App;
import ies.pedro.model.Level;
import ies.pedro.utils.Point;
import ies.pedro.utils.Size;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class EditorCanvas extends StackPane implements IBlockListener {
    private double scale = 1.0; // factor de zoom
    private GraphicsContext ctx;
 private Block block;
    private SimpleIntegerProperty board_size_width;
    private SimpleIntegerProperty board_size_heigth;
    // private Size boardcells_size;
    //private int rows = 0;
    //private int cols = 0;
    private Canvas canvas, bgcanvas;
    private ScrollPane scrollPane;
    private GraphicsContext ctxbg;
   
    private Level level;
    private int offSetX = 0;// 8 * App.SCALE;
    private int offSetY = 0;// 8 * App.SCALE;
   
    private boolean repaintbackground = true;
    private Image imgFondo;

    public EditorCanvas() {
        super();
        this.board_size_heigth = new SimpleIntegerProperty(0);
        this.board_size_width = new SimpleIntegerProperty(30);
    }

    public void init() {
// Crear una Affine
        Affine affine = new Affine();
        affine.appendScale(App.SCALE, App.SCALE);           // escalar por 2

        this.canvas = new Canvas();// this.board_size.getWidth(), this.board_size.getHeight());
        this.canvas.widthProperty().bind(board_size_width);
        this.canvas.heightProperty().bind(board_size_heigth);

        this.canvas.getGraphicsContext2D().setTransform(affine);
        this.bgcanvas = new Canvas();// this.board_size.getWidth(), this.board_size.getHeight());
        this.bgcanvas.widthProperty().bind(board_size_width);
        this.bgcanvas.heightProperty().bind(board_size_heigth);

        this.bgcanvas.getGraphicsContext2D().setTransform(affine);
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
            if(this.block!=null)
                System.out.println(this.block.getType());
            // solo se puede escribir en el area correcta
            
              if (this.block != null && this.level != null && t.getX() > this.offSetX
              && t.getX() < this.board_size_width.get() - this.offSetX && t.getY() >
              this.offSetY) {
              // transformar la pulsacion a la posición
              int r = (int) (((int) t.getY()/App.SCALE - this.offSetY)
              / App.CELLHEIGHT);//((this.board_size_heigth.get() - this.offSetY) / this.rows));
              int c = (int) (((int) t.getX()/App.SCALE - this.offSetX)
              / App.CELLWIDTH);// ((this.board_size_width.get() - this.offSetX * 2) / this.cols));
              System.out.println("F:"+r+" C:"+c);
             ies.pedro.model.Block b= new ies.pedro.model.Block(this.block.getType(),new Point(c,r));
            this.level.addElement(b);
             // solo se deja colocar celdas por encima de una línea, desde 0 hasta maxRow
              //if (r < this.maxRow) {
              //this.getLevel().setBlockValue(this.block.getType(), r, c);
              //}
              // System.out.println(this.getLevel());
              }
             
            this.draw();
        });

    }

    public void reset() {
        if (this.getLevel() != null)
            this.getLevel().reset();
        this.repaintbackground = true;
        this.draw();
    }

    public void setImage(Image img) {
        this.imgFondo = img;
        this.reset();
    }

    private void clear() {
        this.bgcanvas.getGraphicsContext2D().clearRect(0, 0, this.board_size_width.get(), this.board_size_heigth.get());
        this.canvas.getGraphicsContext2D().clearRect(0, 0, this.board_size_width.get(), this.board_size_heigth.get());
    }

    private void drawBackgroundImage(GraphicsContext gc) {
        int contador = 0;

        // gc.fillRect(0, 0, this.getBoard_size().getWidth(),
        // this.getBoard_size().getHeight());

        // si existe el nivel y tiene fondo se pinta
        if (this.level != null && this.level.getBackgroundImage() != null && this.level.getBackgroundImage() != "") {
            var fondo = new Image(this.level.getBackgroundImage());
            for (int i = 0; i < this.board_size_width.get()/App.SCALE; i=i+((int)fondo.getWidth())) {
                gc.drawImage(fondo,
                        0,
                        // this.getLevel().getBackgroundPosition().getY(),
                        0,
                        fondo.getWidth(),
                        fondo.getHeight(),
                        i, 0, fondo.getWidth() , fondo.getHeight() );// this.level.getBackgroundPosition().getX(),
          
                    }
        }
    }

    private void dragBackgroundGrid(GraphicsContext gc) {
         gc.setStroke(Color.RED);
        //gc.setStroke(new Color((double) Math.random(), (double) Math.random(), (double) Math.random(), 1));
        // tamaño de las celdas, se quitan los bordes, cuidado en el eje x hay 2 bordes
        // *2
        int h = App.CELLHEIGHT ;// (this.board_size.getHeight() - 8 * App.SCALE) / this.getRows();
        int w = App.CELLWIDTH ;// (this.board_size.getWidth() - (8 * App.SCALE) * 2) / this.getCols();
        if (this.level != null && this.level.getSize() != null) {
            // columnas
            for (int i = 0; i <= this.level.getSize().getWidth() + 1; i++) {
                gc.moveTo(i * w + offSetX, offSetY);
                gc.lineTo(i * w + offSetX, h * this.level.getSize().getHeight() + offSetY);// this.getBoard_size().getHeight()
                                                                                           // // + offSetY);
                gc.stroke();
                gc.fillText(Integer.toString(i), i * w + offSetX, offSetY);
            }
            // filas
            for (int k = 0; k < this.level.getSize().getHeight(); k++) {
                gc.moveTo(offSetX, k * h + offSetY);
                gc.lineTo(this.level.getSize().getWidth() * w - offSetX, k * h + offSetY);
                gc.stroke();

            }
        }
    }

    private void drawBackground(GraphicsContext gc) {
        // limpiar lienzo
        gc.clearRect(0, 0, this.board_size_width.get(), this.board_size_heigth.get());
        // se pinta el fondo gris
        gc.setFill(Color.GRAY);
        if (this.board_size_heigth.getValue() > 0) {
            this.drawBackgroundImage(gc);
            this.dragBackgroundGrid(gc);

        }

    }

    public void draw() {
        // solo se pinta el fondo si hay un cambio, por rendimiento
        if (this.repaintbackground) {
            this.drawBackground(this.ctxbg);
            this.repaintbackground = false;
        }
        this.draw(this.ctx);
    }

    private void draw(GraphicsContext gc) {
        int offSetX = 8 ;
        int offSetY = 8;
      //  if (this.cols > 0 && this.rows > 0)
       {
            // tamaño de las celdas, se quitan los bordes, cuidado en el eje x hay 2 bordes
            // *2
            int h = App.CELLHEIGHT; //(this.board_size_heigth.get() - 8 ) / this.getRows();
            int w = App.CELLWIDTH; //(this.board_size_width.get() - (8 ) * 2) / this.getCols();
            gc.setStroke(Color.BLACK);
            gc.setFill(Color.BROWN);
            
            gc.clearRect(0, 0, this.getBoard_size().getWidth(), this.getBoard_size().getHeight());
            if (this.level != null) {
                this.level.getElements().forEach( e->{
                     Rectangle2D r = Block.getCoordenadaByName(e.getType());
                            if (r != null)
                                gc.drawImage(Block.getImage(), r.getMinX(), r.getMinY(), 
                                r.getWidth(), r.getHeight(),
                                        e.getPoint().getX() * w-w + offSetX, e.getPoint().getY() * h + offSetY-h, 
                                        r.getWidth(), r.getHeight());
                            ;
                });
               /* for (int i = 0; i <= this.getRows(); i++) {
                    for (int j = 0; j < this.getCols(); j++) {
                        if (this.getLevel().getBlockValue(i, j) != null) {
                            Rectangle2D r = Block.getCoordenadaByName(this.getLevel().getBlockValue(i, j));
                            if (r != null)
                                gc.drawImage(Block.getImage(), r.getMinX(), r.getMinY(), App.CELLWIDTH, App.CELLHEIGHT,
                                        j * w + offSetX, i * h + offSetY, w, h);
                            ;

                        }
                    }
                }*/
            }
        }
    }

    public Size getBoard_size() {
        return new Size(this.board_size_width.get(), this.board_size_heigth.get()); // cols)board_size;
    }

    public void setBoard_size(Size board_size) {
        // this.board_size = board_size;
        this.board_size_heigth.set(board_size.getHeight()  * App.CELLHEIGHT*App.SCALE);
        this.board_size_width.set(board_size.getWidth() * App.CELLHEIGHT*App.SCALE);

    }

   /* public void setBlock(Block block) {
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
    }*/

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        this.repaintbackground = true;
        this.setBoard_size(level.getSize());
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
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }
}
