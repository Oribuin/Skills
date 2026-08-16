package dev.oribuin.skills.scheduler.task;

import dev.oribuin.skills.SkillsPlugin;

public interface ScheduledTask {
    
    /**
     * Cancels this task
     */
    void cancel();

    /**
     * @return true if this task is cancelled, false otherwise
     */
    boolean isCancelled();

    /**
     * @return the plugin that scheduled this task
     */
    SkillsPlugin getOwningPlugin();

    /**
     * @return true if this task is running, false otherwise
     */
    boolean isRunning();

    /**
     * @return true if this task is repeating, false otherwise
     */
    boolean isRepeating();

}
