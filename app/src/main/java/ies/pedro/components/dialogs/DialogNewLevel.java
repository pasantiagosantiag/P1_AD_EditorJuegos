package ies.pedro.components.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;
import ies.pedro.utils.Size;

public class DialogNewLevel extends Dialog<NewLevelResponse> {
    private int max_cell_x = 140;
    private int max_cell_y = 40;
    private String text;

    public DialogNewLevel() {
        super();
        this.text = "";
    }

    public void init() {
        this.setTitle("Nuevo nivel");
        ButtonType acceptButtonType = new ButtonType("Aceptar", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField nombre = new TextField();
        Slider width = new Slider();
        width.setMin(1);
        width.setMax(max_cell_x);
        width.setBlockIncrement(1);
        width.setMajorTickUnit(1);
        width.setMinorTickCount(0);
        width.setShowTickLabels(true);
        width.setShowTickMarks(true);
        width.setSnapToTicks(true);
        Slider height = new Slider();
        height.setMin(1);
        height.setMax(max_cell_y);
        height.setBlockIncrement(1);
        height.setShowTickLabels(true);
        height.setMajorTickUnit(1);
        height.setMinorTickCount(0);
        height.setSnapToTicks(true);
        height.setShowTickMarks(true);

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombre, 1, 0);
        grid.add(new Label("Columnas:"), 0, 1);
        grid.add(width, 1, 1);
        grid.add(new Label("Filas:"), 0, 2);
        grid.add(height, 1, 2);
        Label tfw = new Label();
        tfw.setMinWidth(50);
        tfw.textProperty().bindBidirectional(width.valueProperty(), new NumberStringConverter());
        grid.add(tfw, 2, 1);
        Label tfh = new Label();
        tfh.textProperty().bindBidirectional(height.valueProperty(), new NumberStringConverter());
       tfh.setMinWidth(50);
        grid.add(tfh, 2, 2);

        this.getDialogPane().setContent(grid);
        this.setResultConverter(dialogButton -> {
            if (dialogButton == acceptButtonType) {
                int w = (int) width.getValue();
                int h = 30;// (int) height.getValue();

                return new NewLevelResponse(nombre.getText(), new Size(w, h));
            }
            return null;
        });
    }
}
