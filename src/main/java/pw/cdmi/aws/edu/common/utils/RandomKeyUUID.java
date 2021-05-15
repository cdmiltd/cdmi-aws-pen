package pw.cdmi.aws.edu.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.cdmi.aws.edu.console.Exception.ConsoleErrorMessages;
import pw.cdmi.core.exception.HttpServiceException;

import java.util.UUID;

public class RandomKeyUUID {

    private static Logger logger = LoggerFactory.getLogger(RandomKeyUUID.class);

    public static String getSecureRandomUUID()
    {
        try
        {
            return UUID.randomUUID().toString().replaceAll("-","") + (int)(Math.random() * 100);
        }
        catch (Exception e)
        {
            logger.error("Error:" + e);
            throw new HttpServiceException(ConsoleErrorMessages.RandomTokenFailException);
        }
    }
    
    public static String getUUID()
    {
        try
        {
            return UUID.randomUUID().toString().replaceAll("-","");
        }
        catch (Exception e)
        {
            logger.error("Error:" + e);
            throw new HttpServiceException(ConsoleErrorMessages.RandomTokenFailException);
        }
    }
}
