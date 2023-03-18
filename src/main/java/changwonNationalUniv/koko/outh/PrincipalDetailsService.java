package changwonNationalUniv.koko.outh;

import changwonNationalUniv.koko.entity.Member;
import changwonNationalUniv.koko.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("userId={}",userId);
        Optional<Member> memberWrapper = memberRepository.findByUserId(userId);

        if(memberWrapper.isEmpty()) {
            return null;
        }

        return new PrincipalDetails(memberWrapper.get());
    }
}
