package mytimeacty.model.users.enums;

public enum UserRole {
    USER("user"),
    ADMIN("admin"),
    CHIEF("chief"),
    BANNED("banned");

    private final String role;

    /**
     * Constructor for the UserRole enumeration.
     *
     * @param role The string representing the role.
     */
    UserRole(String role) {
        this.role = role;
    }

    /**
     * Gets the string representing the role.
     *
     * @return The string representing the role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Converts a string to a user role.
     *
     * @param role The string representing the role.
     * @return The UserRole enumeration corresponding to the string.
     * @throws IllegalArgumentException If the string does not match any known role.
     */
    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getRole().equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}