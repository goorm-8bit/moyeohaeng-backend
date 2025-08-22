package eightbit.moyeohaeng.domain.member.entity.member;

import org.hibernate.annotations.SQLDelete;

import eightbit.moyeohaeng.domain.member.dto.MemberDto;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@Column(nullable = false)
	private String email;

	//TODO auth 개발 후 response와 log에 출력되지 않게 하기
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column
	private String profileImage;

	public void update(MemberDto.UpdateRequest request) {
		name = request.name() == null ? "" : request.name();
		profileImage = request.profileImage() == null ? "" : request.profileImage();
		password = request.password() == null ? "" : request.password();

	}

	public Member copy(MemberDto.UpdateRequest request) {
		Member member = new Member();
		member.id = id;
		member.email = email;
		member.password = password;
		member.name = name;
		member.profileImage = profileImage;

		return member;
	}

}
