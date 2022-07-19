package partners.meat.tms.api.v1.model;

import lombok.Data;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;



@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "tb_user")
public class UsersEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "pw")
    private String pw;

    @Builder
    public UsersEntity(String userId, String pw) {
        this.userId = userId;
        this.pw = pw;
    }

}
