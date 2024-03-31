package com.github.farhanaliofficial.jsrun.Handler;

import android.content.res.*;
import java.io.*;
import android.app.*;
import android.content.*;
import android.util.Log;

public class CopyAssets{
    private Context ctx;
    private String fromPathDirectoryAssets;
    private String toDirectoryTarget;
    private String ErrorString;
    public CopyAssets(){

    }
    public CopyAssets(Context ctx, String fromPathDirectoryAssets, String toDirectoryTarget){
        this.ctx = ctx;
        this.fromPathDirectoryAssets = fromPathDirectoryAssets;
        this.toDirectoryTarget = toDirectoryTarget ;
    }

    public void setCtx(Context ctx)
    {
        this.ctx = ctx;
    }

    public Context getCtx()
    {
        return ctx;
    }

    public void setFromPathDirectoryAssets(String fromPathDirectoryAssets)
    {
        this.fromPathDirectoryAssets = fromPathDirectoryAssets;
    }

    public String getFromPathDirectoryAssets()
    {
        return fromPathDirectoryAssets;
    }

    public void setToDirectoryTarget(String toDirectoryTarget)
    {
        this.toDirectoryTarget = toDirectoryTarget;
    }

    public String getToDirectoryTarget()
    {
        return toDirectoryTarget;
    }

    public void setErrorString(String errorString)
    {
        this.ErrorString = errorString;
    }

    public String getErrorString()
    {
        return ErrorString;
    }
    public void start(){
        this.copyAssets(this.getFromPathDirectoryAssets(), this.getToDirectoryTarget());
    }
    private void copyAssets(String path, String outPath) {
        AssetManager assetManager = this.ctx.getAssets();
        String assets[];
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                Log.d("copyFile","path="+path+"\noutPath="+outPath);
                copyFile(path, outPath);
            } else {
                String fullPath = outPath + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    if (!dir.mkdir()) this.ErrorString += "No create external directory: " + dir ;
                for (String asset : assets) {
                    copyAssets(path + "/" + asset, outPath);
                }
            }
        } catch (IOException ex) {
            this.ErrorString += ex.toString();
        }
    }

    private void copyFile(String filename, String outPath) {
        AssetManager assetManager = this.ctx.getAssets();
        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open(filename);
            String newFileName = outPath + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            this.ErrorString += e.toString();
        }
    }
    String getResultError(){
        return this.ErrorString;
    }
}
