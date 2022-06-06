package com.emsthf.webserv.springboot.service.posts;

import com.emsthf.webserv.springboot.domain.posts.Posts;
import com.emsthf.webserv.springboot.domain.posts.PostsRepository;
import com.emsthf.webserv.springboot.web.dto.PostsListResponseDto;
import com.emsthf.webserv.springboot.web.dto.PostsResponseDto;
import com.emsthf.webserv.springboot.web.dto.PostsSaveRequestDto;
import com.emsthf.webserv.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
        // update를 하는데 save같이 DB에 저장하는 쿼리를 던지는 부분이 없는 이유는 JPA의 '영속성 컨텍스트' 때문
        // = 엔티티를 영구 저장하는 환경(JPA의 핵심 내용)

        // JPA의 엔티티 매니저가 활성화된 상태로 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면 이 데이터는 영속성 컨텍스트가 유지된 상태.
        // 이 상태에서 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영
        // = Entity 객체의 값만 변경하면 별도로 Update 쿼리를 날릴 필요가 없음 = '더티 체킹'
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true) // 트랜젝션 범위는 유지하되, 조회 기능만 남겨둬서 조회 속도 개선
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
        // .map(PostsListResponseDto::new)는 람다식 표현
        // = .map(posts -> new PostsListResponseDto(posts))와 같은 표현
        // posts 형식의 List로 리턴 받은 쿼리 결과를 PostsListResponseDto 형식의 List로 변형하는 메소드
    }

    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        postsRepository.delete(posts);
    }
}
