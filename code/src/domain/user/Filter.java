package domain.user;

import java.util.List;

public interface Filter<T> {

    List<T> applyFilter(List<T> products);
}
