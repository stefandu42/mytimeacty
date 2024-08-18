package mytimeacty.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {
	
	public static Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

	public static Pageable createPageableSortByDesc(int page, int size, String sortBy) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
    }
}
