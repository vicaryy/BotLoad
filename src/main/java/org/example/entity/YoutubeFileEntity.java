package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class YoutubeFileEntity {
    @Column(name = "YOUTUBE_ID")
    @NonNull
    private String youtubeId;

    @Column(name = "EXTENSION")
    @NonNull
    private String extension;

    @Column(name = "FILE_SIZE")
    private String size;

    @Column(name = "DURATION")
    private String duration;

    @Id
    @Column(name = "ID")
    @NonNull
    private String fileId;
}
