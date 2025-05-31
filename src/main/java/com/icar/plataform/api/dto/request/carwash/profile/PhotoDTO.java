package com.icar.plataform.api.dto.request.carwash.profile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PhotoDTO {
    private String photoUrl;

    public PhotoDTO(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}
