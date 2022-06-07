package com.emsthf.webserv.springboot.web;

import com.emsthf.webserv.springboot.config.auth.dto.SessionUser;
import com.emsthf.webserv.springboot.service.posts.PostsService;
import com.emsthf.webserv.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) { // Model은 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있음
        model.addAttribute("posts", postsService.findAllDesc()); // 쿼리 결과로 가져온 데이터를 posts로 index 머스테치에 전달
        SessionUser user = (SessionUser) httpSession.getAttribute("user"); // 로그인 성공 시 세션에 SessionUser를 저장
        if (user != null) { // 로그인이 성공해서 user가 존재하면
            model.addAttribute("userName", user.getName()); // 세션에서 유저의 이름을 꺼내 모델에 넣는다
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
