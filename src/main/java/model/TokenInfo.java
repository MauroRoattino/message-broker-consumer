package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    private Boolean active;
    private Long exp;
    private String clientId;
    private List<String> scope = null;
}