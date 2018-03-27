package me.jet315.houses.utils;

import me.jet315.houses.Core;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jet on 01/03/2018.
 */
public class LoadSchematics {

    /**
     * This file loads schematics into the /schematic file if it is empty
     */

    //Constructor
    public LoadSchematics(Core instance){

        /**
         * Create fodler if doesn't exist
         */
        File schematicFolder = new File(instance.getDataFolder(),"schematics");
        if(!schematicFolder.exists()){
            schematicFolder.mkdir();
        }

        /**
         * See if folder is empty, if it is, put scehmatics into it from jar
         */
        if(schematicFolder.list().length == 0){
            for(int i = 1; i<=6;i++){
                try {
                    System.out.println("here " + i);
                    InputStream resource = instance.getResource("schematics/house"+i+".schematic");
                    File outputFile = new File(instance.getDataFolder() + File.separator +"schematics/house"+i+".schematic");
                    FileUtils.copyInputStreamToFile(resource, outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
