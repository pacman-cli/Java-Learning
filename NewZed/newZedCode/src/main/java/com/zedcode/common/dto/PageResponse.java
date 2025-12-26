package com.zedcode.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Page Response DTO for Paginated API Responses
 *
 * This class is used when you need to return a LIST of items with pagination info.
 *
 * WHAT IS PAGINATION?
 * -------------------
 * Instead of returning ALL 10,000 users at once (which is slow!),
 * we return them in "pages" - like pages in a book.
 *
 * Example: 10,000 users divided into pages of 20 users each = 500 pages
 * - Page 0: Users 1-20
 * - Page 1: Users 21-40
 * - Page 2: Users 41-60
 * - ... and so on
 *
 * WHAT IS <T>?
 * ------------
 * <T> is a "Generic Type" - a placeholder for the type of items in the list.
 *
 * Examples:
 * - PageResponse<UserDTO> = A page containing a list of UserDTO objects
 * - PageResponse<ProductDTO> = A page containing a list of ProductDTO objects
 * - PageResponse<OrderDTO> = A page containing a list of OrderDTO objects
 *
 * WHY USE GENERICS?
 * -----------------
 * Instead of creating separate classes like:
 * - UserPageResponse
 * - ProductPageResponse
 * - OrderPageResponse
 *
 * We create ONE class (PageResponse<T>) that works with ANY type!
 *
 * JSON Response Example:
 * {
 *   "content": [
 *     { "id": 1, "name": "John" },
 *     { "id": 2, "name": "Jane" },
 *     { "id": 3, "name": "Bob" }
 *   ],
 *   "pageNumber": 0,
 *   "pageSize": 3,
 *   "totalElements": 100,
 *   "totalPages": 34,
 *   "first": true,
 *   "last": false,
 *   "hasNext": true,
 *   "hasPrevious": false
 * }
 *
 * @param <T> The type of items in the page (e.g., UserDTO, ProductDTO, etc.)
 * @author ZedCode
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * The actual content/data for the current page
     *
     * This is a List<T> which means:
     * - If T is UserDTO, then this is List<UserDTO>
     * - If T is ProductDTO, then this is List<ProductDTO>
     *
     * Example: If you're on page 2 with page size 10,
     * this list will contain items 21-30
     */
    private List<T> content;

    /**
     * Current page number (ZERO-BASED INDEX!)
     *
     * Important: Pages start at 0, not 1!
     * - 0 = First page
     * - 1 = Second page
     * - 2 = Third page
     *
     * Example: If user requests "page 1", they're asking for the SECOND page
     */
    private int pageNumber;

    /**
     * Number of items per page (page size)
     *
     * Example: If pageSize = 20, each page contains up to 20 items
     *
     * Note: The last page might have fewer items!
     * If you have 95 items with pageSize=20:
     * - Pages 0-3 have 20 items each (80 items)
     * - Page 4 has only 15 items
     */
    private int pageSize;

    /**
     * Total number of elements across ALL pages
     *
     * Example: If you have 1,000 users in the database,
     * totalElements = 1000, regardless of page size
     */
    private long totalElements;

    /**
     * Total number of pages
     *
     * Calculation: totalPages = Math.ceil(totalElements / pageSize)
     *
     * Example:
     * - 95 items with pageSize=20 → 5 pages (0,1,2,3,4)
     * - 100 items with pageSize=20 → 5 pages (0,1,2,3,4)
     * - 101 items with pageSize=20 → 6 pages (0,1,2,3,4,5)
     */
    private int totalPages;

    /**
     * Whether this is the LAST page
     *
     * true = No more pages after this one
     * false = There are more pages to load
     *
     * Useful for: Disabling "Next" button in UI
     */
    private boolean last;

    /**
     * Whether this is the FIRST page
     *
     * true = This is page 0 (first page)
     * false = There are pages before this one
     *
     * Useful for: Disabling "Previous" button in UI
     */
    private boolean first;

    /**
     * Whether there is a NEXT page
     *
     * true = You can request pageNumber + 1
     * false = This is the last page
     *
     * Useful for: Showing/hiding "Load More" button
     */
    private boolean hasNext;

    /**
     * Whether there is a PREVIOUS page
     *
     * true = You can request pageNumber - 1
     * false = This is the first page
     *
     * Useful for: Showing/hiding "Previous" button
     */
    private boolean hasPrevious;

    /**
     * Number of elements in the CURRENT page (not all pages!)
     *
     * Usually equals pageSize, except for the last page
     *
     * Example with 95 items, pageSize=20:
     * - Page 0: numberOfElements = 20
     * - Page 1: numberOfElements = 20
     * - Page 2: numberOfElements = 20
     * - Page 3: numberOfElements = 20
     * - Page 4: numberOfElements = 15 (last page!)
     */
    private int numberOfElements;

    /**
     * Whether the current page is empty (has no items)
     *
     * true = content list is empty
     * false = content list has at least one item
     *
     * Useful for: Showing "No results found" message
     */
    private boolean empty;

    // ==================== HELPER METHODS (Optional) ====================

    /**
     * Check if there are any items in this page
     *
     * @return true if content list is not empty
     */
    public boolean hasContent() {
        return content != null && !content.isEmpty();
    }

    /**
     * Get the number of items in this page
     *
     * @return size of content list (0 if empty or null)
     */
    public int getContentSize() {
        return content != null ? content.size() : 0;
    }

    /**
     * Check if this is a single-page result
     * (all data fits in one page)
     *
     * @return true if totalPages <= 1
     */
    public boolean isSinglePage() {
        return totalPages <= 1;
    }
}
