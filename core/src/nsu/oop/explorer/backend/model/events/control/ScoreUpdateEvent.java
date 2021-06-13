package nsu.oop.explorer.backend.model.events.control;

import java.util.*;

public class ScoreUpdateEvent extends InfoEvent{
    private String score;
    public ScoreUpdateEvent(HashMap<String, Integer> scoreBoard) {
        super(3,0,0,0);
        score = "";
        for (Map.Entry<String, Integer> line : scoreBoard.entrySet()) {
            score += line.getKey() + " " + line.getValue() + "\n";
        }
    }

    public String score() {
        return score;
    }
}
