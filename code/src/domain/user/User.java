package domain.user;

public class User {
    private UserState us;

    public User()
    {
        us = null;
    }

    /***
     *
     */
    public void enterMarket()
    {
        us = new Guest();
    }

    /***
     *
     */
    public void leaveMarket()
    {
        us = null;
    }

    /***
     *
     * @param id
     * @param pw
     */
    public void register(String id, String pw)
    {
        throw new UnsupportedOperationException();
    }

    /***
     *
     * @param id
     * @param pw
     */
    public void login(String id, String pw)
    {
        throw new UnsupportedOperationException();
    }

    /***
     *
     */
    public void logout()
    {
        throw new UnsupportedOperationException();
    }



}
