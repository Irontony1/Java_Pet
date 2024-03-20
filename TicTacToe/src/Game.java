public class Game {
    private Board board;
    private Player playerHuman, playerAi;
    private GameOverHandler gameIsOverHandler;
    private Player nextPlayer;

    public Game() {
        reset();
    }

    public void reset() {
        board = new Board();
        MiniMax ai = new MiniMax();
        playerHuman = new Player(Seed.X, board, ai);
        playerAi = new Player(Seed.O, board, ai);
        this.nextPlayer = playerHuman;
    }
    public Board getBoard() {
        return board;
    }

    public void doHumanMoveTo(Pos pos) {
        checkPlayer(playerHuman);
        playerHuman.moveTo(pos);
        turnToTheOppositePlayer(playerAi);
    }

    public Pos doHumanMoveToAi() {
        checkPlayer(playerHuman);
        Pos pos = playerHuman.moveToAI();
        turnToTheOppositePlayer(playerAi);
        return pos;
    }

    public Pos doAIMove() {
        checkPlayer(playerAi);
        Pos pos = playerAi.moveToAI();
        turnToTheOppositePlayer(playerHuman);
        return pos;
    }

    public void setGameIsOverHandler(GameOverHandler gameIsOverHandler) {
        this.gameIsOverHandler = gameIsOverHandler;
    }

    private void turnToTheOppositePlayer(Player oppositePlayer) {
        GameStatus status = board.getGameStatus();
        if (status.isOver()) {
            nextPlayer = null;
            if (gameIsOverHandler != null) {
                gameIsOverHandler.handlerGameIsOver(this, status.getWinnerSeed());
            }
        } else nextPlayer = oppositePlayer;
    }

    private void checkPlayer(Player player) {
        if (nextPlayer != player) throw new IllegalArgumentException("Сейчас не ваш ход!");
    }
}
