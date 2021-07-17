package com.game.board;

import com.game.common.Constants;
import com.game.model.Ball;
import com.game.model.Bar;
import com.game.model.Brick;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends Canvas implements Runnable, KeyListener {

    private int currentKeyEvent = -1;
    private Bar bar;
    private Ball ball;
    private List<Brick> brickList = new ArrayList();

    GameCanvas() {
        setBounds(0,0,Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        addKeyListener(this);
    }

    public void start() {
        Thread t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    @Override
    public void run() {
        init();
        while(true) {
            update();
            BufferStrategy buff = getBufferStrategy();
            if(buff == null) {
                createBufferStrategy(2);
                continue;
            }
            Graphics2D g = (Graphics2D) buff.getDrawGraphics();
            render(g);
            g.dispose();
            buff.show();
            Toolkit.getDefaultToolkit().sync();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void init() {
        bar = new Bar( Constants.WINDOW_WIDTH/2 - Constants.BAR_WIDTH/2,
                Constants.WINDOW_HEIGHT - Constants.BAR_HEIGHT - 10, Constants.BAR_WIDTH, Constants.BAR_HEIGHT, 0);
        ball = new Ball( bar.getX() + (bar.getWidth()/2) - Constants.BALL_WIDTH/2,
                bar.getY() - Constants.BALL_HEIGHT - 2, Constants.BALL_WIDTH, Constants.BALL_HEIGHT, -2, -2);

        int numberOfRows = 5;
        int numberOfBranchPerColumn = (Constants.WINDOW_WIDTH / Constants.BRICK_WIDTH);
        int x=3, y=1;
        for (int i=1 ; i<=numberOfRows ; i++) {
            for (int j=1 ; j<=numberOfBranchPerColumn ; j++) {
                brickList.add( new Brick(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT) );
                x += Constants.BRICK_WIDTH+1;
            }
            x = 3;
            y += Constants.BRICK_HEIGHT+1;
        }
    }


    private void render(Graphics2D g2d) {

        g2d.setColor(Constants.COLOR_GAME_BACKGROUND);
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        g2d.setColor(Constants.COLOR_BAR);
        g2d.fillRect(bar.getX(), bar.getY(), bar.getWidth(), bar.getHeight());

        g2d.setColor(Constants.COLOR_BALL);
        g2d.fillOval(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());

        g2d.setColor(Constants.COLOR_BRICK);
        for (int k=0 ; k<brickList.size() ; k++) {
            g2d.fillRect( brickList.get(k).getX(), brickList.get(k).getY(), brickList.get(k).getWidth(), brickList.get(k).getHeight() );
        }

    }

    private void update() {
        moveBall();
        moveBar();
        checkBricksCollision();
    }

    private void moveBar() {
        if( currentKeyEvent==KeyEvent.VK_LEFT && bar.getX() > 0 ) {
            bar.setDx(-2);
        } else if ( currentKeyEvent==KeyEvent.VK_RIGHT && bar.getX()+bar.getWidth() < Constants.WINDOW_WIDTH) {
            bar.setDx(2);
        } else {
            bar.setDx(0);
        }
        bar.move();
    }

    private void moveBall() {
        if( ball.getX()<=0 || ball.getX()>=Constants.WINDOW_WIDTH ) {
            ball.setDx( -ball.getDx() );
        } else if( new Rectangle(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight()).intersects(
                new Rectangle(bar.getX(), bar.getY(), bar.getWidth(), bar.getHeight()/2)) || ball.getY()<=0) {
            ball.setDy( -ball.getDy() );
        }
        ball.move();
    }

    private void checkBricksCollision() {
        for (int k=0 ; k<brickList.size() ; k++) {
            if( new Rectangle(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight()).intersects(
                    new Rectangle(brickList.get(k).getX(), brickList.get(k).getY(), brickList.get(k).getWidth(), brickList.get(k).getHeight()/2)) ) {
                ball.setDy( -ball.getDy() );
                brickList.remove( brickList.get(k) );
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentKeyEvent = e.getKeyCode();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentKeyEvent = -1;
    }

}
