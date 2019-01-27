package com.noahcharlton.stationalpha.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;

public abstract class GuiComponent {

    protected static final BitmapFont font;

    static {
        if(Gdx.app != null)
            font = new BitmapFont(Gdx.files.internal("ui/font.fnt"),
                    Gdx.files.internal("ui/font.png"), false);
        else
            font = null;
    }

    private GuiComponent parent;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean visible = true;

    private ArrayList<GuiComponent> subGuis;

    public GuiComponent() {
        subGuis = new ArrayList<>();
    }


    void addGui(GuiComponent gui){
        if(gui.parent != null){
            throw new GdxRuntimeException("Gui cannot have more than one parent!");
        }

        subGuis.add(gui);
        gui.parent = this;
    }

    void render(SpriteBatch batch){
        if(!visible)
            return;

        update();
        drawBackground(batch);

        for(GuiComponent gui : subGuis){
            gui.render(batch);
        }

        drawForeground(batch);
    }

    protected void update(){}

    public boolean handleClick(int clickX, int clickY) {
        if(!visible)
            return false;

        boolean onGui = isPointOnGui(clickX, clickY);

        if(onGui)
            onClick();

        return onGui || handleClickOnSubGui(clickX, clickY);
    }

    protected void onClick(){}

    boolean isPointOnGui(int clickX, int clickY){
        return clickX > x && clickY > y && clickX < x  + width && clickY < y + height;
    }

    private boolean handleClickOnSubGui(int x, int y) {
        boolean onGui = false;

        for(GuiComponent gui : subGuis){
            if(gui.isVisible()) {
                if(gui.handleClick(x, y)){
                    onGui = true;
                }
            }
        }

        return onGui;
    }

    protected void setFontData(float size, Color color){
        font.setColor(color);
        font.getData().setScale(size);
    }

    protected abstract void drawBackground(SpriteBatch batch);

    protected abstract void drawForeground(SpriteBatch batch);

    protected void setX(int x) {
        this.x = x;
    }

    protected void setY(int y) {
        this.y = y;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible(){
        return visible;
    }
}
