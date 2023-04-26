package com.nimatnemat.nine.domain.Image;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public byte[] getImage(ObjectId objectId) throws IOException {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = gridFSBucket.openDownloadStream(objectId)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return outputStream.toByteArray();
    }
}