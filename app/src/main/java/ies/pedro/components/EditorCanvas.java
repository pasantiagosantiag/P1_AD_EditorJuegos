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

public class EditorCanvas extends StackPane {
    public enum MODO_EDITOR {
        DELETE,
        NONE,
        ADD
    }

    private double scale = 1.0; // factor de zoom
    private MODO_EDITOR mode;

    //bloque actual
    private Block block;
    //reactivo
    private SimpleIntegerProperty board_size_width;
    private SimpleIntegerProperty board_size_heigth;

    private Canvas canvas, bgcanvas, gridcanvas;
    private ScrollPane scrollPane;
    private GraphicsContext ctx;
    private GraphicsContext ctxbg;
    private GraphicsContext gridbg;

    private Level level;


    private boolean repaintbackground = true;
    private boolean repaingrid = true;
    private Image imgFondo;

    public EditorCanvas() {
        super();
        this.mode = MODO_EDITOR.NONE;
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

        this.gridcanvas = new Canvas();// this.board_size.getWidth(), this.board_size.getHeight());
        this.gridcanvas.widthProperty().bind(board_size_width);
        this.gridcanvas.heightProperty().bind(board_size_heigth);
        this.gridcanvas.getGraphicsContext2D().setTransform(affine);

        this.ctx = canvas.getGraphicsContext2D();
        this.ctxbg = this.bgcanvas.getGraphicsContext2D();
        this.gridbg = this.gridcanvas.getGraphicsContext2D();
        // this.imgFondo = new Image(getClass().getResourceAsStream("/elementos.png"));
        Pane container = new Pane();
        container.getChildren().addAll(this.bgcanvas, this.gridcanvas, this.canvas);
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
            //add
            if (this.mode == MODO_EDITOR.ADD) {
                // transformar la pulsacion a la posición
                int r = ((int) ((t.getY() / App.SCALE)) / App.CELLHEIGHT) * App.CELLHEIGHT;
                int c = ((int) ((t.getX() / App.SCALE)) / App.CELLWIDTH) * App.CELLWIDTH;

                ies.pedro.model.Block b = new ies.pedro.model.Block(this.block.getType(), new Rectangle2D(c, r, this.block.getBlockSize().getWidth(), this.block.getBlockSize().getHeight()));
                //solo si no se tiene uno
                if (!this.level.intersects(b))
                    this.level.addElement(b);
                this.draw();
            }
            //remove
            else if (this.mode == MODO_EDITOR.DELETE) {
                var o = this.level.getByPosition((int) (t.getX() / App.SCALE), (int) (t.getY() / App.SCALE));
                if (o.isPresent()) {
                    this.level.removeElement(o.get());
                }
                this.draw();
            }

        });

    }

    public void changMode(MODO_EDITOR mode) {
        this.mode = mode;
    }

    public void reset() {
        if (this.getLevel() != null)
            this.getLevel().reset();
        this.repaintbackground = true;
        this.dragBackgroundGrid(this.gridbg);
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
        // si existe el nivel y tiene fondo se pinta
        if (this.level != null && this.level.getBackgroundImage() != null && this.level.getBackgroundImage() != "") {
            var fondo = new Image(this.level.getBackgroundImage());
            for (int i = 0; i < this.board_size_width.get() / App.SCALE; i = i + ((int) fondo.getWidth())) {
                gc.drawImage(fondo,
                        0,
                        // this.getLevel().getBackgroundPosition().getY(),
                        0,
                        fondo.getWidth(),
                        fondo.getHeight(),
                        i, 0, fondo.getWidth(), fondo.getHeight());// this.level.getBackgroundPosition().getX(),

            }
        }
    }

    private void dragBackgroundGrid(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(0.5);
        if (this.level != null && this.level.getSize() != null) {
            // columnas
            for (int i = 0; i <= this.level.getSize().getWidth() + 1; i++) {
                gc.moveTo(i * App.CELLWIDTH, 0);
                gc.lineTo(i * App.CELLWIDTH, App.CELLHEIGHT* this.level.getSize().getHeight());// this.getBoard_size().getHeight()
                gc.stroke();
            }
            // filas
            for (int k = 0; k < this.level.getSize().getHeight(); k++) {
                gc.moveTo(0, k * App.CELLHEIGHT);
                gc.lineTo(this.level.getSize().getWidth() * App.CELLWIDTH, k * App.CELLHEIGHT);
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
        }
    }

    public void draw() {
        // solo se pinta el fondo si hay un cambio, por rendimiento
        if (this.repaintbackground) {
            this.drawBackground(this.ctxbg);
            this.repaintbackground = false;
        }
        //se pinta solo el grid solo si es necedario, por rendimiento
        if (this.repaingrid) {
            // this.drawBackground(this.ctxbg);
            this.dragBackgroundGrid(this.gridbg);
            this.repaingrid = false;
        }
        this.draw(this.ctx);
    }

    private void draw(GraphicsContext gc) {

       // gc.setStroke(Color.BLACK);
      //  gc.setFill(Color.BROWN);

        gc.clearRect(0, 0, this.getBoard_size().getWidth(), this.getBoard_size().getHeight());
        if (this.level != null) {
            this.level.getElements().forEach(e -> {
                Rectangle2D r = Block.getCoordenadaByName(e.getType());

                if (r != null)
                    gc.drawImage(Block.getImage(), r.getMinX(), r.getMinY(),
                            r.getWidth(), r.getHeight(),
                            e.getRectangle().getMinX(), e.getRectangle().getMinY(),
                            e.getRectangle().getWidth(), e.getRectangle().getHeight()
                    );
                ;
            });
        }
    }

    public Size getBoard_size() {
        return new Size(this.board_size_width.get(), this.board_size_heigth.get()); // cols)board_size;
    }

    public void setBoard_size(Size board_size) {
        // this.board_size = board_size;
        this.board_size_heigth.set(board_size.getHeight() * App.CELLHEIGHT * App.SCALE);
        this.board_size_width.set(board_size.getWidth() * App.CELLHEIGHT * App.SCALE);

    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        this.repaintbackground = true;
        this.repaingrid = true;
        if (level != null) {
            this.setBoard_size(level.getSize());
        }
        else{
            this.setBoard_size(new Size(0,0));
        }
        this.clear();
        this.draw();
    }
    public void setRepaintbackground(boolean repaintbackground) {
        this.repaintbackground = repaintbackground;
    }

    public void setBlock(Block b) {
        this.block = b;
    }


}
