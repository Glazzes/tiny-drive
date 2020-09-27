package com.tiny.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String filename;
    private int fileSize;
    private String fileType;
}