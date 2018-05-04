package com.jdroids.team7026.ftc2017.auto.actions;

/**
 * An interface that describes an iterative action
 */

public interface Action {
    /**
     * Returns whether or not the code has finished execution
     *
     * @return boolean
     */
    public abstract boolean isFinished();

    /**
     * Iterative logic lives here
     */
    public abstract void update();

    /**
     * code to be run after the action finishes (mostly used for clean up)
     */
    public abstract void done();

    /**
     * Code to be run when the action is started, normally for setup
     */
    public abstract void start();
}
