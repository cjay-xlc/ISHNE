package io.cjay.common.ecg.ishne;

import io.cjay.common.ecg.ishne.enumeration.Race;
import io.cjay.common.ecg.ishne.enumeration.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    private String id;
    private String firstName;
    private String lastName;
    private Sex sex;
    private Race race;
    private long birth;
}
