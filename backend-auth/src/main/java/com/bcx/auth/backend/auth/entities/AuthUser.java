package com.bcx.auth.backend.auth.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(
        name = "auth_user"
)
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_auth_generator")
    @GenericGenerator(
            name = "sequence_auth_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "service_auth.auth_user_id_seq"),
                    @Parameter(name = "initial_value", value = "1000"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )

    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

}
