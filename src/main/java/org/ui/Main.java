package org.ui;

import java.util.List;
import javax.swing.*;
import org.example.domain.board.SquareBoard;
import org.example.domain.board.SquareBoardCreator;
import org.example.service.YutGameService;

public class Main {

    public static void main(String[] args) {
        SquareBoardCreator squareBoardCreator = new SquareBoardCreator();
        SquareBoard squareBoard = squareBoardCreator.initialize();

        DrawableBoard drawableBoard = DrawableBoard.fromSquareBoard(squareBoard);

        BoardView boardView = new BoardView(drawableBoard);

        YutGameService yutGameService = new YutGameService(2, 4, boardView.board);

        JFrame frame = new JFrame("Yut Game");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new FlowLayout());

        boardView.draw(frame);

        frame.setVisible(true);
    }
}