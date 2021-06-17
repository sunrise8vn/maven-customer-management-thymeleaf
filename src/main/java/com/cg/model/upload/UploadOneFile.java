package com.cg.model.upload;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadOneFile {
    private String description;

    // Upload files.
    private CommonsMultipartFile[] filesData;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommonsMultipartFile[] getFilesData() {
        return filesData;
    }

    public void setFilesData(CommonsMultipartFile[] filesData) {
        this.filesData = filesData;
    }
}
