package ies.pedro.components;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.HashMap;

public class ElementsPanel extends Accordion{
    public ElementsPanel(HashMap<String, ArrayList<Block>> blocks) {
        super();
        blocks.forEach((s, blockList) -> {
            FlowPane flow = new FlowPane();
            flow.setHgap(10);
            flow.setVgap(10);
            blockList.forEach(block -> {
                flow.getChildren().add(block.getComponent());
            });
            TitledPane pane = new TitledPane(s, flow);
            this.getPanes().addAll(pane);
        });


        this.setPrefWidth(110); // ancho fijo
        this.setMaxHeight(Double.MAX_VALUE);

    }
}
