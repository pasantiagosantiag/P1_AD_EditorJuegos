package ies.pedro.components.dialogs;



import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;


public class DialogName extends Dialog<String> {
    private String text;
    public DialogName()
    {
        super();
        this.text="";
    }
    public DialogName(String level_name)
    {
        super();
        this.text=level_name;
    }
    public void init() {
        this.setTitle("Nombre nivel");
        ButtonType acceptButtonType = new ButtonType("Aceptar", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField nombre = new TextField();


        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombre, 1, 0);


        this.getDialogPane().setContent(grid);
        this.setResultConverter(dialogButton -> {
            if (dialogButton == acceptButtonType) {
                return nombre.getText();
            }
            return null;
        });
    }
}
