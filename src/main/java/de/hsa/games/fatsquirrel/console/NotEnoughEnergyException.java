package de.hsa.games.fatsquirrel.console;

/**
 *This Exception is thrown when a MasterSquirrel tries to birth a MiniSquirrel
 * and has not enough energy to birth the child.
 */
public class NotEnoughEnergyException extends Exception {
    public NotEnoughEnergyException() {
        super();
    }
}
