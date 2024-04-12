package everycoding.NalseeFlux.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SimpUserRegistry simpUserRegistry;

    public boolean isUserSubscribed(Long userId, String destination) {
        for (SimpUser user : simpUserRegistry.getUsers()) {
            if (user.getName().equals(userId.toString())) {
                for (SimpSubscription subscription : user.getSessions().stream()
                        .flatMap(session -> session.getSubscriptions().stream())
                        .toList()) {
                    if (subscription.getDestination().equals(destination)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
