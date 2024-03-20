import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class UI implements ActionListener, GameOverHandler {
    private final static int N = Board.N;
    private final JFrame frame = new JFrame();
    private final JButton[] buttons = new JButton[N * N];
    private final Game game;
    private final Executor executor;
    private boolean isGameOver;

    UI(Game game){
        this.game = game;
        game.setGameIsOverHandler(this);
        executor = Executors.newSingleThreadExecutor();
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.getContentPane().setBackground(Color.lightGray);
        frame.setTitle("Tic Tac Toe");
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(N,N));
        buttonPanel.setBackground(Color.gray);
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                var button = new JButton();
                button.setActionCommand(String.format("%d%d", row, col));
                buttonPanel.add(button);
                button.setFont(new Font("Ink Free", Font.BOLD, 120));
                button.setFocusable(false);
                button.addActionListener(this);
                buttons[row * N + col] = button;
            }
        }
        frame.add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var button = (JButton) e.getSource();
        var actionCommand = button.getActionCommand();
        var row = Integer.parseInt(actionCommand.substring(0,1));
        var col = Integer.parseInt(actionCommand.substring(1,2));
        button.setText("X");
        button.setForeground(Color.red);
        button.setEnabled(true);
        game.doHumanMoveTo(new Pos(row, col));
        if (!isGameOver) {
            executor.execute(() -> {
                Pos pos = game.doAIMove();
                EventQueue.invokeLater(() -> uiShowAIStep(pos));
            });
        }
    }

    private void uiShowAIStep(Pos pos) {
        var button = buttons[pos.getRow() * N + pos.getCol()];
        button.setText("O");
        button.setForeground(Color.blue);
        button.setEnabled(true);
    }

    @Override
    public void handlerGameIsOver(Game game, Seed winner) {
        isGameOver = true;
        EventQueue.invokeLater(() -> uiHandleGameIsOver(winner));
    }

    private void uiHandleGameIsOver(Seed winner) {
        for (var i = 0; i < N * N; i++) {
            buttons[i].setEnabled(false);
        }
        var message = "???";
        switch (winner) {
            case Empty -> message = "НИЧЬЯ";
            case O -> message = "Победил AI";
            case X -> message = "Вы победили";
        }
        JOptionPane.showMessageDialog(frame,message);
        reset();
    }

    private void reset() {
        for (var i = 0; i < N * N; i++) {
            var button = buttons[i];
            button.setText("");
            button.setEnabled(true);
        }
        isGameOver = false;
        game.reset();
    }
}