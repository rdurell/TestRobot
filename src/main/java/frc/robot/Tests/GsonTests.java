/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Tests;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.core238.autonomous.*;
import org.junit.*;

/**
 * Add your docs here.
 */
public class GsonTests {
    public static void main(String[] args){

    }

    @Test
    public void test1(){
        /*         
        [
            {
                "name": "Some auto mode",
                "commands": [
                    {
                        "name": "DriveStraightNavBoard",
                        "parameters": [
                            "1",
                            "2"
                        ]
                    },
                    {
                        "name": "DriveStraightVision",
                        "parameters": [
                            "3",
                            "4"
                        ]
                    }
                ]
            }
        ] 
        */

        String json = "[{\"name\":\"Some auto mode\",\"commands\":[{\"name\":\"DriveStraightNavBoard\",\"parameters\":[\"1\",\"2\"]},{\"name\":\"DriveStraightVision\",\"parameters\":[\"3\",\"4\"]}]}]";

        Type listType = new TypeToken<ArrayList<AutonomousModeDescriptor>>() {}.getType();
        List<AutonomousModeDescriptor> list = new Gson().fromJson(json, listType);   

        System.out.println(list.size());

        IAutonomousModeDataSource dataSource = new JsonStringAutonomousModeDataSource(json);
        AutonomousModesReader reader = new AutonomousModesReader(dataSource);
        var modes = reader.getAutonmousModes();
        Assert.assertEquals("Incorrect number of auto modes", 2, modes.size());

        CommandGroup cg = modes.get("Some auto mode");
        Assert.assertNotNull(cg);
        // in a real API, you would be able to get a collection of the commands added to the command group
        //Assert.assertEquals("Incorrect number of commands", 2, cg.getCommands().size());

    }

}
