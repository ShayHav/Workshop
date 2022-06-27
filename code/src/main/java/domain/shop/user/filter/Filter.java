package domain.shop.user.filter;

import java.util.List;

public interface Filter<T> {

    List<T> applyFilter(List<T> listToFilter);
}
