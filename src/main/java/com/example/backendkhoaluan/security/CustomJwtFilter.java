package com.example.backendkhoaluan.security;

import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomJwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    private Gson gson=new Gson();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromHeader(request);
        if (token != null) {
            String id=jwtUtilsHelper.verifyToken(token);

            if(id!=null){
                UserDetails userDetails=customUserDetailService.loadUserById(Integer.parseInt(id));
                if(userDetails != null) {
                    // Nếu người dùng hợp lệ, set thông tin cho Seturity Context
                    UsernamePasswordAuthenticationToken
                            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContext securityContext=SecurityContextHolder.getContext();

                    securityContext.setAuthentication(authentication);
                }
            }
            // Tạo ra custom type để Gson hỗ trợ parse json kiểu list
//            Type listType=new TypeToken<List<SimpleGrantedAuthority>>() {}.getType();
//            List<GrantedAuthority> listRoles=gson.fromJson(data,listType);
//            if(data!=null){
//                //Bước 4: Nếu giải mã thành công tạo ra Security Context Holder
//                // Tạo ContextHolder để bypass qua các filter của Security
//                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken("","",listRoles);
//                SecurityContext securityContext=SecurityContextHolder.getContext();
//                securityContext.setAuthentication(authenticationToken);
//            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }
        return token;
    }
}
