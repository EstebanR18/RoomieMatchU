package co.edu.ucentral.dto.multipart;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class FotoPerfilForm {

    @RestForm("file")
    public FileUpload file;

}