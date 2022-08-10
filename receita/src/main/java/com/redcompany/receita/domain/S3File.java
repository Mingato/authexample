package com.redcompany.receita.domain;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by JoaoVictor on 03/09/15.
 */
public class S3File {

	static Logger sampLogger = Logger.getLogger("S3File");
	
    @Id
    private String id;

    public String name;

    private String contentType;

    @Transient
    private byte[] file;

    public String url;

    //==================================================================================================================
    //                                          Construtores
    //==================================================================================================================

    public S3File() {
    }

    public S3File(String id) {
        this.id = id;
    }

    //==================================================================================================================
    //                                          Domain Methods
    //==================================================================================================================

    /**
     * Create a new S3File and persist it.
     *
     * @return The created S3File.
     */
    public static S3File create(String fileName, String pathString, String contentType, String directory) throws IOException {
        S3File s3File = new S3File();

        s3File.name = fileName;
        s3File.contentType = contentType;

        s3File.fillContentType();
        Path path = Paths.get(pathString);
        byte[] fileBytes = Files.readAllBytes(path);
        s3File.file = fileBytes;
        s3File.url = "/arquivos/" + directory + "/" + s3File.name;
        
        return s3File;
    }

    @Transient
    public S3File fillContentType() {
        if (this.getContentType() == null && this.getFile() != null) {
            try {
                this.setContentType(URLConnection
                        .guessContentTypeFromStream(new ByteArrayInputStream(
                                this.getFile())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public static File convert(MultipartFile file, String arq) throws IOException {
    	//TODO:File convFile = new File("../webapps/arquivos/" + arq);
    	File convFile = new File("src/main/resources/webapp/arquivos/" + arq);
    	sampLogger.log(Level.INFO, "REDCOMPANY S3File getAbsolutePath: " + convFile.getAbsolutePath());
    	//convFile = new File(convFile.getAbsolutePath());
    	try{
    		convFile.createNewFile();
    	}catch(Exception e){
    		//convFile = new File("webapps/arquivos/" + arq);//para localhost
    		//convFile.createNewFile();
    	}
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        sampLogger.log(Level.INFO, "REDCOMPANY S3File filename: " + file.getOriginalFilename());
        sampLogger.log(Level.INFO, "REDCOMPANY S3File filePath: " + convFile.getCanonicalPath());
        fos.close();

        return convFile;
    }

    //==================================================================================================================
    //                                          Getters/Setters
    //==================================================================================================================


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
