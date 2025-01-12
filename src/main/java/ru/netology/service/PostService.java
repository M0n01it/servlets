package ru.netology.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;
import java.util.List;

@Service
public class PostService {
  private final PostRepository repository;

  @Autowired
  public PostService(PostRepository repository) {
    this.repository = repository;
  }

  public List<Post> all() {
    return repository.all();
  }

  public Post getById(long id) {
    return repository.getById(id).orElseThrow(() ->
            new NotFoundException("Post with id " + id + " not found.")
    );
  }

  public Post save(Post post) {
    return repository.save(post);
  }

  public void removeById(long id) {
    if (!repository.getById(id).isPresent()) {
      throw new NotFoundException("Post with id " + id + " not found.");
    }
    repository.removeById(id);
  }
}
