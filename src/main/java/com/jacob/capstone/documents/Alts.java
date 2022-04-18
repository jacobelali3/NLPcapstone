package com.jacob.capstone.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document("alts")
public class Alts {
    @Id
    private UUID id;
    private String dataSetName;
    private String[] alts;
}
