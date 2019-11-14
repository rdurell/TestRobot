/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.core238.autonomous;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.BaseCommand;
import frc.robot.commands.IAutonomousCommand;

/**
 * Add your docs here.
 */
public class AutonomousModesReader {
    private IAutonomousModeDataSource dataSource;

    public AutonomousModesReader(IAutonomousModeDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public HashMap<String, CommandGroup> getAutonmousModes() {
        String classPath = "frc.robot.commands.";
        HashMap<String, CommandGroup> autoModes = new HashMap<>();

        List<AutonomousModeDescriptor> modeDescriptors = getAutonomousModeDescriptors();

        modeDescriptors.forEach(modeDescriptor -> {
            String name = modeDescriptor.getName();
            CommandGroup commands = new CommandGroup();

            modeDescriptor.getCommands().forEach(commandDescriptor -> {

                String commandName = commandDescriptor.getName();
                String className = classPath + commandName;

                // create a command object
                IAutonomousCommand autoCommand = null;
                try {
                    autoCommand = (IAutonomousCommand) Class.forName(className).getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException
                        | ClassNotFoundException e) {
                    frc.core238.Logger
                            .Debug("AutonomousModesReader.getAutonmousModes unable to instantiate " + className);
                }

                //call set parameters
                autoCommand.setParameters(commandDescriptor.getParameters());
                autoCommand.setIsAutonomousMode(true);

                // add to list
                commands.addSequential((BaseCommand)autoCommand);
            });

            //add to dictionary
            autoModes.put(name, commands);
        });

        return autoModes;
    }

    private List<AutonomousModeDescriptor> getAutonomousModeDescriptors(){
        String json = dataSource.getJson();
        
        Type listType = new TypeToken<ArrayList<AutonomousModeDescriptor>>() {}.getType();
        List<AutonomousModeDescriptor> modeDescriptors = new Gson().fromJson(json, listType);   
        return modeDescriptors;
    }
    
}
