package br.com.marcosalexandre.desafiobackendcrdc.entity;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

public class MyMultipartRequest {
    @Schema(type = "string", format = "binary")
	private MultipartFile file;

    public MyMultipartRequest(){
        
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
