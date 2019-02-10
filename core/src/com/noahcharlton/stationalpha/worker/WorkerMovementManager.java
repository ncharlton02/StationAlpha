package com.noahcharlton.stationalpha.worker;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.noahcharlton.stationalpha.worker.pathfinding.Path;
import com.noahcharlton.stationalpha.worker.pathfinding.WorldGraph;
import com.noahcharlton.stationalpha.world.Tile;

import java.util.Optional;

public class WorkerMovementManager {

    private final WorldGraph worldGraph;
    private final WorkerAI workerAI;
    private final Worker worker;

    private Optional<Tile> targetTile = Optional.empty();

    WorkerMovementManager(WorkerAI workerAI, Worker worker) {
        this.workerAI = workerAI;
        this.worker = worker;
        this.worldGraph = new WorldGraph(worker.getWorld());
    }

    public void update(){
        if(!targetTile.isPresent())
            return;
        System.out.println("t");

        Tile target = targetTile.get();
        Path path = worldGraph.generatePath(worker.getTileOn(), target);

        path.calcPathSync().ifPresent(this::moveAlongPath);
    }

    private void moveAlongPath(GraphPath<Tile> tiles) {
        if(tiles.getCount() == 0)
            return;

        Tile nextTile = getTargetTileFromPath(tiles);

        moveToTile(nextTile);
    }

    Tile getTargetTileFromPath(GraphPath<Tile> tiles) {
        Tile nextTile = tiles.get(0);

        if(worker.getTileOn().equals(nextTile) && tiles.getCount() > 1)
            nextTile = tiles.get(1);

        return nextTile;
    }

    private void moveToTile(Tile nextTile) {
        Tile current = worker.getTileOn();

        int diffX = nextTile.getX() - current.getX();
        int diffY = nextTile.getY() - current.getY();
        if(Math.abs(diffX) > 0){
            moveWorkerRelative((int) Math.signum(diffX), 0);
        }else if(Math.abs(diffY) > 0){
            moveWorkerRelative(0, (int) Math.signum(diffY));
        }else{
            moveToTileOrigin();
        }
    }

    void moveToTileOrigin() {
        if(worker.getPixelX() % Tile.TILE_SIZE > 0){
            moveWorkerRelative(-1, 0);
        }else if(worker.getPixelY() % Tile.TILE_SIZE > 0){
            moveWorkerRelative(0, -1);
        }
    }

    void moveWorkerRelative(int x, int y) {
        int newX = worker.getPixelX() + x;
        int newY = worker.getPixelY() + y;

        worker.setPixelX(newX);
        worker.setPixelY(newY);
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = Optional.ofNullable(targetTile);
    }

    boolean onTargetTile() {
        return targetTile.map(tile -> tile.equals(worker.getTileOn())).orElse(true);
    }
}
