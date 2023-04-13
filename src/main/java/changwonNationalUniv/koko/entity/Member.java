package changwonNationalUniv.koko.entity;

import changwonNationalUniv.koko.enums.Role;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 30)
    private String userId;

    @Column(length = 100)
    private String password;

    @Column(length = 20)
    private String mobile;

    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    @Column(nullable = false, length = 100)
    private String email;


    @Builder
    public Member(String name, String userId, String password, String mobile, String email) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
    }



}
