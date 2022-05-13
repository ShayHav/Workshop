package Testing_System;

public class Selector {

    public Selector()
    { }
    public static Bridge GetBridge()
    {
        return new RealBridge();
        //return new ProxyBridge();
    }

}
