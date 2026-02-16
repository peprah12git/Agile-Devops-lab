package bookshop.dto.request;

/**
 * Pageable request object for pagination and sorting
 * Similar to Spring's Pageable interface
 */
public class PageRequest {

    private int page;              // Page number (0-indexed)
    private int size;              // Items per page
    private String sortBy;         // Field to sort by
    private String direction;      // ASC or DESC

    // Default pagination values
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    // Constructor
    public PageRequest(int page, int size, String sortBy, String direction) {
        this.page = Math.max(page, 0);  // Ensure page >= 0
        this.size = Math.min(Math.max(size, 1), MAX_SIZE);  // Between 1 and MAX_SIZE
        this.sortBy = sortBy;
        this.direction = (direction != null && direction.equalsIgnoreCase("DESC")) ? "DESC" : "ASC";
    }

    // Static factory methods
    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size, null, "ASC");
    }

    public static PageRequest of(int page, int size, String sortBy) {
        return new PageRequest(page, size, sortBy, "ASC");
    }

    public static PageRequest of(int page, int size, String sortBy, String direction) {
        return new PageRequest(page, size, sortBy, direction);
    }

    public static PageRequest defaultPage() {
        return new PageRequest(DEFAULT_PAGE, DEFAULT_SIZE, null, "ASC");
    }

    // Calculate offset for SQL LIMIT/OFFSET
    public int getOffset() {
        return page * size;
    }

    // Getters and Setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(page, 0);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = Math.min(Math.max(size, 1), MAX_SIZE);
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = (direction != null && direction.equalsIgnoreCase("DESC")) ? "DESC" : "ASC";
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}