package de.deaddoctor.skillz.skill.data;

import java.io.Serializable;
import java.util.List;

public class SkillData implements Serializable {
    private final String id;
    private List<SkillData> subSkills = null;
    private int completedLevels = 0;

    public SkillData(String id, List<SkillData> subSkills) {
        this.id = id;
        this.subSkills = subSkills;
    }

    public SkillData(String id, int completedLevels) {
        this.id = id;
        this.completedLevels = completedLevels;
    }

    public String getId() {
        return id;
    }

    public List<SkillData> getSubSkills() {
        return subSkills;
    }

    public int getCompletedLevels() {
        return completedLevels;
    }
}
