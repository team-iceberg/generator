package iceberg.generator.services;

import iceberg.generator.exceptions.ServiceException;

import java.io.InputStream;
import java.util.Map;

/**
 * Combination service interface
 */
public interface GraduationService {

    /**
     * Manage uploaded memberships file
     *
     * @param file graduations file input stream
     * @return Combination list
     * @throws ServiceException when an error occurs
     */
    Map<String, Integer> getGraduations(InputStream file) throws ServiceException;
}
