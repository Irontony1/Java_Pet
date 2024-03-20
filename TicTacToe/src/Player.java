public class Player {
    private final Seed seed;
    private final Board board;
    private final MiniMax ai;

    Player(Seed seed, Board board, MiniMax ai){
        if (seed == Seed.Empty) throw new IllegalArgumentException("Ячейка не должна быть Empty");
        this.seed = seed;
        this.board = board;
        this.ai = ai;
    }

    public Pos moveToAI() {
        Pos pos = ai.findOptimalMovement(board, seed);
        board.setSeedAtPosition(pos, seed);
        return pos;
    }

    public void moveTo(Pos pos) {
        board.setSeedAtPosition(pos,seed);
    }
}
