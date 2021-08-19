package com.sp.fc.user.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "sp_user_authority")
@IdClass(SpAuthority.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class SpAuthority implements GrantedAuthority {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Id
    private String authority;

}
