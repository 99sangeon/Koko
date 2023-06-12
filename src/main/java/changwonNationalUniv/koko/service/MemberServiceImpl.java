package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.request.MemberRequest;
import changwonNationalUniv.koko.entity.Member;
import changwonNationalUniv.koko.exception.MemberNotFoundException;
import changwonNationalUniv.koko.exception.NotLoginException;
import changwonNationalUniv.koko.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final SecurityService securityService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(MemberRequest memberRequest) {
        String rawPassword = bCryptPasswordEncoder.encode(memberRequest.getPassword());
        memberRequest.setPassword(rawPassword);

        memberRepository.save(memberRequest.toEntity());
    }

    @Override
    public Member getCurrentMember() {

        if(!securityService.isAuthenticated()){
            throw new NotLoginException("로그인 후 이용해 주세요.");
        }
        return memberRepository.findByUserId(securityService.getName())
                .orElseThrow(() -> new MemberNotFoundException());
    }

    @Override
    public int getMyRank(String userId) {
        return memberRepository.getMyRank(userId);
    }

}
