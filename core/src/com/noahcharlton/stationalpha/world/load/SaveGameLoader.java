package com.noahcharlton.stationalpha.world.load;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.noahcharlton.stationalpha.StationAlpha;
import com.noahcharlton.stationalpha.world.World;

import java.util.Objects;

public class SaveGameLoader {

    private static void load(String xml, World world) {
        XmlReader.Element element = new XmlReader().parse(xml);

        verifyVersion(element);
        loadWorkers(element, world);
        loadInventory(element, world);
        loadWorld(element, world);
    }

    private static void loadWorkers(XmlReader.Element element, World world) {
        XmlReader.Element elements = Objects.requireNonNull(element.getChildByName("Workers"));

        WorkerLoader.load(elements, world);
    }

    private static void loadInventory(XmlReader.Element element, World world) {
        XmlReader.Element elements = Objects.requireNonNull(element.getChildByName("Inventory"));

        InventoryLoader.load(elements, world.getInventory());
    }

    private static void loadWorld(XmlReader.Element element, World world) {
        XmlReader.Element tiles = Objects.requireNonNull(element.getChildByName("Tiles"));

        WorldLoader.load(tiles, world);
    }

    static void verifyVersion(XmlReader.Element element) {
        XmlReader.Element saveInfo = Objects.requireNonNull(element.getChildByNameRecursive("SaveInfo"),
                "SaveInfo not found in save file!");

        String saveVersion = saveInfo.getAttribute("GameVersion", "Unknown");

        if(!saveVersion.equals(StationAlpha.VERSION)){
            throw new GdxRuntimeException("Failed to load game: invalid version. Local: " + StationAlpha.VERSION +
                    ". Save: " + saveVersion);
        }
    }

    public static void load(World world){
        if(Gdx.graphics == null) // Running in headless mode (a.k.a. in test mode)
            return;

        FileHandle handle = Gdx.files.external("/station-alpha/save.xml");

        load(handle.readString(), world);
    }
}
