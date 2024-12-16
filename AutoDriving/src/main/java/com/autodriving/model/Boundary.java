package com.autodriving.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boundary")
public final class Boundary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int x;

    private int y;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(x);
        sb.append(" x ");
        sb.append(y);
        return sb.toString();
    }
}
