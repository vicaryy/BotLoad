package org.vicary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "TWITTER_FILES")
public class TwitterFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TWITTER_ID")
    @NonNull
    private String twitterId;

    @Column(name = "EXTENSION")
    @NonNull
    private String extension;

    @Column(name = "QUALITY")
    @NonNull
    private String quality;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "DURATION")
    private String duration;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "URL")
    private String url;

    @Column(name = "FILE_ID")
    @NonNull
    private String fileId;
}
