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
    private static final CommandTypeInfo[] commandTypeInfos = GameCommandType.values();
    private static Command inputBuffer = null;
    private Canvas boardCanvas;
    private Label msgLabel;


    public FxUI(Parent parent, Canvas boardCanvas, Label msgLabel) {
        super(parent);
        this.boardCanvas = boardCanvas;
        this.msgLabel = msgLabel;
    }

    private void inputBuffer(KeyCode keyCode) {
        CommandTypeInfo cmdType = null;
        int MiniSquirrelBirthEnergy = 50;

        if(keyCode == KeyCode.M){
            msgLabel.setText("MasterSquirrel HP:");
            return;
        } else if (keyCode == KeyCode.W) {
            cmdType = GameCommandType.UP;
        } else if (keyCode == KeyCode.A) {
            cmdType = GameCommandType.LEFT;
        } else if (keyCode == KeyCode.S) {
            cmdType = GameCommandType.DOWN;
        } else if (keyCode == KeyCode.D) {
            cmdType = GameCommandType.RIGHT;
        } else if (keyCode == KeyCode.J) {
            cmdType = GameCommandType.SPAWN_MINI;
            msgLabel.setText("Congratulations, you just gave birth!");
        }

        if (cmdType != null) {
            Class<?>[] paramsClass = cmdType.getParamTypes();
            Object[] params = new Object[paramsClass.length];

            if (paramsClass.length > 0) {
                for (int j = 0; j < paramsClass.length; j++) {
                    Class<?> c = paramsClass[j];

                    if (c.equals(int.class)) {
                        params[j] = new Integer(MiniSquirrelBirthEnergy);
                    } else if (c.equals(XY.class)) {
                        switch (cmdType.getName()) {
                            case "1":
                                params[j] = new XY(-1, 0);
                                break;
                            case "2":
                                params[j] = new XY(0, 1);
                                break;
                            case "3":
                                params[j] = new XY(1, 0);
                                break;
                            case "5":
                                params[j] = new XY(0, -1);
                                break;
                        }
                    }
                }
            }
            inputBuffer = new Command(cmdType, params);
        }
    }


    public static FxUI createInstance(XY boardSize) {
        Canvas boardCanvas = new Canvas(boardSize.getX()*CELL_SIZE, boardSize.getY()*CELL_SIZE);
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
                        fxUI.inputBuffer(keyEvent.getCode());
                    }
                });
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
        int boardWidth = view.getSize().getX();
        int boardHeight = view.getSize().getY();

        for (int j = 0; j < boardHeight; j++) {
            for (int i = 0; i < boardWidth; i++) {
                if (view.getEntityType(i, j) instanceof Wall) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (view.getEntityType(i, j) instanceof BadBeast) {
                    gc.setFill(Color.RED);
                    gc.fillOval(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (view.getEntityType(i, j) instanceof GoodBeast) {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (view.getEntityType(i, j) instanceof BadPlant) {
                    gc.setFill(Color.YELLOW);
                    gc.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (view.getEntityType(i, j) instanceof GoodPlant) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (view.getEntityType(i, j) instanceof MasterSquirrel) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (view.getEntityType(i, j) instanceof MiniSquirrel) {
                    gc.setFill(Color.WHEAT);
                    gc.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
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

    @Override
    public void setMsg(String msg) {
        if(!msg.equals("")) {
            msgLabel.setText(msg);
        }
    }

    public Command getCommand() {
        Command temp = inputBuffer;
        inputBuffer = null;
        return temp;
    }
}
