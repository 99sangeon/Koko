package changwonNationalUniv.koko.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 30)
    private String userId;

    @Column(length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String mobile;

    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    @Column(nullable = false, length = 100)
    private String email;

    @Embedded
    private Time time = new Time();

    @Builder
    public Member(String name, String userId, String password, String mobile, String email) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
    }

}
