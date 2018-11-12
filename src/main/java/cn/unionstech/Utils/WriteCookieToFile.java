package cn.unionstech.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class WriteCookieToFile {

    public static void writeCookieToFile(String content, long time) {
        BufferedWriter bw = null;
        try {
            File file = new File("cookie.txt");
//            if (!file.exists()) {
            file.createNewFile();
//            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(String.valueOf(time) + ",");
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
