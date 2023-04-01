package kz.kaznu.smartbot.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {

    private String name;
    private String email;
    private String password;
    private String rePassword;
}
