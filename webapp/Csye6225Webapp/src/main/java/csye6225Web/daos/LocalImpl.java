package csye6225Web.daos;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class LocalImpl implements LocalDao {

    private static String rootPath = "/Users/xingli/Desktop/neu/class/2018fall/CSYE6225cloudcomputing/test";

    private static LocalImpl instance = null;

    private LocalImpl() {
    }

    public static LocalImpl getInstance() {
        if (instance == null)
            instance = new LocalImpl();
        return instance;
    }

    @Override
    public String saveFile(MultipartFile receipt) {
        File path = new File(rootPath);
        String filename = receipt.getOriginalFilename();
        String filepath =  path+ File.separator + filename;
        File file = new File(path,filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            receipt.transferTo(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filepath;
    }
}
