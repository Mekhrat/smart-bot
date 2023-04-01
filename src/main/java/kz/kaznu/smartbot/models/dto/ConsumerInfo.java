package kz.kaznu.smartbot.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsumerInfo {
    private String name;
    private String phone;
    private String address;
    private String index;
}
