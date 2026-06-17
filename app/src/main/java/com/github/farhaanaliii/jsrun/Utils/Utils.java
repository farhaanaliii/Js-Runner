package com.github.farhaanaliii.jsrun.Utils;

import android.content.SharedPreferences;
import com.github.farhaanaliii.jsrun.Handler.Constants;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import android.widget.Toast;

public class Utils{
    public static boolean getBoolean(Context context, String name, boolean defaults){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(name, defaults);
    }
    public static void putBoolean(Context context, String name, boolean bool){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, bool);
        editor.apply();
    }
    public static String readFile(File f) {
        if (!f.exists()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    public static void writeFile(String content, String path){
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
        }
    }
}
