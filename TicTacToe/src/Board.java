import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Board {
    /**
     * Вспомогательный интерфейс. Используется для forEachElement.
     */
    private interface Action {
        void doAction(int row, int col);
    }

    public static final int N = 3; // Поле 3х3
    private final Seed[][] cells = new Seed[N][N];
    private final Set<Pos> freePositions = new HashSet<>(N * N);

    public Board() {
        forEachElement((row,col) -> {
            cells[row][col] = Seed.Empty;
            freePositions.add(new Pos(row, col));
        });
    }
    private static void forEachElement(Action action){
        for (int row = 0; row < N; row++){
            for (int col = 0; col < N; col++){
                action.doAction(row,col);
            }
        }
    }

    public Set<Pos> getFreePositions() {
        return Collections.unmodifiableSet(freePositions);
    }

    public Seed getSeedAtPosition(Pos pos){
        return cells[pos.getRow()][pos.getCol()];
    }

    public void setSeedAtPosition(Pos pos, Seed seed){
        Seed currentSeed = cells[pos.getRow()][pos.getCol()];
        if (currentSeed != Seed.Empty && seed != Seed.Empty) {
            throw new GameException(String.format("Позиция %s уже занята!",pos));
        }
        if (currentSeed == seed) return;
        cells[pos.getRow()][pos.getCol()] = seed;
        if (seed == Seed.Empty) freePositions.add(pos);
        else freePositions.remove(pos);
    }

    /**
     * Подсчет баллов для выявления победителя или выбора наилучшего хода в
     * miniMax алгоритме
     * @return
     */
    public GameStatus getGameStatus() {
        final int[] rowScores = new int[N];
        final int[] colScores = new int[N];
        final int[] diagMajorScores = new int[N];
        final int[] diagMinorScores = new int[N];
        forEachElement((row,col) -> {
            Seed seed = this.cells[row][col];
            int delta = getDelta(seed);
            rowScores[row] += delta;
            colScores[col] += delta;
            if (row == col) diagMajorScores[0] += delta;
            if (row == N - col - 1) diagMinorScores[0] += delta;
        });

        /*Выясняем, кто победил. Если сумма по какой-то строке, столбцу или диагонали
         равна N*delta, то в этой строке, столбце или диагонале размещено
         N одинаковых знаков - крестиков или ноликов:*/

        for (Seed seed : new Seed[]{Seed.O, Seed.X}) {
            final int winPoints = N * getDelta(seed);
            for (int i = 0; i < N ; i++){
                if (rowScores[i] == winPoints || colScores[i] == winPoints) return new GameStatus(true,seed);
            }
            if (diagMajorScores[0] == winPoints || diagMinorScores[0] == winPoints) return new GameStatus(true, seed);

        }
        return new GameStatus(getFreePositions().isEmpty(), Seed.Empty);
    }

    private int getDelta(Seed seed) {
        if (seed == Seed.X) return 1;
        else if (seed == Seed.O) return -1;
        return 0;
    }

    /**
     * Метод использующийся для обновленя поля при рекурсивном вызове miniMax
     * @return - текущее поле со всеми ходами
     */
    public Board createFullCopy() {
        Board board = new Board();
        forEachElement((row, col) -> {
            Pos pos = new Pos(row, col);
            Seed seed = getSeedAtPosition(pos);
            board.setSeedAtPosition(pos,seed);
        });
        return board;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  | 0 | 1 | 2 |\n");
        sb.append("--+---+---+---+\n");
        forEachElement((row,col) -> {
            if (col == 0) {
                sb.append(row);
                sb.append(" | ");
            }
            sb.append(cells[row][col]);
            sb.append(" | ");
            if (col == N - 1) sb.append("\n--+---+---+---+\n");
        });
        return sb.toString();
    }
}
