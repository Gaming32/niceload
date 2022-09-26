package io.github.gaming32.niceload.api;

public final class LoadTask {
    private final String name;
    private int progress;
    private int maxProgress;
    private String description = "";

    public LoadTask(String name, int maxProgress) {
        this.name = name;
        this.progress = 0;
        this.maxProgress = maxProgress;
    }

    public String getName() {
        return name;
    }

    public int getProgress() {
        return progress;
    }

    public LoadTask setProgress(int progress) {
        this.progress = progress;
        NiceLoad.attemptRender();
        return this;
    }

    public LoadTask addProgress(int progress) {
        this.progress += progress;
        NiceLoad.attemptRender();
        return this;
    }

    public LoadTask addProgress() {
        progress++;
        NiceLoad.attemptRender();
        return this;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public LoadTask setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        NiceLoad.attemptRender();
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LoadTask setDescription(Object description) {
        if (description == null) {
            this.description = "";
        } else {
            this.description = description.toString();
        }
        NiceLoad.attemptRender();
        return this;
    }

    public LoadTask finish() {
        progress = maxProgress;
        NiceLoad.attemptRender();
        return this;
    }

    public boolean finished() {
        return progress >= maxProgress;
    }

    public float getProgress01() {
        return progress / (float)maxProgress;
    }
}
