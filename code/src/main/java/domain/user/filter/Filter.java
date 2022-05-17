package domain.user.filters;

import java.util.List;

public interface Filter<T> {

    List<T> applyFilter(List<T> listToFilter);
}
