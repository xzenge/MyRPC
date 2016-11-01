package server;

/**
 * Created by Administrator on 2016/11/1.
 */
public class HelloServiceImpl implements HelloService {
    public String hello(String name) {
        return "hello! " + name;
    }
}
