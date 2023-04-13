package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.request.MemberRequest;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(MemberRequest form) {
        String rawPassword = bCryptPasswordEncoder.encode(form.getPassword());
        form.setPassword(rawPassword);
        memberRepository.save(form.toEntity());
    }
}
