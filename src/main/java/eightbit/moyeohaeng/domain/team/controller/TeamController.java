package eightbit.moyeohaeng.domain.team.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/teams")
@Slf4j
public class TeamController implements TeamApi{
	
	@Override
	public ResponseEntity inviteMember() {
		return null;
	}
	
	@Override
	public ResponseEntity creationTeam() {
		return null;
	}
	
	@Override
	public ResponseEntity removeMember() {
		return null;
	}
	
	@Override
	public ResponseEntity updateMemberRole() {
		return null;
	}
}
