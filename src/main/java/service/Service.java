package service;

import result.EventResult;
import result.Result;

/**
 * Base class for all services
 */
public abstract class Service {
    /** All services output a result, even if the result is simply "success". We store that here.*/
    private Result result;

    /**
     * When the service is constructed, initialize the result to null
     */
    public Service() {
        result = null;
    }

    public Result getResult() {
        return result;
    }

    /** All services must implement a process function */
    public abstract void process();
}
