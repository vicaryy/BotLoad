package org.vicary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ID3Tag {

    private String artist;

    private String title;

    private String album;

    private String releaseYear;

    private int genre;
}
