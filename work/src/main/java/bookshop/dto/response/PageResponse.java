package bookshop.dto.response;

import java.util.List;

/**
 * Generic paginated response wrapper
 * Similar to Spring's Page interface
 */
public class PageResponse<T> {

    private List<T> content;           // The actual data
    private int page;                  // Current page number (0-indexed)
    private int size;                  // Items per page
    private long totalElements;        // Total items in database
    private int totalPages;            // Total number of pages
    private boolean first;             // Is this the first page?
    private boolean last;              // Is this the last page?
    private boolean empty;             // Is the content empty?
    private int numberOfElements;      // Number of items in current page

    // Constructor
    public PageResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.first = (page == 0);
        this.last = (page >= totalPages - 1);
        this.empty = content.isEmpty();
        this.numberOfElements = content.size();
    }

    // Getters and Setters
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean hasNext() {
        return !last;
    }

    public boolean hasPrevious() {
        return !first;
    }
}