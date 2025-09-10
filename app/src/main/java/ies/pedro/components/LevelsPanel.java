/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.pedro.components;

import ies.pedro.components.dialogs.DialogNewLevel;

import ies.pedro.components.dialogs.NewLevelResponse;
import ies.pedro.model.Level;
import ies.pedro.model.Levels;
import ies.pedro.utils.Size;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.antdesignicons.AntDesignIconsFilled;
import org.kordamp.ikonli.antdesignicons.AntDesignIconsOutlined;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class LevelsPanel extends VBox implements IBlockListener {


    private Size blockSize;
    private ArrayList<Block> blocks;
    private Block selected;
    private ListView listlevels;
    private Button add;
    private Button delete;
    private Button reset;
    private Button play;
    private Button pause;
    private Button stop;
    private Consumer<String> onseleted=null;
    private Consumer<NewLevelResponse> onadd=null;
    private Consumer<String> ondelete=null;
    private Consumer<String> onreset=null;
    private Runnable onplay=null;
    private Runnable onpause=null;
    private Runnable onstop=null;
    public LevelsPanel() {
        super();
        this.setPadding(new Insets(5, 0, 5, 0));
        this.setBlockSize(new Size(50, 50));
    }


    public void addBlock(Block b) {
        this.blocks.add(b);
    }
    public void reset(){
        this.listlevels.getItems().clear();
    }
    public void init() {
        int col = 0, row = 0;
        this.listlevels = new ListView();
        this.listlevels.setOnMouseClicked( mouseEvent -> {
            //se llama a la capa superior para informar
            if(this.onseleted!=null) {
                this.onseleted.accept(this.listlevels.getSelectionModel().getSelectedItem().toString());
           }
        });
        //se crean los elementos
        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.BASELINE_LEFT);

        this.add = new Button("Add");
        add.setGraphic(new FontIcon(AntDesignIconsFilled.FILE_ADD));
        this.add.setOnMouseClicked(mouseEvent -> {
           // DialogName dn = new DialogName();
            DialogNewLevel ds = new DialogNewLevel();
          //  dn.init();
            ds.init();
            Optional<NewLevelResponse> result = ds.showAndWait();

            if (result.isPresent()) {
                result.get();
                if (result.get().getName() != "") {
                    //se añade y se deja seleccionado
                    this.listlevels.getItems().add(result.get().getName());
                    this.listlevels.getSelectionModel().select(result.get());//.select(this.listlevels.getSelectionModel());
                    //se llama a la capa superior para informar
                    if (this.onadd != null) {
                        this.onadd.accept(result.get());
                    }
                }
            }
        });


        this.delete = new Button("Delete");
        //similar a los eventos
        this.delete.setOnMouseClicked(mouseEvent -> {
            if (this.listlevels.getSelectionModel().getSelectedItem() != null) {
                if(this.ondelete!=null) {
                    this.ondelete.accept(this.listlevels.getSelectionModel().getSelectedItem().toString());
                }
                this.listlevels.getItems().remove(this.listlevels.getSelectionModel().getSelectedItem());
            }

        });
        delete.setGraphic(new FontIcon(AntDesignIconsFilled.DELETE));


        this.reset = new Button("Reset");
        this.reset.setOnMouseClicked(mouseEvent -> {

                    if(this.onreset!=null && this.listlevels.getSelectionModel()!=null) {
                        this.onreset.accept(this.listlevels.getSelectionModel().getSelectedItem().toString());
                    }



        });
        this.reset.setGraphic(new FontIcon(AntDesignIconsOutlined.RELOAD));

        hbox.getChildren().add(this.add);
        hbox.getChildren().add(this.delete);
        hbox.getChildren().add(this.reset);


        HBox hboxmusic = new HBox(5);
        hboxmusic.setPadding(new Insets(5,5,0,0));
        hboxmusic.setAlignment(Pos.BASELINE_LEFT);

        this.play = new Button("Play");
        this.play.setGraphic(new FontIcon(AntDesignIconsFilled.PLAY_CIRCLE));
        this.play.setOnMouseClicked(mouseEvent -> {
            if (this.listlevels.getSelectionModel().getSelectedItem() != null && this.onplay!=null) {
                    this.onplay.run();
            }
        });


        this.pause = new Button("Pause");
        this.pause.setOnMouseClicked(mouseEvent -> {
            if (this.listlevels.getSelectionModel().getSelectedItem() != null && this.onpause!=null) {
                    this.onpause.run();
            }

        });
        this.pause.setGraphic(new FontIcon(AntDesignIconsFilled.PAUSE_CIRCLE));

        this.stop = new Button("Stop");
        this.stop.setOnMouseClicked(mouseEvent ->
        {

            if(this.onstop!=null && this.listlevels.getSelectionModel()!=null) {
                this.onstop.run();
            }



        });
        this.stop.setGraphic(new FontIcon(AntDesignIconsOutlined.STOP));

        hboxmusic.getChildren().add(this.play);
        hboxmusic.getChildren().add(this.pause);
        hboxmusic.getChildren().add(this.stop);

        this.getChildren().add(this.listlevels);
        this.getChildren().add(hbox);
        this.getChildren().add(hboxmusic);

    }


    public Size getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(Size blockSize) {
        this.blockSize = blockSize;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public void onClicked(Block b) {
        if (this.selected != null)
            this.selected.unselect();
        this.selected = b;
        b.select();
    }

    @Override
    public void onDoubleClicked(Block b) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Consumer<String> getOnseleted() {
        return onseleted;
    }

    public void setOnseleted(Consumer<String> onseleted) {
        this.onseleted = onseleted;
    }
    public void setOnreset(Consumer<String> onreset ){
        this.onreset=onreset;
    }
    public Consumer<NewLevelResponse> getOnadd() {
        return onadd;
    }

    public void setOnadd(Consumer<NewLevelResponse> onadd) {
        this.onadd = onadd;
    }

    public Consumer<String> getOndelete() {
        return ondelete;
    }

    public void setOndelete(Consumer<String> ondelete) {
        this.ondelete = ondelete;
    }

    public void setSelected(Block selected) {
        this.selected = selected;
    }
    public void setItems(Levels levels){
        this.listlevels.getItems().clear();
        levels.getLevels().stream().map(Level::getName).forEach( l->this.listlevels.getItems().add(l));
        this.listlevels.getSelectionModel().selectFirst();
        if(this.onseleted!=null)
            this.onseleted.accept(this.listlevels.getSelectionModel().getSelectedItem().toString());
       this.listlevels.getItems().forEach(System.out::println);
    }
    public void setOnplay(Runnable onplay) {
        this.onplay = onplay;
    }

    public void setOnpause(Runnable onpause) {
        this.onpause = onpause;
    }

    public void setOnstop(Runnable onstop) {
        this.onstop = onstop;
    }
}
