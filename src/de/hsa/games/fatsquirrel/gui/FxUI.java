package de.hsa.games.fatsquirrel.gui;

import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.console.CommandTypeInfo;
import de.hsa.games.fatsquirrel.console.GameCommandType;
import de.hsa.games.fatsquirrel.console.ScanException;
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
    private CommandTypeInfo[] commandTypeInfos;
    private String[] str;
    private Command inputBuffer;

    public FxUI(Parent parent, Canvas boardCanvas, Label msgLabel) {
        super(parent);
        this.boardCanvas = boardCanvas;
        this.msgLabel = msgLabel;
        this.commandTypeInfos = GameCommandType.values();
        this.str = new String[2];
    }

    public static FxUI createInstance(XY boardSize) {
        Canvas boardCanvas = new Canvas(boardSize.getX() * CELL_SIZE, boardSize.getY() * CELL_SIZE);
        Label statusLabel = new Label();
        VBox top = new VBox();
        top.getChildren().add(boardCanvas);
        top.getChildren().add(statusLabel);
        statusLabel.setText("Hallo Welt");
        final FxUI fxUI = new FxUI(top, boardCanvas, statusLabel);
//        fxUI.setOnKeyPressed(
//                new EventHandler<KeyEvent>() {
//                    @Override
//                    public void handle(KeyEvent keyEvent) {
//                        System.out.println("Es wurde folgende Taste gedrückt: " + keyEvent.getCode() + " bitte behandeln!");
//                        if (keyEvent.getCode() == KeyCode.W) {
//                            System.out.println("moved up!");
//                        } else if (keyEvent.getCode() == KeyCode.A) {
//                            System.out.println("moved left");
//                        } else if (keyEvent.getCode() == KeyCode.S) {
//                            System.out.println("moved down!");
//                        } else if (keyEvent.getCode() == KeyCode.D) {
//                            System.out.println("moved right!");
//                        }
//                    }
//                }
//        );
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

        for (int i = 0; i < view.getSize().getX(); i++) {
            for (int j = 0; j < view.getSize().getY(); j++) {
                if (view.getEntityType(j, i) instanceof Wall) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(i * 10, j * 10, 10, 10);
                } else if (view.getEntityType(j, i) instanceof BadBeast) {
                    gc.setFill(Color.RED);
                    gc.fillOval(i * 10, j * 10, 10, 10);
                } else if (view.getEntityType(j, i) instanceof GoodBeast) {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(i * 10, j * 10, 10, 10);
                } else if (view.getEntityType(j, i) instanceof BadPlant) {
                    gc.setFill(Color.YELLOW);
                    gc.fillOval(i * 10, j * 10, 10, 10);
                } else if (view.getEntityType(j, i) instanceof GoodPlant) {
                    gc.setFill(Color.GREEN);
                    gc.fillOval(i * 10, j * 10, 10, 10);
                } else if (view.getEntityType(j, i) instanceof MasterSquirrel) {
                    gc.setFill(Color.BROWN);
                    gc.fillRect(i * 10, j * 10, 10, 10);
                } else if (view.getEntityType(j, i) instanceof MiniSquirrel) {
                    gc.setFill(Color.FIREBRICK);
                    gc.fillRect(i * 10, j * 10, 10, 10);
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
        this.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.W) {
                            str[0] = "5";
                        } else if (keyEvent.getCode() == KeyCode.A) {
                            str[0] = "1";
                        } else if (keyEvent.getCode() == KeyCode.S) {
                            str[0] = "2";
                        } else if (keyEvent.getCode() == KeyCode.D) {
                            str[0] = "3";
                        } else if (keyEvent.getCode() == KeyCode.J){
                            str[0] = "mini";
                            str[1] = "100";
                        }
                    }
                }
        );
        for (int i = 0; i < commandTypeInfos.length; i++) {
            if (str.equals(commandTypeInfos[i].getName())) {

                Class<?>[] paramsClass = commandTypeInfos[i].getParamTypes();
                Object[] params = new Object[paramsClass.length];

                if (paramsClass.length > 0) {
                    for (int j = 0; j < paramsClass.length; j++) {
                        Class<?> c = paramsClass[j];

                        if (c.equals(int.class)) {
                            params[j] = new Integer(str[j + 1]);
                        } else if (c.equals(XY.class)) {
                            switch (commandTypeInfos[i].getName()) {
                                case "1":
                                    params[j] = new XY(0, -1);
                                    break;
                                case "2":
                                    params[j] = new XY(1, 0);
                                    break;
                                case "3":
                                    params[j] = new XY(0, 1);
                                    break;
                                case "5":
                                    params[j] = new XY(-1, 0);
                                    break;
                            }
                        }
                    }
                }
                inputBuffer = new Command(commandTypeInfos[i], params);
            }
        }
    }

    public Command getCommand() {
        Command temp = inputBuffer;
        inputBuffer = null;
        return temp;
    }
}
