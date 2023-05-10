//package com.nimatnemat.nine.domain.image;
//
//
//import com.mongodb.client.gridfs.GridFSBucket;
//import com.mongodb.client.gridfs.model.GridFSUploadOptions;
//import org.bson.Document;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Service
//public class ImageUploadService {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private GridFSBucket gridFSBucket;
//
//    public String storeFile(MultipartFile file) throws IOException {
//        if (file.isEmpty()) {
//            throw new RuntimeException("업로드할 파일을 선택해주세요.");
//        }
//
//        try (InputStream inputStream = file.getInputStream()) {
//            GridFSUploadOptions options = new GridFSUploadOptions()
//                    .metadata(new Document("contentType", file.getContentType()));
//
//            ObjectId fileId = gridFSBucket.uploadFromStream(file.getOriginalFilename(), inputStream, options);
//            return fileId.toHexString();
//        }
//    }
//}
