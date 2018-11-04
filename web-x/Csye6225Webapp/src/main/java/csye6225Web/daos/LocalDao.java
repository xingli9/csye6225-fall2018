package csye6225Web.daos;

import org.springframework.web.multipart.MultipartFile;

public interface LocalDao {
    String saveFile(MultipartFile file);
}
