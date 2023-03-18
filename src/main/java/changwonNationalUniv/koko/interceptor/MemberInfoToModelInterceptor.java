package changwonNationalUniv.koko.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MemberInfoToModelInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        addMemberInfoToModel(modelAndView);
    }

    private static void addMemberInfoToModel(ModelAndView modelAndView) {
        String userId = null;

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("name={}", name);
        if(!name.equals("anonymousUser")) {
            userId = name;
        }

        if(modelAndView == null) {
            modelAndView = new ModelAndView();
        }

        modelAndView.addObject("userId", userId);
    }
}
