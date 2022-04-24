package com.cos.photogramstart.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

// @ 없어도 JpaRepository 상속하면 IoC에 자동 등록
public interface UserRepository extends JpaRepository<User, Integer> { // object, pk type
  // JPA query creation - method names
  User findByUsername(String username);
}
