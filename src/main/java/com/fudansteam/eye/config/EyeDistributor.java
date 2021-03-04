package com.fudansteam.eye.config;

import com.fudansteam.eye.Eye;

import java.io.*;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-03-03 08:08:10
 * Copyright 2021 HDU_IES. All rights reserved.
 */
public class EyeDistributor {
    
    private static File configFile;
    
    static {
        File[] files = new File(System.getProperty("user.dir")).listFiles();
        for (File file : files) {
            if (file.isDirectory() && "mods".equals(file.getName())) {
                configFile = new File(file.getAbsolutePath() + File.separator + "Eye.txt");
                break;
            }
        }
    }
    
    public static void save() {
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            String data = getConfig();
            FileWriter fileWriter = new FileWriter(configFile, false);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void load() {
        if (configFile.exists()) {
            try {
                FileReader fileReader = new FileReader(configFile);
                StringBuilder config = new StringBuilder();
                int ch;
                while ((ch = fileReader.read()) != -1) {
                    config.append((char) ch);
                }
                fileReader.close();
                if (config.toString().length() == 0) {
                    return;
                }
                setConfig(config.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void setConfig(String config) {
        String[] splits = config.split("\n");
        EyeConfig.distance = Integer.parseInt(splits[0].split("=")[1]);
        EyeConfig.warnDistance = Integer.parseInt(splits[1].split("=")[1]);
        EyeConfig.superEye = Boolean.parseBoolean(splits[2].split("=")[1]);
        Eye.originGamma = Double.parseDouble(splits[3].split("=")[1]);
    }
    
    private static String getConfig() {
        return "distance=" + EyeConfig.distance + "\n" +
                "warnDistance=" + EyeConfig.warnDistance + "\n" +
                "superEye=" + EyeConfig.superEye + "\n" +
                "originGamma=" + Eye.originGamma + "\n";
    }
    
}
