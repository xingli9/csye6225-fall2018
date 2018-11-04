package csye6225Web.daos;

import org.springframework.web.multipart.MultipartFile;

public interface AWSS3 {
    String uploadToS3(MultipartFile file);

    void deleteToS3(String url);

}
