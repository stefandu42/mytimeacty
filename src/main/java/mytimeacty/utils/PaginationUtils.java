package mytimeacty.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {
	
	/**
     * Creates an object with the specified page number and page size.
     *
     * @param page the page number (zero-based index)
     * @param size the number of items per page
     * @return a Pageable object configured with the specified page number and size
     */
	public static Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

	/**
     * Creates an object with the specified page number, page size, and sorting order (descending).
     *
     * @param page the page number (zero-based index)
     * @param size the number of items per page
     * @param sortBy the field by which to sort the results
     * @return a Pageable object configured with the specified page number, size, and sorting order (descending)
     */
	public static Pageable createPageableSortByDesc(int page, int size, String sortBy) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
    }
	
	/**
     * Creates an object with the specified page number, page size, and sorting order (ascending).
     *
     * @param page the page number (zero-based index)
     * @param size the number of items per page
     * @param sortBy the field by which to sort the results
     * @return a Pageable object configured with the specified page number, size, and sorting order (ascending)
     */
	public static Pageable createPageableSortByAsc(int page, int size, String sortBy) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
    }
}
