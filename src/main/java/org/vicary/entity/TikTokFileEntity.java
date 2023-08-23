package org.vicary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "TIKTOK_FILES")
public class TikTokFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TIKTOK_ID")
    @NonNull
    private String tiktokId;

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
    private String URL;

    @Column(name = "FILE_ID")
    @NonNull
    private String fileId;
}
