package com.company.GB8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Map extends JPanel {
    public static final int MODE_H_V_A = 0;
    public static final int MODE_H_V_H = 1;

    int [][] field;
    int fieldSizeX;
    int fieldSizeY;
    int winLen;

    int cellHeight;
    int cellWidth;

    final int emptiness = 0;
    final int HUMAN = 1;
    final int AI = 2;


    boolean isInitialized = false;

    Map () {
        setBackground(Color.DARK_GRAY);

        goHuman();
        goAI();

    }

    void update (MouseEvent e){
        int cellX = e.getX()/ cellWidth;
        int cellY = e.getY()/ cellHeight;
        System.out.println("x"+ cellX + "y" + cellY);
        field [cellY][cellX] = HUMAN;
        goAI();

        repaint();

    }

    @Override
    protected void paintComponent (Graphics g){
        super.paintComponent(g);
        render(g);

    }




    void startNewGame (int mode, int fieldSizeX, int fieldSizeY, int winLen){
        System.out.println(mode + " " + fieldSizeX + " " + fieldSizeY + " " + winLen);

        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLen = winLen;
        field = new int [fieldSizeY][fieldSizeX];
        isInitialized = true;
        init();
        repaint();
    }

    void render (Graphics g){
        if (!isInitialized)return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        cellWidth = panelWidth / fieldSizeX;
        cellHeight = panelHeight / fieldSizeY;

        for (int i = 0; i < fieldSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == HUMAN) {
                    g.drawLine(cellHeight*j, cellWidth*i, cellHeight*(j+1), cellWidth*(i+1));
                    g.drawLine(cellHeight*j, cellWidth*(i+1), cellHeight*(j+1), cellWidth*i);
                }
                if (field[i][j] == AI) {
                    g.drawOval(cellWidth*j, cellHeight*i, cellWidth, cellHeight);
                }
            }

        }

    }

    void goAI() {
        Random random = new Random();

        int manager = 0;

        for (int i = 0; i <= fieldSizeY - winLen; i++) {

            if (manager>=1)break;
            for (int j = 0; j <= fieldSizeX - winLen; j++) {

                if (manager>=1)break;
                for (int k = 0; k < winLen; k++) {

                    if (manager>0)break;

                    for (int l = 0; l < winLen; l++) {
                        if (field[k + i][l + j] == AI) {
                            if ((k + i+1) <= field.length-1 && field[k + i + 1][l + j] == emptiness) {
                                field[k + i + 1][l + j] = AI;
                                manager++;
                                break;
                            }
                            if ((k + i-1) >= 0 && field[k + i - 1][l + j] == emptiness) {
                                field[k + i - 1][l + j] = AI;
                                manager++;
                                break;
                            }
                        }
                    }
                }
            }

        }

        while (true) {
            if (manager>=1)break;
            int y = random.nextInt(fieldSizeY);
            int x = random.nextInt(fieldSizeX);

            if (isCorrectGo(y, x)) {
                field[y][x] = AI;
                break;
            }
        }

    }

    boolean isCorrectGo(int y, int x) {
        if (x < fieldSizeX && x >= 0 && y < fieldSizeY && y >= 0 && field[y][x] == emptiness) return true;
        return false;
    }

    private boolean emptyDots(int field[][]) {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == emptiness) return true;
            }
        }
        return false;
    }

    private void init () {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = emptiness;
            }
        }
    }

    private boolean win(int c, int [][] mass) {

        int countX = 0;
        int countY = 0;
        int countD1 = 0;
        int countD2 = 0;

        for (int i = 0; i <= mass.length-winLen; i++) {
            for (int j = 0; j <= mass[i].length-winLen; j++) {
                for (int k = 0; k < winLen; k++) {
                    countX = 0;
                    countY = 0;
                    for (int l = 0; l < winLen; l++) {
                        if (field[k+i][l+j]==c)countX++;
                        else countX=0;

                        if (field[l+j][k+i]==c)countY++;
                        else countY=0;

                        if ((k==l) && field [k+i][l+j]==c)countD1++;
                        if ((k==l) && field [k+i][l+j]!=c)countD1=0;

                        if (k == ((winLen-1-l)) && field [k+i][l+j]==c) countD2++;
                        if (k == ((winLen-1-l)) && field [k+i][l+j]!=c) countD2=0;

                        if (countD1==winLen||countD2==winLen||countX==winLen||countY==winLen)return true;
                    }
                }
            }
        }
        return false;
    }

    void goHuman(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {

                update(e);

            }
        });
    }


}
