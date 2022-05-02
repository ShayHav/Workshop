package Testing_System;

import javax.lang.model.element.Element;

public class Result<T, S> {
    private T ele1;
    private S ele2;
    public Result(T ele1, S ele2)
    {
        this.ele1 = ele1;
        this.ele2 = ele2;
    }

    public T GetFirstElement() { return ele1;}

    public S GetSecondElement() { return ele2;}
}
