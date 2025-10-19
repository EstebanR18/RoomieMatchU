package co.edu.ucentral.dto.multipart;

import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.RestForm;

import java.util.List;

public class FotosResidenciaForm {

    @RestForm("files")
    public List<FileUpload> files;

}