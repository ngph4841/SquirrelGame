package de.hsa.games.fatsquirrel;

/**
 * The abstract Game class is used for the Game engine and is inherited by the GameImpl class.
 * A new Game Implementation can be created by inheriting this abstract class.
 * A User Interface should be used to interact with the user.
 * A State should be inside the Game class, so the game state can be updated.
 */
public abstract class Game {
    private State state;

    /**
     * Constructor of the class.
     * A State which contains the state of the game and a User Interface should be parameters.
     */
    protected Game() {
    }

    /**
     * This method is used to render a visual of the board onto a desired medium via a User Interface.
     * @throws Exception
     */
    protected abstract void render() throws Exception;//Spielzustand auf ausgabemedium

    /**
     * This method is used to process the input of the user through a desired medium via a User Interface.
     * @throws Exception
     */
    protected abstract void processInput() throws Exception;//verarbeitet Benutzereingaben

    /**
     * This method is used to update the game state.
     * @throws Exception
     */
    protected abstract void update() throws Exception;//ver�ndert akt. Spielzustand

    /**
     * This method is used to run the game loop.
     * @throws Exception
     */
    public void run() throws Exception {
        while (true) {
            render();
            processInput();
            update();
        }
    }

    /**
     * This method should be used with the bufferInput() method to update the game state
     * independently from the user inputs.
     * @throws Exception
     */
    public void runLive() throws  Exception{}

    /**
     * This method should be used with the bufferInput() method to update the game state
     * independently from the user inputs.
     * @throws Exception
     */
    public void bufferInput() throws Exception{}

    /**
     * This method returns the current state of the game.
     * @return state of the current game
     */
    public State getState(){return state;}
}
