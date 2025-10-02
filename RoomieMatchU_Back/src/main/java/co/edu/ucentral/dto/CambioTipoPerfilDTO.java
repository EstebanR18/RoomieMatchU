package co.edu.ucentral.dto;

import co.edu.ucentral.entity.UserEntity;
import lombok.Data;

@Data
public class CambioTipoPerfilDTO {
    private UserEntity.PerfilTipo nuevoTipo;
}

