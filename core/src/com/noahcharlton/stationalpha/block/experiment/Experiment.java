package com.noahcharlton.stationalpha.block.experiment;

public class Experiment {

    enum Stage{PRE_START, IN_PROGRESS, FINISHED};

    private final String name;
    private final int length;

    private Stage stage;
    private int progress = 0;

    public Experiment(int length) {
        this.name = "ExpName";
        this.length = length;
        this.stage = Stage.PRE_START;
    }

    public void start(){
        if(stage != Stage.PRE_START)
            throw new UnsupportedOperationException();

        stage = Stage.IN_PROGRESS;
    }

    public void onTick() {
        if(stage != Stage.IN_PROGRESS)
            return;

        progress++;

        if(progress >= length) {
            stage = Stage.FINISHED;
        }
    }

    public int getProgress() {
        return progress;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public Stage getStage() {
        return stage;
    }
}
