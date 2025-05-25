package org.view.jfx.control;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * ControlPanel (JavaFX)
 */
public class ControlPanel extends StackPane {

    private final Button fixedThrowButton;   // 지정윷 던지기
    private final Button randomThrowButton;  // 랜덤윷 던지기

    public ControlPanel() {
        setPrefSize(400, 400);

        fixedThrowButton  = new Button("지정윷 던지기");
        randomThrowButton = new Button("랜덤윷 던지기");

        // 버튼 가로 정렬
        HBox buttons = new HBox(20, fixedThrowButton, randomThrowButton);
        buttons.setAlignment(Pos.CENTER);

        // 컨트롤 패널 가운데 하단에 배치
        VBox wrapper = new VBox(buttons);
        wrapper.setAlignment(Pos.BOTTOM_CENTER);
        wrapper.setPadding(new Insets(0, 0, 20, 0)); // 살짝 위로 띄우기

        getChildren().add(wrapper);
    }

    /* 필요하면 외부에서 버튼 핸들러 연결 */
    public Button getFixedThrowButton()  { return fixedThrowButton; }
    public Button getRandomThrowButton() { return randomThrowButton; }
}
