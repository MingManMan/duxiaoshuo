package com.beibei.mingmanman.readxiaoshuo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mingmanman on 2016/10/25.
 */
public class ComTool {
    public static boolean ping(String zhandian_ming) {
        String result = null;
        try {
            Log.v("testcrab", zhandian_ming);
            Process p                 = Runtime.getRuntime().exec("ping -c 3 -w 400 www.sina.com.cn");// ping1次
            // 读取ping的内容，可不加。
            InputStream input         = p.getInputStream();
            BufferedReader in         = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content            = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
                Log.v("testcrab", "result content : " + content);
            }
            //Log.v("testcrab", "result content : " + stringBuffer.toString());
            // PING的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "successful~";
                return true;
            } else {
                result = "failed~ cannot reach the IP address";
            }
        } catch (IOException e) {
            result = "failed~ IOException";
        } catch (InterruptedException e) {
            result = "failed~ InterruptedException";
        } finally {
            Log.v("testcrab", "result = " + result);
        }
        return false;
    }
}
