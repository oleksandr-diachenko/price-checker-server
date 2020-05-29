package oleksandrdiachenko.pricechecker.service;

import lombok.RequiredArgsConstructor;
import oleksandrdiachenko.pricechecker.repository.FileRepository;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {


    private final FileRepository fileRepository;
    private final FileStatusRepository fileStatusRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        fileStatusRepository.deleteAll();
        fileRepository.deleteAll();
    }
}
