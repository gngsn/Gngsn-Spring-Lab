package com.gngsn.jpademo.repository;

import com.gngsn.jpademo.vo.MovieVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<MovieVO, Long> {

    @Override
    Page<MovieVO> findAll(Pageable pageable);

    //    List<MovieVO> findByName(String name);

//    List<MovieVO> findByNameLike(String keyword);
}