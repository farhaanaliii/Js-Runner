package com.github.farhanaliofficial.jsrun.Utils;

import android.content.SharedPreferences;
import com.github.farhanaliofficial.jsrun.Handler.Constants;
import android.content.Context;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import android.widget.Toast;

public class Utils{
    public static boolean getBoolean(Context context, boolean defaults){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.FILE_EXTRACTED, defaults);
    }
    public static void putBoolean(Context context, boolean bool){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.FILE_EXTRACTED,bool);
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
    public static void saveFile(String path, String content, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html>\n<head>\n<head>\n<meta charset='utf-8'>\n<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n<title>Code Editor - Farhan Ali</title>\n</head>\n<body>\n");
        sb.append("<script>\n");
        sb.append(readFile(new File(context.getFilesDir(), "js/eruda.min.js")));
        sb.append("</script>\n");
        sb.append("<script>eruda.init();\neruda.show();</script>\n");
        sb.append("<script>\n");
        sb.append(content);
        sb.append("</script>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        try {
            File ff = new File(context.getFilesDir(), "index.html");
            if (ff.createNewFile()) {

            }
            FileWriter fw = new FileWriter(ff.getAbsolutePath());
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            Log.d("index.html", e.toString());
            Toast.makeText(context,e.toString(),2).show();
        }
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            Toast.makeText(context,e.toString(),2).show();
        }
	}
}
