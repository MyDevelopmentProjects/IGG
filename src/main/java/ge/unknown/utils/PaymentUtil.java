package ge.unknown.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PaymentUtil {

    public static boolean writeStringToFile(String name, String data, String location) {
        try {
            name = name.replaceAll("/", "_");
            String path = location + File.separator + "payments" + File.separator + name;
            File f = new File(path);
            if(!f.exists()){
                Files.write( Paths.get(path), data.getBytes(), StandardOpenOption.CREATE);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
