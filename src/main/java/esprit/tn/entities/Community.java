package esprit.tn.entities;

import esprit.tn.services.UserService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Community {
    private int id;
    private String name;
    private String description;
    private int creatorId;
    private LocalDateTime createdAt;
    private List<Integer> members;

    public Community() {}

    public Community(String name, String description, int creatorId) {
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.createdAt = LocalDateTime.now();
        this.members = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Integer> getMembers() {
        return members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }

    public void addMember(int userId) {
        if (!members.contains(userId)) {
            members.add(userId);
        }
    }

    public void removeMember(int userId) {
        members.remove((Integer) userId);
    }

    public String getMembersAsString() {
        return String.join(",", members.stream().map(String::valueOf).toArray(String[]::new));
    }

    public void setMembersFromString(String membersString) {
        if (membersString == null || membersString.isEmpty()) {
            this.members = new ArrayList<>();
        } else {
            this.members = Arrays.stream(membersString.split(","))
                    .map(Integer::parseInt)
                    .toList();
        }
    }

    public String getMembersNamesAsString(UserService userService) {
        StringBuilder membersNames = new StringBuilder();
        for (Integer memberId : members) {

                Users user = userService.getUserById2(memberId);
                if (user != null) {
                    membersNames.append(user.getFullName()).append("\n");
                }

        }
        return membersNames.toString().trim();
    }

    @Override
    public String toString() {
        return "Community{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creatorId=" + creatorId +
                ", createdAt=" + createdAt +
                ", members=" + members +
                '}';
    }
}