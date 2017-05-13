package de.hsa.games.fatsquirrel.gui;

import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.util.Command;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by Nghia Pham on 12.05.2017.
 */
public class FxUI extends Scene implements UI {
    private static final int CELL_SIZE = 10;
    private Canvas boardCanvas;
    private Label msgLabel;

    public FxUI(Parent parent, Canvas boardCanvas, Label msgLabel) {
        super(parent);
        this.boardCanvas = boardCanvas;
        this.msgLabel = msgLabel;
    }

    public static FxUI createInstance(XY boardSize) {
        Canvas boardCanvas = new Canvas(boardSize.getX() * CELL_SIZE, boardSize.getY() * CELL_SIZE);
        Label statusLabel = new Label();
        VBox top = new VBox();
        top.getChildren().add(boardCanvas);
        top.getChildren().add(statusLabel);
        statusLabel.setText("Hallo Welt");
        final FxUI fxUI = new FxUI(top, boardCanvas, statusLabel);
        fxUI.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        System.out.println("Es wurde folgende Taste gedrückt: " + keyEvent.getCode() + " bitte behandeln!");
                        if (keyEvent.getCode() == KeyCode.W) {
                            System.out.println("moved up!");
                        } else if (keyEvent.getCode() == KeyCode.A) {
                            System.out.println("moved left");
                        } else if (keyEvent.getCode() == KeyCode.S) {
                            System.out.println("moved down!");
                        } else if (keyEvent.getCode() == KeyCode.D) {
                            System.out.println("moved right!");
                        }
                    }
                }
        );
        return fxUI;
    }


    @Override
    public void render(final BoardView view) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                repaintBoardCanvas(view);
            }
        });
    }

    private void repaintBoardCanvas(BoardView view) {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
        XY viewSize = view.getSize();

        for(int i = 0; i < view.getSize().getX();i++){
            for(int j = 0; j < view.getSize().getY(); j++){
                if(view.getEntityType(j,i) instanceof Wall){
                    gc.setFill(Color.BLACK);
                    gc.fillRect(i*10,j*10,10,10);
                }else if(view.getEntityType(j,i) instanceof BadBeast){
                    gc.setFill(Color.RED);
                    gc.fillOval(i*10,j*10,10,10);
                }else if(view.getEntityType(j,i) instanceof GoodBeast){
                    gc.setFill(Color.BLUE);
                    gc.fillOval(i*10,j*10,10,10);
                } else if(view.getEntityType(j,i) instanceof BadPlant){
                    gc.setFill(Color.YELLOW);
                    gc.fillOval(i*10,j*10,10,10);
                } else if(view.getEntityType(j,i) instanceof GoodPlant){
                    gc.setFill(Color.GREEN);
                    gc.fillOval(i*10,j*10,10,10);
                } else if(view.getEntityType(j,i) instanceof MasterSquirrel){
                    gc.setFill(Color.BROWN);
                    gc.fillRect(i*10,j*10,10,10);
                }

            }
        }
    }


    //    @Override
    public void message(final String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                msgLabel.setText(msg);
            }
        });
    }

    public void commandBuffer() {
    }

    public Command getCommand() {
        return null;
    }
}
