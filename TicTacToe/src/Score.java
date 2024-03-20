public class Score {
    private final Pos pos;
    private final int scorePoints;

    Score(Pos pos, int scorePoints){
        this.pos = pos;
        this.scorePoints = scorePoints;
    }

    Pos getPos(){
        return pos;
    }

    public int getScorePoints() {
        return scorePoints;
    }
}
