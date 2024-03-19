//package everycoding.NalseeFlux.event;
//
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
//import reactor.core.publisher.Flux;
//
//public interface EventRepository extends ReactiveMongoRepository<Event, String> {
//
//    Flux<Event> findByReceiverId(Long receiverId);
//    @Query("{ 'receiverId': ?0, '_id': { '$gt': ?1 } }")
//    Flux<Event> findWithInfiniteScroll(Long receiverId, String afterId, Pageable pageable);
//
//}