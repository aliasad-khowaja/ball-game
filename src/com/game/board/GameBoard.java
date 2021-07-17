package com.game.board;

import com.game.common.Constants;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JFrame {

    private GameCanvas canvas;

    public GameBoard() {
        initFrame();
    }

    private void initFrame() {
        this.setTitle("Ball and Bricks");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH+10, Constants.WINDOW_HEIGHT+30));
        this.pack();
        this.setLocationRelativeTo(null);

        canvas = new GameCanvas();
        this.add(canvas);
        canvas.requestFocus();
        canvas.start();
    }

}
