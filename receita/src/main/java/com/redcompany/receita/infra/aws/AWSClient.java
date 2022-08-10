package com.redcompany.receita.infra.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.redcompany.receita.domain.S3File;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author joaovictorchenccimarques
 * @since 12/02/15.
 */
public class AWSClient {

    public static void uploadS3(S3File s3file) {
        AmazonS3 s3client = new AmazonS3Client();
        try {
            String bucketName = "chatprofile";

            InputStream stream = new ByteArrayInputStream(s3file.getFile());
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(s3file.getFile().length);
            meta.setContentType(s3file.fillContentType().getContentType());
            s3client.putObject(new PutObjectRequest(bucketName, s3file.name,
                                                    stream, meta));

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which "
                               + "means your request made it "
                               + "to Amazon S3, but was rejected with an error response"
                               + " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which "
                               + "means the client encountered "
                               + "an internal error while trying to "
                               + "communicate with S3, "
                               + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
