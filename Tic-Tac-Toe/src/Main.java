import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Main {

    JFrame f = new JFrame();
    JLabel xScoreLabel;
    JLabel oScoreLabel;
    int xScore = 0;
    int oScore = 0;
    boolean isXPlayerTurn = true; // Track whose turn it is

    JLabel optionLabel = new JLabel("Two player option", JLabel.CENTER);

    JPanel panelTable = new JPanel();

    JButton[][] table;

    void initGame() {

        f.setTitle("Ticky Tac Toes");

        f.setSize(500, 500);

        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table = new JButton[3][3];

        panelTable.setLayout(new GridLayout(3, 3));

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                table[i][j] = new JButton(" ");
                table[i][j].setFont(new Font("Arial", Font.BOLD, 64));
                table[i][j].addActionListener(new ButtonClickListener(i, j));
                panelTable.add(table[i][j]);
            }
        }

        Container c = f.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(optionLabel, BorderLayout.NORTH);
        c.add(panelTable, BorderLayout.CENTER);

        xScoreLabel = new JLabel(Integer.toString(xScore));
        xScoreLabel.setFont(xScoreLabel.getFont().deriveFont(24.0f));
        xScoreLabel.setBounds(108, 35, 24, 24);

        oScoreLabel = new JLabel(Integer.toString(oScore));
        oScoreLabel.setFont(oScoreLabel.getFont().deriveFont(24.0f));
        oScoreLabel.setBounds(375, 35, 24, 24);

        f.setVisible(true);
    }

    class ButtonClickListener implements ActionListener {
        int x, y;

        public ButtonClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            if (isXPlayerTurn) {
                clickedButton.setText("X");
            } else {
                clickedButton.setText("O");
            }

            // Check for win or tie
            if (checkTie()) {
                JOptionPane.showMessageDialog(f, "It's a tie!");
                resetGame();
            } else if (checkWin()) {
                JOptionPane.showMessageDialog(f, (isXPlayerTurn ? "X" : "O") + " is your Tic Tac Champion! Get good");
                resetGame();
            } else {
                isXPlayerTurn = !isXPlayerTurn;
            }
        }
    }
    
    boolean checkTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (table[i][j].getText().equals(" ")) {
                    return false;
                }
            }
        }
        return true; // All cells filled, game is a tie
    }

    boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (table[i][0].getText().equals(table[i][1].getText()) && 
                table[i][1].getText().equals(table[i][2].getText()) &&
                !table[i][0].getText().equals(" ")) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (table[0][j].getText().equals(table[1][j].getText()) && 
                table[1][j].getText().equals(table[2][j].getText()) &&
                !table[0][j].getText().equals(" ")) {
                return true;
            }
        }

        // Check diagonals
        if ((table[0][0].getText().equals(table[1][1].getText()) && 
             table[1][1].getText().equals(table[2][2].getText()) &&
             !table[0][0].getText().equals(" ")) ||
            (table[0][2].getText().equals(table[1][1].getText()) && 
             table[1][1].getText().equals(table[2][0].getText()) &&
             !table[0][2].getText().equals(" "))) {
            return true;
        }

        return false; 
    }

   

    void resetGame() {
        // Reset the game state
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                table[i][j].setText(" ");
            }
        }
        isXPlayerTurn = true; // X starts the next game
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.initGame();
    }
}
