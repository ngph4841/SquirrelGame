package de.hsa.games.fatsquirrel;


public abstract class Game {
    private State state;

    protected Game() {
    }

    protected abstract void render() throws Exception;//Spielzustand auf ausgabemedium

    protected abstract void processInput() throws Exception;//verarbeitet Benutzereingaben

    protected abstract void update() throws Exception;//ver�ndert akt. Spielzustand

    public void run() throws Exception {
        while (true) {
            render();
            processInput();
            update();
        }
    }

    public void runLive() throws  Exception{}

    public void bufferInput() throws Exception{}

    public State getState(){return state;}
}
