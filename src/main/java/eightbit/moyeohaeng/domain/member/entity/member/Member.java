package eightbit.moyeohaeng.domain.member.entity.member;

import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "members")
@SQLDelete(sql = "UPDATE members SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	//TODO auth 개발 후 response와 log에 출력되지 않게 하기
	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false, length = 10)
	private String name;

	@Column
	private String profileImage;

	public void update(String name, String profileImage, String password) {
		if (name != null) {
			this.name = name;
		}
		if (profileImage != null) {
			this.profileImage = profileImage;
		}
		if (password != null) {
			this.password = password;
		}
	}

}
