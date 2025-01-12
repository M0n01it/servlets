package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.Optional;

public class PostRepository {
  private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  public List<Post> all() {
    return posts.values().stream().collect(Collectors.toList());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      long id = idCounter.getAndIncrement();
      post.setId(id);
      posts.put(id, post);
      return post;
    } else {
      // Обновление существующего поста
      if (posts.replace(post.getId(), post) != null) {
        return post;
      } else {
        // Стратегия: если поста нет, считаем это ошибкой
        throw new NotFoundException("Пост с id " + post.getId() + " не найден.");
      }
    }
  }

  public void removeById(long id) {
    posts.remove(id);
  }
}
